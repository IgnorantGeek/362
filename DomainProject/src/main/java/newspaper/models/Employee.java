package newspaper.models;

import newspaper.Global;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public abstract class Employee implements Writeable
{
    private int Id;
    private int supervisorId;
    private String FullName;
    private String Password;

    public Employee() { }

    public Employee(int Id, String FullName)
    {
        this.Id = Id;
        this.FullName = FullName;
    }

    public Employee(int Id, String password, int supervisorId, String FullName)
    {
        this.Id = Id;
        this.Password = password;
        this.supervisorId = supervisorId;
        this.FullName = FullName;
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

    @Override
    public int write()
    {
        // Write the employee to the db file
        String fileName = this.Id() + ".txt";
        String build = "";
        build += FullName + "\n" + this.supervisorId();

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