package newspaper.models;


/**
 * Represents an  individual subscriber.
 * @author Jonah Armstrong
 */

public class Subscription {
	public String Firstname;
	public String Lastname;
	public String address;
	public String email;
	private PaymentInformation info;
	
	/**
	 * 
	 * @param Firstname The first name of subscriber
	 * @param Lastname The last name of subscriber
	 * @param address The address of subscriber
	 * @param email The email address of subscriber
	 */
	public Subscription(String Firstname, String Lastname, String address, String email) {
		this.Firstname = Firstname;
		this.Lastname = Lastname;
		this.address = address;
		this.email = email;
	} 
	/**
	 * adds a payment information to the subscriber
	 * @param p Payment information to be added
	 */
	public void addPaymentInformation(PaymentInformation p) {
		info = p;
	}
	
	/**
	 * charges the given amount to the payment type
	 * not likely to be implemented in this project, always returns true
	 * @param dollars amount to be charged
	 */
	public boolean charge(double dollars) {
		return info.charge(dollars);		
	}
	
	@Override
	public String toString() {
		String result = "";
		result += Firstname;
		result += ", " + Lastname;
		result += ", " + address;
		result += ", " + email;
		result += ", " + info.toString();
		return result;
	}
}
