package newspaper;

/**
 * Represents a way to collect payment from subscribers  
 * @author Jonah Armstrong
 */
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
