package newspaper;

public class Employee implements Writeable
{
    private int Id;
    private String FullName;

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
        return 0;
    }
}