package newspaper;


public abstract class Customer 
{
    private int id;

    public Customer(int id)
    {
        this.id = id;
    }

    public int id() { return id; }
}