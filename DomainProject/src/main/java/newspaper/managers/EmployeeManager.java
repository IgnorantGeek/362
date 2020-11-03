package newspaper.managers;

import newspaper.models.Employee;
import newspaper.Global;
import java.io.File;
import java.util.HashMap;
import java.util.Scanner;

/**
 * EmployeeManager - Manages Employees and Login Verification
 * @author Nick Heisler
 */
public class EmployeeManager
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
     * Adds an employee to the system
     * @param employeeName Name of the new employee
     * @param password Password for the new employee
     * @param supervisorId ID of the employee this user will report to
     * @return True on a successful add, false otherwise
     */
    public int addEmployee(String employeeName, String password, int supervisorId)
    {
        Employee in = new Employee(idCounter++, password, supervisorId, employeeName);
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

    public boolean updateSupervisorId(Employee updateEmployee, int supervisorId)
    {
        
        return true;
    }

    public boolean updateFullName(Employee updateEmployee, String fullName)
    {

        return true;
    }

    public boolean updatePassword(Employee updatEmployee, String password)
    {

        return true;
    }



    /**
     * Checks whether some employee is authorized to make changes to some other employee
     * @param supervisor the supervisor to check
     * @param worker the worker whos supervisor we are checking
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
     * @return true on success, false otherwise
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

            String fname = "";
            for (int i = 0; i < fileName.length(); i++)
            {
                if (fileName.charAt(i) == '.') break;
                else fname += fileName.charAt(i);
            }
            int id = Integer.parseInt(fname);

            out = new Employee(id, password, supIdString, name);

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
}
