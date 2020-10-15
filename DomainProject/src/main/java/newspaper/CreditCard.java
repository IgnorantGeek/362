package main.java.newspaper;

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
	
	@Override
	public boolean validate() {
		return true;
	}
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


