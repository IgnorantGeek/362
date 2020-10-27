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
        registry = new HashMap<Integer, Employee>();
        idCounter = 0;
        employeeCount = 0;
    }

    public EmployeeManager(int start)
    {
        this.idCounter = start;
        employeeCount = 0;
        registry = new HashMap<Integer, Employee>();
    }

    // Methods

    /**
     * Adds an employee to the system
     * @param employeeName Name of the new employee
     * @param supervisorId ID of the employee this user will report to
     * @return True on a successful add, false otherwise
     */
    public boolean addEmployee(String employeeName, int supervisorId)
    {
        Employee in = new Employee(idCounter++, supervisorId, employeeName);
        employeeCount++;

        registry.put(in.Id(), in);

        if (in.write() < 0) return false;
        return true;
    }

    /**
     * Drops an Employee from the Database
     * @param ID - Id of the employee to drop
     * @return 0 on success, Less than zero for error code
     */
    public int dropEmployee(int ID)
    {
        // Search hashmap for entry, if it exists remove it and drop the database reference
        Employee find = registry.get(ID);
        if (find == null) return -1;

        // Remove from registry and delete from db
        registry.remove(find.Id());
        if (find.delete() < 0) return -2;

        // Successful return
        employeeCount--;
        return 0;
    }

    /**
     * Initializes the Employees Database and builds any present
     * Employee files into memory
     * @return true on success, false otherwise
     */
    public boolean init()
    {
        // Check for presence of Employee DB entry, build if not present
        File rootDir = new File(Global.EMPLOYEE_DB_PATH);

        if (!rootDir.exists())
        {
            rootDir.mkdir();
            return true;
        }

        for (String employeeFileName : rootDir.list())
        {
            Employee employee = buildFromFile(employeeFileName);

            if (employee != null) this.registry.put(employee.Id(), employee);
        }

        return true;
    }

    /**
     * Builds an Employee class from a filename
     * @param Filename - Name of file (relative path)
     * @return New Employee class, null if build failed
     */
    public Employee buildFromFile(String Filename)
    {
        Employee out = null;
        File employeeFile = new File(Global.EMPLOYEE_DB_PATH + Filename);
        Scanner fileScanner;

        try 
        {
            fileScanner = new Scanner(employeeFile);

            String name = fileScanner.nextLine();
            int supIdString = Integer.parseInt(fileScanner.nextLine());

            String[] fname = Filename.split(".");
            int id = Integer.parseInt(fname[0]);

            out = new Employee(id, supIdString, name);

            fileScanner.close();
        }
        catch (Exception e)
        {
            System.out.println("ERROR: Failed to add employee file: " + Filename);
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

    public boolean validateLogin(int user_id, String password)
    {
        if (registry == null) return false;
        if (!registry.containsKey(user_id)) return false;
        if (registry.get(user_id).Password().compareTo(password) != 0) return false;

        return true;
    }
}
