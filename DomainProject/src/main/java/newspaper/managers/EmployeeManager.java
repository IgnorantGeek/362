package newspaper.managers;

import newspaper.models.Employee;
import newspaper.models.SalaryEmployee;
import newspaper.models.HourlyEmployee;
import newspaper.Global;
import newspaper.ui.Command;

import java.io.File;
import java.util.HashMap;
import java.util.Scanner;

/**
 * EmployeeManager - Manages Employees and Login Verification
 * @author Nick Heisler and Jonah Armstrong
 */
public class EmployeeManager implements Commandable
{
    // Variables
    private int idCounter;
    private int employeeCount;
    private HashMap<Integer, Employee> registry;

    // Constructors
    public EmployeeManager()
    {
        registry = new HashMap<>();
        idCounter = 0;
        employeeCount = 0;
        this.init();
    }

    public EmployeeManager(int start)
    {
        this.idCounter = start;
        employeeCount = 0;
        registry = new HashMap<>();
        this.init();
    }

    // Methods

    /**
     * Adds a salaried employee to the system
     * @param employeeName Name of the new employee
     * @param password Password for the new employee
     * @param supervisorId ID of the employee this user will report to
     * @param hourlyRate Hourly pay rate of the employee
     * @return The ID of the new employee, -1 on error
     */
    public int addHourlyEmployee(String employeeName, String password, int supervisorId, double hourlyRate)
    {
        Employee in = new HourlyEmployee(idCounter++, password, supervisorId, employeeName, hourlyRate);
        employeeCount++;

        registry.put(in.Id(), in);

        if (in.write() < 0) return -1;
        return in.Id();
    }
    
    /**
     * Adds a salaried employee to the system
     * @param employeeName Name of the new employee
     * @param password Password for the new employee
     * @param supervisorId ID of the employee this user will report to
     * @param salary Yearly salary of the employee
     * @return The ID of the new employee, -1 on error
     */
    public int addSalariedEmployee(String employeeName, String password, int supervisorId, double salary)
    {
        Employee in = new SalaryEmployee(idCounter++, password, supervisorId, employeeName,salary);
        employeeCount++;

        registry.put(in.Id(), in);

        if (in.write() == 0) return in.Id();
        else return -1;
    }

    /**
     * Drops an Employee from the Database
     * @param ID - Id of the employee to drop
     * @return 0 on success, Less than zero for error code
     */
    public int dropEmployee(Employee loggedIn, int ID)
    {
        // Search hashmap for entry
        Employee find = registry.get(ID);
        if (find == null) return -1;

        // Check if logged in user is super user (owner)
        if (loggedIn.Id() != -1)
        {
            // Check if logged in user is allowed to drop this employee
            if (!checkPrivilege(loggedIn, ID))
            {
                // not authorized to remove this employee
                return -2;
            }
        }

        // Remove from registry and delete from db
        registry.remove(ID);
        if (find.delete() < 0) return -3;

        // Successful return
        employeeCount--;
        return 0;
    }

    public boolean updateSupervisorId(int updateEmployeeID, int supervisorId)
    {
        Employee update = registry.get(updateEmployeeID);

        if (update == null) return false;

        update.setSupervisorId(supervisorId);
        return true;
    }

    public boolean updateFullName(int updateEmployeeID, String fullName)
    {
        Employee update = registry.get(updateEmployeeID);

        if (update == null) return false;

        update.setFullName(fullName);
        return true;
    }

    public boolean updatePassword(int updateEmployeeID, String password)
    {
        Employee update = registry.get(updateEmployeeID);

        if (update == null) return false;

        update.setPassword(password);
        return true;
    }



    /**
     * Checks whether some employee is authorized to make changes to some other employee
     * @param supervisor the supervisor to check
     * @param workerID the worker who's supervisor we are checking
     * @return true if Employee has supervisor privileges, false otherwise
     */
    public boolean checkPrivilege(Employee supervisor, int workerID)
    {
        Employee worker = registry.get(workerID);

        if (worker == null) return false;
        if (supervisor.Id() == worker.supervisorId()) return true;
        else
        {
            Employee scan = registry.get(worker.supervisorId());
            while (scan != null)
            {
                if (supervisor.Id() == scan.supervisorId()) return true;
                scan = registry.get(scan.supervisorId());
            }
        }
        return false;
    }

    /**
     * Function to check for the presence of some employee ID in the registry
     * @param userID the ID of the user to check for
     * @return true if the user exists, false otherwise
     */
    public boolean checkID(int userID)
    {
        return registry.containsKey(userID);
    }

    /**
     * Initializes the Employees Database and builds any present
     * Employee files into memory
     */
    public void init()
    {
        // Check for presence of Employee DB entry, build if not present
        File rootDir = new File(Global.EMPLOYEE_DB_PATH);

        if (!rootDir.exists())
        {
            rootDir.mkdir();
            return;
        }

        for (String employeeFileName : rootDir.list())
        {
            Employee employee = buildFromFile(employeeFileName);

            if (employee != null) 
            {
                registry.put(employee.Id(), employee);
                employeeCount++;
            }

            // Prevents us from overwriting employee files that failed to add
            idCounter++;
        }
    }

    /**
     * Builds an Employee class from a filename
     * @param fileName - Name of file (relative path)
     * @return New Employee class, null if build failed
     */
    public Employee buildFromFile(String fileName)
    {
        Employee out = null;
        File employeeFile = new File(Global.EMPLOYEE_DB_PATH + fileName);
        Scanner fileScanner;

        try 
        {
            fileScanner = new Scanner(employeeFile);

            String name = fileScanner.nextLine();
            int supIdString = Integer.parseInt(fileScanner.nextLine());
            String password = fileScanner.nextLine();
            int type = Integer.parseInt(fileScanner.nextLine());

            String fname = "";
            for (int i = 0; i < fileName.length(); i++)
            {
                if (fileName.charAt(i) == '.') break;
                else fname += fileName.charAt(i);
            }
            int id = Integer.parseInt(fname);
            
            if(type == 0) {
            	double salary = Double.parseDouble(fileScanner.nextLine());
            	out = new SalaryEmployee(id, password, supIdString, name, salary);
            }
            
            else {
            	double rate = Double.parseDouble(fileScanner.nextLine());
            	double hours = Double.parseDouble(fileScanner.nextLine());
            	out = new HourlyEmployee(id, password, supIdString, name, rate, hours);
            	
            }
            

            fileScanner.close();
        }
        catch (Exception e)
        {
            System.out.println("ERROR: Failed to add employee file: " + fileName);
            System.out.println(e.getMessage());
            return null;
        }

        return out;
    }

    /**
     * Get the number of employees in the system
     * @return The number of active employees
     */
    public int getEmployeeCount()
    {
        return employeeCount;
    }

    /**
     * Returns the current value of the ID count
     * @return
     */
    public int counter()
    {
        return idCounter;
    }

    /**
     * Validates the login and returns the logged in Employee
     * @param userID
     * @param password
     * @return Employee object of the matching user, or Null
     */
    public Employee validateLogin(int userID, String password)
    {
        if (registry == null) return null;
        else if (!registry.containsKey(userID)) return null;
        else
        {
            Employee login = registry.get(userID);
            if (login.Password().compareTo(password) == 0) return login;
            else return null;
        }
    }
    
    public HashMap<Integer, Employee> getRegistry()
    {
    	return registry;
    }

    @Override
    public String executeCommand(Employee loggedIn, Command command)
    {
        StringBuilder build = new StringBuilder();
        switch (command.getCommand())
        {
            case "add":
                // Check params
                if (command.getOptions().size() < 2)
                {
                    build.append("Employee cmd Error: Not enough arguments\n");
                    build.append("\nExpected - add <name> <password> ");
                    break;
                }
                if (command.getOptions().size() == 2)
                {
                    // Default hourly employee
                    String name = command.getOptions().get(0);
                    String password = command.getOptions().get(1);

                    int ret = addHourlyEmployee(name, password, loggedIn.Id(), Global.DEFAULT_WAGE);

                    if (ret < 0) {
                        // Employee add failure
                        build.append("Employee internal Error: Error writing new Employee");
                    }
                    else build.append("Successfully added Hourly Employee with ID: ").append(ret);
                    break;
                }
                if (command.getOptions().size() == 3)
                {
                    build.append("Employee cmd Error: Not enough arguments\n");
                    build.append("Expected - add <name> <password> [-i <supervisorId>] [-s <salary>]");
                    build.append(" || [-h <hourlyRate>]");
                    break;
                }
                if (command.getOptions().size() == 4)
                {
                    // Check flag
                    String name = command.getOptions().get(0);
                    String password = command.getOptions().get(1);
                    String flag = command.getOptions().get(2);
                    int supId = loggedIn.Id();
                    double rate = Global.DEFAULT_WAGE;
                    switch (flag)
                    {
                        case "-i":
                        case "-I":
                            try {
                                supId = Integer.parseInt(command.getOptions().get(3));
                            }
                            catch (NumberFormatException e)
                            {
                                build.append("Employee cmd Error: Supervisor Id argument '");
                                build.append(command.getOptions().get(3)).append("' is not a valid number");
                                // Exit
                                return build.toString();
                            }
                            break;
                        case "-h":
                        case "-H":
                            // Hourly employee
                            try {
                                rate = Double.parseDouble(command.getOptions().get(3));
                            }
                            catch (NumberFormatException e)
                            {
                                build.append("Employee cmd Error: Hourly rate argument '");
                                build.append(command.getOptions().get(3)).append("' is not a valid number");
                                return build.toString();
                            }
                            break;
                        case "-s":
                        case "-S":
                            // Salary employee
                            try {
                                rate = Double.parseDouble(command.getOptions().get(3));
                            }
                            catch (NumberFormatException e)
                            {
                                build.append("Employee cmd Error: Salary argument '");
                                build.append(command.getOptions().get(3)).append("' is not a valid number");
                                return build.toString();
                            }
                            break;
                        default:
                            build.append("Employee cmd Error: Invalid flag '").append(flag).append("'\n");
                            build.append("Expected - add <name> <password> [-i <supervisorId>] [-s <salary>]");
                            build.append(" || [-h <hourlyRate>]");
                            // Exit
                            return build.toString();
                    }

                    int ret;
                    if (flag.compareTo("-s") == 0
                    ||  flag.compareTo("-S") == 0)
                    {
                        ret = addSalariedEmployee(name, password, supId, rate);

                        if (ret < 0) {
                            // Employee add failure
                            build.append("Employee internal Error: Error writing new Employee");
                        } else build.append("Successfully added Salaried Employee with ID: ").append(ret);
                    }
                    else
                    {
                        ret = addHourlyEmployee(name, password, supId, rate);

                        if (ret < 0) {
                            // Employee add failure
                            build.append("Employee internal Error: Error writing new Employee");
                        } else build.append("Successfully added Hourly Employee with ID: ").append(ret);
                    }
                    break;
                }
                if (command.getOptions().size() == 5)
                {
                    build.append("Employee cmd Error: Not enough arguments\n");
                    build.append("Expected - add <name> <password> [-i <supervisorId>] [-s <salary>]");
                    build.append(" || [-h <hourlyRate>]");
                    break;
                }

                String name = command.getOptions().get(0);
                String password = command.getOptions().get(1);
                String flag1 = command.getOptions().get(2);
                String flag2 = command.getOptions().get(4);
                int supId = loggedIn.Id();
                double rate = -1;
                int ret;

                // First flag
                switch (flag1)
                {
                    case "-h":
                    case "-H":
                        // Hourly employee
                        try {
                            rate = Double.parseDouble(command.getOptions().get(3));
                        }
                        catch (NumberFormatException e)
                        {
                            build.append("Employee cmd Error: Hourly rate argument '");
                            build.append(command.getOptions().get(3)).append("' is not a valid number");
                            return build.toString();
                        }
                        break;
                    case "-s":
                    case "-S":
                        // Salary employee
                        try {
                            rate = Double.parseDouble(command.getOptions().get(3));
                        }
                        catch (NumberFormatException e)
                        {
                            build.append("Employee cmd Error: Salary argument '");
                            build.append(command.getOptions().get(3)).append("' is not a valid number");
                            return build.toString();
                        }
                        break;
                    case "-i":
                    case "-I":
                        try {
                            supId = Integer.parseInt(command.getOptions().get(3));
                        }
                        catch (NumberFormatException e)
                        {
                            build.append("Employee cmd Error: Supervisor Id argument '");
                            build.append(command.getOptions().get(3)).append("' is not a valid number");
                            // Exit
                            return build.toString();
                        }
                        break;

                    default:
                        build.append("Employee cmd Error: Not enough arguments\n");
                        build.append("Expected - add <name> <password> [-i <supervisorId>] [-s <salary>]");
                        build.append(" || [-h <hourlyRate>]\n");
                        build.append("Invalid flag: ").append(flag1);
                }

                // Flag 2
                switch (flag2)
                {
                    case "-h":
                    case "-H":
                        // Hourly employee
                        if (rate != -1)
                        {
                            build.append("Employee cmd Error: Duplicate employee type. Employee can only be ");
                            build.append("hourly (-h) or salaried (-s).");
                            return build.toString();
                        }
                        try {
                            rate = Double.parseDouble(command.getOptions().get(3));
                        }
                        catch (NumberFormatException e)
                        {
                            build.append("Employee cmd Error: Hourly rate argument '");
                            build.append(command.getOptions().get(3)).append("' is not a valid number");
                            return build.toString();
                        }
                        break;
                    case "-s":
                    case "-S":
                        // Salary employee
                        if (rate != -1)
                        {
                            build.append("Employee cmd Error: Duplicate employee type. Employee can only be ");
                            build.append("hourly (-h) or salaried (-s).");
                            return build.toString();
                        }
                        try {
                            rate = Double.parseDouble(command.getOptions().get(3));
                        }
                        catch (NumberFormatException e)
                        {
                            build.append("Employee cmd Error: Salary argument '");
                            build.append(command.getOptions().get(3)).append("' is not a valid number");
                            return build.toString();
                        }
                        break;
                    case "-i":
                    case "-I":
                        if (flag1.compareTo(flag2) == 0)
                        {
                            build.append("Employee cmd Error: Duplicate supervisor Id flag (-i)");
                            return build.toString();
                        }
                        try {
                            supId = Integer.parseInt(command.getOptions().get(3));
                        }
                        catch (NumberFormatException e)
                        {
                            build.append("Employee cmd Error: Supervisor Id argument '");
                            build.append(command.getOptions().get(3)).append("' is not a valid number");
                            // Exit
                            return build.toString();
                        }
                        break;
                }

                // Check new Employee type
                if (flag1.compareTo("-s") == 0
                ||  flag1.compareTo("-S") == 0
                ||  flag2.compareTo("-s") == 0
                ||  flag2.compareTo("-S") == 0)
                {
                    // Salaried
                    ret = addSalariedEmployee(name, password, supId, rate);

                    if (ret < 0) {
                        // Employee add failure
                        build.append("Employee internal Error: Error writing new Employee");
                    }
                    else build.append("Successfully added Hourly Employee with ID: ").append(ret);
                }
                else if (flag1.compareTo("-h") == 0
                ||  flag1.compareTo("-H") == 0
                ||  flag2.compareTo("-h") == 0
                ||  flag2.compareTo("-H") == 0)
                {
                    // Salaried
                    ret = addHourlyEmployee(name, password, supId, rate);

                    if (ret < 0) {
                        // Employee add failure
                        build.append("Employee internal Error: Error writing new Employee");
                    }
                    else build.append("Successfully added Hourly Employee with ID: ").append(ret);
                }
                break;
            case "remove":
                if (command.getOptions() == null
                ||  command.getOptions().size() < 1)
                {
                    build.append("Employee cmd Error: Not enough arguments\n");
                    build.append("Expected - remove <id1> <id2> ... <idn>");
                    break;
                }
                for (String op : command.getOptions())
                {
                    int ID;

                    try {
                        ID = Integer.parseInt(op);
                    }
                    catch (NumberFormatException e)
                    {
                        build.append("Argument '").append(op).append("' not a valid number. Skipped\n");
                        continue;
                    }


                }
                break;
            case "update":
                break;
            default:
                build.append("Employee cmd Error: No binding found for '").append(command.getCommand()).append("'");
        }
        return build.toString();
    }
}
