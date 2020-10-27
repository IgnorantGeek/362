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
        this.init();
    }

    public EmployeeManager(int start)
    {
        this.idCounter = start;
        employeeCount = 0;
        registry = new HashMap<Integer, Employee>();
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
    public boolean addEmployee(String employeeName, String password, int supervisorId)
    {
        Employee in = new Employee(idCounter++, password, supervisorId, employeeName);
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
            System.out.println(employeeFileName);
            Employee employee = buildFromFile(employeeFileName);

            if (employee != null) 
            {
                registry.put(employee.Id(), employee);
                employeeCount++;
            }

            // Prevents us from overwriting employee files that failed to add
            idCounter++;
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
            String password = fileScanner.nextLine();

            String fname = "";
            for (int i = 0; i < Filename.length(); i++)
            {
                if (Filename.charAt(i) == '.') break;
                else fname += Filename.charAt(i);
            }
            int id = Integer.parseInt(fname);

            out = new Employee(id, password, supIdString, name);

            fileScanner.close();

            System.out.println("Employee added: " + id);
        }
        catch (Exception e)
        {
            System.out.println("ERROR: Failed to add employee file: " + Filename);
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

    public int counter()
    {
        return idCounter;
    }

    public Employee validateLogin(int user_id, String password)
    {
        if (registry == null) return null;
        else if (!registry.containsKey(user_id)) return null;
        else
        {
            Employee login = registry.get(user_id);
            if (login.Password().compareTo(password) == 0) return login;
            else return null;
        }
    }
}