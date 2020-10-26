package newspaper;

import java.util.HashMap;

public class EmployeeManager
{
    private int idCounter;
    private int employeeCount;
    private HashMap<Integer, Employee> registry;

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

    public boolean addEmployee(String employeeName)
    {
        Employee in = new Employee(idCounter++, employeeName);
        employeeCount++;

        registry.put(in.Id(), in);

        if (in.write() < 0) return false;
        return true;
    }

    public boolean dropEmployee(int ID)
    {
        
        return true;
    }

    public int getEmployeeCount()
    {
        return employeeCount;
    }
}
