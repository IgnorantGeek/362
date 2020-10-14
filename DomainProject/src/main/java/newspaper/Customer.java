package newspaper;


public abstract class Customer 
{
    private String id;

    public Customer(String id)
    {
        this.id = id;
    }

    public String id() { return id; }
}