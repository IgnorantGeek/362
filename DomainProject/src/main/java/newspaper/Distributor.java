package newspaper;

public class Distributor extends Customer
{
    private int paperCount; // Number of papers to recieve

    public Distributor(String id, int paperCount)
    {
        super(id);
        this.paperCount = paperCount;
    }

    public int paperCount()
    {
        return paperCount;
    }
}
