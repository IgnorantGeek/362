package newspaper;


public abstract class Customer 
{
    private String custID;

    public Customer(String ID)
    {
        custID = ID;
    }

    public String custID() { return custID; }
}
