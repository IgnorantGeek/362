package newspaper;

public class Advertiser extends Customer implements Writeable
{
    private String name;

    public Advertiser(String name, int id)
    {
        super(id);
        this.name = name;
    }

    public String Name() { return name; }

    @Override
    public int write()
    {
        // TODO
        return 0;
    }
}
