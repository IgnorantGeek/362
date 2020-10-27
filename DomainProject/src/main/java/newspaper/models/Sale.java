package newspaper.models;

/**
 * A simple class that basically returns true no matter what method is called.
 * @author Alexander Irlbeck
 *
 */
public class Sale 
{
	private String transId;
	private Customer customer;

	public Sale(String transId, Customer customer)
	{
		this.transId = transId;
		this.customer = customer;
	}

	public Sale()
	{
		transId = new String();
		customer = null;
	}

	public String Id()
	{
		return this.transId;
	}

	public Customer Customer()
	{
		return this.customer;
	}

	public boolean cardSale(String cardNum, String cvv, String expiration, String Name_on_card)
	{
		return true;
	}
	public boolean payPal()
	{
		return true;
	}
}
