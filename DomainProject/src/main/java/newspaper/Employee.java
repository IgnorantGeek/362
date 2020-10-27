package newspaper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Employee implements Writeable
{
    private int Id;
    private String FullName;
    private static String DB_PATH = "../Database/Employees/";

    public Employee() { }

    public Employee(int Id, String Name)
    {
        this.Id = Id;
        this.FullName = Name;
    }

    public String FullName()
    {
        return FullName;
    }

    public int Id()
    {
        return Id;
    }

    // TODO
    @Override
    public int write()
    {
        // Write the employe to the db file
        String fileName = this.Id() + ".txt";
        String build = "";

        try
        {
            BufferedWriter writer = new BufferedWriter(new FileWriter(DB_PATH + fileName));

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
        File empFile = new File(DB_PATH + this.Id() + ".txt");

        if (!empFile.exists()) return -1;

        if (!empFile.delete()) return -2;

        return 0;
    }
}