package newspaper;

public class Subscription {
	public String Firstname;
	public String Lastname;
	public String address;
	public String email;
	private PaymentInformation info;
	
	public Subscription(String Firstname, String Lastname, String address, String email) {
		this.Firstname = Firstname;
		this.Lastname = Lastname;
		this.address = address;
		this.email = email;
	} 
	
	public void addPaymentInformation(PaymentInformation p) {
		info = p;
	}
	
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
