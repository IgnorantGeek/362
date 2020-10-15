package newspaper;

/**
 * Represents a way to collect payment from subscribers via credit card
 * Doesn't actually charge anything for this project. Just stores relevent information  
 * @author Jonah Armstrong
 */
public class CreditCard extends PaymentInformation{
	private String name;
	private String number;
	private String expDate;
	private String code;
	
	public CreditCard(String name, String number,String expDate, String code) {
		this.number = number;
		this.code = code;
		this.expDate = expDate;
		this.name = name;
		this.setType("CreditCard");
	}
	/**
	 * Validates that the credit card works
	 * Unlikely to be implemented, just returns true
	 */
	@Override
	public boolean validate() {
		return true;
	}
	/**
	 * Charges an amount to the credit card
	 * Unlikely to be implemented, just returns true
	 * @param dollars amount to be charged
	 */
	@Override
	public boolean charge(double dollars) {
		return true;
	}
	@Override
	public String toString() {
		String result = "";
		result += this.getType();
		result += ", " + name;
		result += ", " + number;
		result += ", " + expDate;
		result += ", " + code;
		return result;
	}
}


