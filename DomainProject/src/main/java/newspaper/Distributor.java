package main.java.newspaper;

public class Distributor extends Customer implements Writeable
{
    private int paperCount; // Number of papers to recieve
    private String distName;
    private int id;

    public Distributor(int id, int paperCount, String name)
    {
        super(id);
        this.paperCount = paperCount;
        this.distName = name;
    }

    public int Id()
    {
        return id;
    }

    public int paperCount()
    {
        return paperCount;
    }

    public String nameString()
    {
        return distName;
    }

    @Override
    public int write()
    {
        // TODO
        return 0;
    }
}
