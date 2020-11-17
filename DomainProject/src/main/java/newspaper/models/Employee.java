package newspaper.models;

import newspaper.Global;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public abstract class Employee implements Writeable
{
    private final int Id;
    private final int clearance;
    private int supervisorId;
    private String FullName;
    private String Password;

    public Employee(int Id, String FullName, String password, int supervisorId, int clearance)
    {
        this.Id = Id;
        this.Password = password;
        this.supervisorId = supervisorId;
        this.FullName = FullName;
        this.clearance = clearance;
    }

    public String FullName()
    {
        return FullName;
    }

    public int Id()
    {
        return Id;
    }

    public int supervisorId()
    {
        return supervisorId;
    }

    public String Password()
    {
        return Password;
    }

    public int Clearance() { return clearance; }

    public void setFullName(String fullName)
    {
        FullName = fullName;
    }

    public void setPassword(String password)
    {
        Password = password;
    }

    public void setSupervisorId(int supervisorId)
    {
        this.supervisorId = supervisorId;
    }

    @Override
    public int write()
    {
        // Write the employee to the db file
        String fileName = this.Id() + ".txt";
        String build = "";
        build += FullName + "\n" + this.supervisorId() + "\n" + Password + "\n" + clearance;

        try
        {
            BufferedWriter writer = new BufferedWriter(new FileWriter(Global.EMPLOYEE_DB_PATH + fileName));

            writer.write(build);

            writer.close();
        }
        catch (IOException e)
        {
            System.out.println("ERROR WRITING EMPLOYEE TO DB: " + e.getMessage());
            return -1;
        }
        return 0;
    }

    @Override
    public int delete()
    {
        File empFile = new File(Global.EMPLOYEE_DB_PATH + this.Id() + ".txt");

        if (!empFile.exists()) return -1;

        if (!empFile.delete()) return -2;

        return 0;
    }
    
    public abstract double getPaycheckValue();
}