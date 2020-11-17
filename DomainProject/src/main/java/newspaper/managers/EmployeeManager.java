package newspaper.managers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Scanner;

import newspaper.Global;
import newspaper.models.Employee;
import newspaper.models.HourlyEmployee;
import newspaper.models.SalaryEmployee;
import newspaper.models.Task;
import newspaper.ui.Command;

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
    private HashMap<Integer, ArrayList<Task>> tasks;

    // Constructors
    public EmployeeManager()
    {
        registry = new HashMap<>();
        tasks = new HashMap<Integer, ArrayList<Task>>();

        idCounter = 0;
        employeeCount = 0;
        this.init();
        this.initTasks();
    }

    public EmployeeManager(int start)
    {
        this.idCounter = start;
        employeeCount = 0;
        registry = new HashMap<>();
        tasks = new HashMap<Integer, ArrayList<Task>>();
        this.init();
        this.initTasks();
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
    public int addHourlyEmployee(String employeeName, String password, int supervisorId, int clearance, double hourlyRate)
    {
        Employee in = new HourlyEmployee(idCounter++, password, supervisorId, employeeName, clearance, hourlyRate);
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
    public int addSalariedEmployee(String employeeName, String password, int supervisorId, int clearance, double salary)
    {
        Employee in = new SalaryEmployee(idCounter++, password, supervisorId, employeeName, clearance, salary);
        employeeCount++;

        registry.put(in.Id(), in);

        if (in.write() == 0) return in.Id();
        else return -1;
    }


    public boolean addTask(int id, String desc)
    {
        Task t = new Task(desc, id);
        ArrayList<Task> list = tasks.get(id);
        if(list == null) {
            list = new ArrayList<Task>();
            tasks.put(id, list);
        }
        list.add(t);
        return writeTask(t);
    }

    /**
     * gets all unfinished tasks for given employee
     * @param id Id of the employee
     * @return ArrayList of the tasks, null on error
     */
    public ArrayList<Task> getTasks(int id)
    {
        return tasks.get(id);
    }

    /**
     * deletes a task from memory
     * @param t task to be deleted
     * @return true it delete is successful, false if not
     */
    public boolean deleteTask(Task t)
    {
        ArrayList<Task> list = tasks.get(t.getID());
        if(list == null) {
            return false;
        }
        for(int i = 0; i < tasks.size(); i++) {
            Task t2 = list.get(i);
            if(t2.getDesc().equals(t.getDesc())) {
                list.remove(i);
                return eraseTask(t2);
            }
        }
        return false;
    }

    /**
     * writes a task to file
     * @param t task to be deleted
     * @return true it delete is successful, false if not
     */
    public boolean writeTask(Task t)
    {
        PrintWriter out = null;
        try {
            out = new PrintWriter(new BufferedWriter(new FileWriter(Global.TASK_DB_PATH, true)));
            out.print("\n" + t.toString());
        } catch (IOException e) {
            System.out.println("Tasks File system corrupted. Seek IT help.");
            return false;
        } finally {
            if (out != null) {
                out.close();
            }
        }
        return true;
    }

    /**
     * erases a task from file
     * @param t Task to be erased
     * @return true it delete is successful, false if not
     */
    public boolean eraseTask(Task t)
    {
        if(t == null) {
            return false;
        }
        tasks.remove(t);
        Collection<ArrayList<Task>> tsks = tasks.values();
        PrintWriter out = null;
        try {
            out = new PrintWriter(new BufferedWriter(new FileWriter(Global.TASK_DB_PATH, false)));
            int i = 0;
            for(ArrayList<Task> tsklst: tsks) {
                for(Task tsk: tsklst) {
                    if(i > 0)
                        out.print("\n");
                    out.print(tsk.toString());
                    i++;
                }
            }
        } catch (IOException e) {
            System.out.println("Task file system corrupted. Seek IT help.");
            return false;
        } finally {
            if (out != null) {
                out.close();
            }
        }
        return true;

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
     * Initializes the Tasks Database and builds any present
     * Employee files into memory
     * @return true on success, false otherwise
     */
    public void initTasks()
    {
        File Archive =  new File(Global.TASK_DB_PATH);
        try {
            Scanner s = new Scanner(Archive);
            while(s.hasNextLine())
            {
                String[] line = s.nextLine().split(",");
                int id = Integer.parseInt(line[0]);
                String desc = line[1].trim();
                Task t = new Task(desc, id);
                ArrayList<Task> tlist = tasks.get(id);
                if(tlist == null) {
                    tlist = new ArrayList<Task>();
                    tasks.put(id, tlist);
                }
                tlist.add(t);
            }
            s.close();
        } catch (FileNotFoundException e) {
            System.out.println("Task file is inaccessible.");
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
            int clearance = Integer.parseInt(fileScanner.nextLine());
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
                out = new SalaryEmployee(id, password, supIdString, name, clearance, salary);
            }

            else {
                double rate = Double.parseDouble(fileScanner.nextLine());
                double hours = Double.parseDouble(fileScanner.nextLine());
                out = new HourlyEmployee(id, password, supIdString, name, clearance, rate, hours);

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

    public Employee getEmployee(int EmployeeID) { return registry.get(EmployeeID); }

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
                if (command.getOptions().size() < 3)
                {
                    build.append("Employee cmd Error: Not enough arguments\n");
                    build.append("Expected - add <name> <password> <clearance> [-i <supervisorId>] [-s <salary>]");
                    build.append(" || [-h <hourlyRate>]");
                    break;
                }
                if (command.getOptions().size() == 3)
                {
                    // Default hourly employee
                    String name = command.getOptions().get(0);
                    String password = command.getOptions().get(1);
                    int clearance;
                    try {
                        clearance = Integer.parseInt(command.getOptions().get(2));
                    }
                    catch (NumberFormatException e)
                    {
                        build.append("Employee cmd Error: Entered clearance parameter '");
                        build.append(command.getOptions().get(2)).append("' is not a valid number");
                        return build.toString();
                    }

                    int ret = addHourlyEmployee(name, password, loggedIn.Id(), clearance, Global.DEFAULT_WAGE);

                    if (ret < 0) {
                        // Employee add failure
                        build.append("Employee internal Error: Error writing new Employee");
                    }
                    else build.append("Successfully added Hourly Employee with ID: ").append(ret);
                    break;
                }
                if (command.getOptions().size() == 4)
                {
                    build.append("Employee cmd Error: Not enough arguments\n");
                    build.append("Expected - add <name> <password> <clearance> [-i <supervisorId>] [-s <salary>]");
                    build.append(" || [-h <hourlyRate>]");
                    break;
                }
                if (command.getOptions().size() == 5)
                {
                    // Check flag
                    String name = command.getOptions().get(0);
                    String password = command.getOptions().get(1);
                    int clearance;
                    try {
                        clearance = Integer.parseInt(command.getOptions().get(2));
                    }
                    catch (NumberFormatException e)
                    {
                        build.append("Employee cmd Error: Entered clearance parameter '");
                        build.append(command.getOptions().get(2)).append("' is not a valid number");
                        return build.toString();
                    }
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
                            build.append("Expected - add <name> <password> <clearance> [-i <supervisorId>] [-s <salary>]");
                            build.append(" || [-h <hourlyRate>]");
                            // Exit
                            return build.toString();
                    }

                    int ret;
                    if (flag.compareTo("-s") == 0
                            ||  flag.compareTo("-S") == 0)
                    {
                        ret = addSalariedEmployee(name, password, supId, clearance, rate);

                        if (ret < 0) {
                            // Employee add failure
                            build.append("Employee internal Error: Error writing new Employee");
                        } else build.append("Successfully added Salaried Employee with ID: ").append(ret);
                    }
                    else
                    {
                        ret = addHourlyEmployee(name, password, supId, clearance, rate);

                        if (ret < 0) {
                            // Employee add failure
                            build.append("Employee internal Error: Error writing new Employee");
                        } else build.append("Successfully added Hourly Employee with ID: ").append(ret);
                    }
                    break;
                }
                if (command.getOptions().size() == 6)
                {
                    build.append("Employee cmd Error: Not enough arguments\n");
                    build.append("Expected - add <name> <password> <clearance> [-i <supervisorId>] [-s <salary>]");
                    build.append(" || [-h <hourlyRate>]");
                    break;
                }

                String name = command.getOptions().get(0);
                String password = command.getOptions().get(1);
                int clearance;
                try {
                    clearance = Integer.parseInt(command.getOptions().get(2));
                }
                catch (NumberFormatException e)
                {
                    build.append("Employee cmd Error: Entered clearance parameter '");
                    build.append(command.getOptions().get(2)).append("' is not a valid number");
                    return build.toString();
                }
                String flag1 = command.getOptions().get(3);
                String flag2 = command.getOptions().get(5);
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
                            rate = Double.parseDouble(command.getOptions().get(4));
                        }
                        catch (NumberFormatException e)
                        {
                            build.append("Employee cmd Error: Hourly rate argument '");
                            build.append(command.getOptions().get(4)).append("' is not a valid number");
                            return build.toString();
                        }
                        break;
                    case "-s":
                    case "-S":
                        // Salary employee
                        try {
                            rate = Double.parseDouble(command.getOptions().get(4));
                        }
                        catch (NumberFormatException e)
                        {
                            build.append("Employee cmd Error: Salary argument '");
                            build.append(command.getOptions().get(4)).append("' is not a valid number");
                            return build.toString();
                        }
                        break;
                    case "-i":
                    case "-I":
                        try {
                            supId = Integer.parseInt(command.getOptions().get(4));
                        }
                        catch (NumberFormatException e)
                        {
                            build.append("Employee cmd Error: Supervisor Id argument '");
                            build.append(command.getOptions().get(4)).append("' is not a valid number");
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
                            rate = Double.parseDouble(command.getOptions().get(6));
                        }
                        catch (NumberFormatException e)
                        {
                            build.append("Employee cmd Error: Hourly rate argument '");
                            build.append(command.getOptions().get(6)).append("' is not a valid number");
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
                            rate = Double.parseDouble(command.getOptions().get(6));
                        }
                        catch (NumberFormatException e)
                        {
                            build.append("Employee cmd Error: Salary argument '");
                            build.append(command.getOptions().get(6)).append("' is not a valid number");
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
                            supId = Integer.parseInt(command.getOptions().get(6));
                        }
                        catch (NumberFormatException e)
                        {
                            build.append("Employee cmd Error: Supervisor Id argument '");
                            build.append(command.getOptions().get(6)).append("' is not a valid number");
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
                    ret = addSalariedEmployee(name, password, supId, clearance, rate);

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
                    ret = addHourlyEmployee(name, password, supId, clearance, rate);

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

                    int dropStatus = dropEmployee(loggedIn, ID);

                    if (dropStatus == 0)
                    {
                        build.append("Successfully dropped Employee: ").append(ID).append('\n');
                    }
                    else if (dropStatus == -1)
                    {
                        build.append("Employee internal Error: No Employee found with ID ").append(ID);
                        build.append(". Skipped\n");
                    }
                    else if (dropStatus == -2)
                    {
                        build.append("Employee internal Error: User not authorized to drop Employee ").append(ID);
                        build.append(". Skipped\n");
                    }
                    else if (dropStatus == -3)
                    {
                        build.append("Employee internal Error: Error writing Employee ").append(ID);
                        build.append(" to database. Skipped\n");
                    }
                }
                break;
            case "update":
                // Check params
                if (command.getOptions().size() < 1)
                {
                    build.append("Employee cmd Error: Missing required Employee ID\n");
                    build.append("Expected - update <ID> [-n <name>] [-p <password>] [-i <supervisorId>");
                    break;
                }
                if (command.getOptions().size() < 3)
                {
                    build.append("Employee cmd Error: Missing update parameters\n");
                    build.append("Expected - update <ID> [-n <name>] [-p <password>] [-i <supervisorId>");
                    break;
                }

                int findID;
                Employee find;

                // Check Employee param
                try {
                    findID = Integer.parseInt(command.getOptions().get(0));
                }
                catch (NumberFormatException e)
                {
                    build.append("Employee cmd Error: Entered value for supervisor ID is not a number: ");
                    build.append(command.getOptions().get(0));
                    break;
                }

                // Check Employee
                if (registry.containsKey(findID)) find = registry.get(findID);
                else
                {
                    build.append("Employee cmd Error: No Employee found with ID ").append(findID);
                    break;
                }

                String updateName = null;
                String updatePass = null;
                int    updateID   = -1;

                // Check update options
                for (int i = 1; i < command.getOptions().size(); i+=2)
                {
                    // If all values have been updated, exit
                    if (updateName != null
                            &&  updatePass != null
                            &&  updateID   != -1) break;

                    // Get the next flag
                    String flag = command.getOptions().get(i);

                    if ((i+1) >= command.getOptions().size()
                            ||  command.getOptions().get(i+1).charAt(0) == '-')
                    {
                        // There are either no more values to parse, or
                        // The expected value is missing
                        build.append("Employee cmd Error: Missing expected value for flag '");
                        build.append(flag).append("'");
                        return build.toString();
                    }

                    if (!checkPrivilege(loggedIn, find.Id()))
                    {
                        build.append("Employee cmd Error: User not authorized to update this user.");
                        return build.toString();
                    }

                    switch (flag)
                    {
                        case "-n":
                        case "-N":
                            if (updateName == null) updateName = command.getOptions().get(i+1);
                            break;

                        case "-p":
                        case "-P":
                            if (updatePass == null ) updatePass = command.getOptions().get(i+1);
                            break;

                        case "-i":
                        case "-I":
                            if (updateID == -1)
                            {
                                try {
                                    updateID = Integer.parseInt(command.getOptions().get(i+1));
                                }
                                catch (NumberFormatException e) {
                                    build.append("Employee cmd Error: Supervisor ID not a number");
                                    return build.toString();
                                }
                            }
                            break;
                        default:
                            build.append("Employee cmd Error: No binding found for flag '").append(flag).append("'");
                            return build.toString();
                    }
                }

                if (updateID != -1) find.setSupervisorId(updateID);
                if (updateName != null) find.setFullName(updateName);
                if (updatePass != null) find.setPassword(updatePass);

                find.write();
                build.append("Successfully updated Employee ").append(findID);
                break;
            case "list":
                build.append("Employees:\n");
                Collection<Employee> employees = registry.values();
                for (Employee e : employees)
                {
                    build.append(e.Id()).append(": ").append(e.FullName()).append(" -- ").append(e.supervisorId());
                    build.append('\n');
                    if (e instanceof SalaryEmployee)
                    {
                        build.append("Salary: ").append(((SalaryEmployee) e).salary()).append('\n');
                    }
                    if (e instanceof HourlyEmployee)
                    {
                        build.append("Hourly Rate: ").append(((HourlyEmployee) e).hourlyRate()).append('\n');
                    }
                    build.append("----------------------------\n");
                }
                break;

            case "viewtasks":
                ArrayList<Task> list = tasks.get(loggedIn.Id());
                int i = 1;
                for(Task t: list) {
                    build.append("\n"+ i + ": " + t.getDesc());
                    i++;
                }
                break;

            case "addtask":
                if (command.getOptions().size() < 1) {
                    build.append("addTask requires a task description");
                }
                else {
                	String description = "";
                	ArrayList<String> ops = command.getOptions();
                	for(String word : ops) {
                		description += word + " ";
                	}
                	
                    if(addTask(loggedIn.Id(), description)) {
                        build.append("Task succesfully added");
                    }
                    else {
                        build.append("No task was added");
                    }
                }
                break;

            case "completetask":
                if (command.getOptions().size() != 1) {
                    build.append("addTask requires 1 argument, the task number given when viewing tasks");
                }
                else {
                    int num = Integer.parseInt(command.getOptions().get(0));
                    ArrayList<Task> list3 = tasks.get(loggedIn.Id());
                    if(deleteTask(list3.get(num - 1))) {
                    	build.append("Task successfully completed");
                    }
                    else {
                    	build.append("Task could not be marked complete");
                    }
                }
                break;

            default:
                build.append("Employee cmd Error: No binding found for '").append(command.getCommand()).append("'");
        }
        return build.toString();
    }
}
