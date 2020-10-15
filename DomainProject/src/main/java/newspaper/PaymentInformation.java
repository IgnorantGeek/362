package main.java.newspaper;

public abstract class PaymentInformation {
	
	private String type;
	
	public abstract boolean validate();
	public abstract boolean charge(double dollars);
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
