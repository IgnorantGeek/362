package newspaper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Scanner;

public class SubscriptionManager {
	
	private HashMap<String,Subscription> Subscriptions;
	private String directory;
	
	public SubscriptionManager() {
		Subscriptions = new HashMap<String,Subscription>();
		directory = "../Database/Subscribers/Subscribers.txt";
		File Archive = new File(directory);
		try {
			Scanner s = new Scanner(Archive);
			while(s.hasNextLine()) {
				String[] subInfo = s.nextLine().split(",");
				String firstName = subInfo[0].trim();
				String lastName = subInfo[1].trim();
				String address = subInfo[2].trim();
				String email = subInfo[3].trim();
				String piType = subInfo[4].trim();
				Subscription sub = new Subscription(firstName, lastName,address,email);
				PaymentInformation info = null;
				if(piType.equals("CreditCard")) {
					String piName = subInfo[8].trim();
					String piNumber = subInfo[5].trim();
					String piExpDate = subInfo[7].trim();
					String piCode = subInfo[6].trim();
					info = new CreditCard(piName, piNumber, piExpDate, piCode);
				}
				sub.addPaymentInformation(info);
				Subscriptions.put(email, sub);
			}
		} catch (FileNotFoundException e) {
			System.out.println("Subscribtion file is inaccessible.");
		} 		
	}
	
	public boolean addSub(Subscription s) {
		if(s != null && getSub(s.email) == null) {
			Subscriptions.put(s.email, s);
			return writeSub(s);

		}
		return false;
	}
	
	private boolean writeSub(Subscription s) {
		PrintWriter out = null;
		try {
		    out = new PrintWriter(new BufferedWriter(new FileWriter(directory, true)));
		    out.print("\n" + s.toString());
		} catch (IOException e) {
		    System.out.println("Subscription File system corrupted. Seek IT help.");
		    return false;
		} finally {
		    if (out != null) {
		        out.close();
		    }
		} 
		return true;
	}

	public Collection<Subscription> getAllSubs() {
		return Subscriptions.values();
	}
	
	public boolean removeSub(String email) {
		if(getSub(email) != null) {
			Subscriptions.remove(email);
			return true;
		}
		return false;
	}

	public boolean validateEmail(String email) {
		return true;
	}

	public Subscription getSub(String email) {
		if(email == null) {
			return null;
		}
		return Subscriptions.get(email);
	}

	public boolean validateCcn(String ccn) {
		if (ccn.length() != 16)
			return false;
		for (int i = 0; i < ccn.length(); i++) {
			if (ccn.charAt(i) == ' ')
				return false;
		}
		return true;
	}

	public boolean validateDate(String date) {
		if(date.length() != 5) 
			return false;
		if(!Character.isDigit(date.charAt(0)))
			return false;
		if(!Character.isDigit(date.charAt(1)))
			return false;
		if(date.charAt(2) != '/')
			return false;
		if(!Character.isDigit(date.charAt(3)))
			return false;
		if(!Character.isDigit(date.charAt(4)))
			return false;	
		return true;
	}

	public boolean addSubscription() {
		Scanner in = new Scanner(System.in);
		System.out.println("Enter first name.");
		String firstName = in.nextLine();
		if(firstName.equals("quit")) {
			return false;
		}
		System.out.println("Enter last name.");
		String lastName = in.nextLine();
		if(lastName.equals("quit")) {
			return false;
		}
		System.out.println("Enter address.");
		String address = in.nextLine();
		if(address.equals("quit")) {
			return false;
		}
		boolean valid = false;
		String email = "";
		while(!valid) {
			System.out.println("Enter email address");
			email = in.nextLine();
			if(email.equals("quit")) {
				return false;
			}
			valid = validateEmail(email);
			if(!valid) {
				System.out.println("Not a valid email address. Format should be as follows: 'address@example.dom'. Enter quit to cancel.");
			}
			
			else if(getSub(email) != null) {
				valid = false;
				System.out.println("Email address already in use. Enter a new address or enter quit to cancel.");
			}
			
		}
		
		System.out.println("Currently only credit cards are accepted.");
		String cardNum = null;
		String expDate = null;
		String code = null;
		String name = null;
		PaymentInformation payment = null;
		valid = false;
		boolean ccnValid = false;
		while(!ccnValid) {
			System.out.println("Enter the name on the card.");
			name = in.nextLine();
			while(!valid) {
				System.out.println("Enter credit card number");
				cardNum = in.nextLine();
				if(cardNum.equals("quit")) {
					return false;
				}
				valid = validateCcn(cardNum);
				if(!valid) {
					System.out.println("Not a valid card number. Credit card numbers should be 16 digits long and have no spaces");
				}
			}
		
			valid = false;
			while(!valid) {
				System.out.println("Enter expiration date");
				expDate = in.nextLine();
				if(expDate.equals("quit")) {
					return false;
				}
				valid = validateDate(expDate);
				if(!valid) {
					System.out.println("Not a valid date. Date should be formatted as follows: 'MO/YE'");
				}
			}
			
			valid = false;
			while(!valid) {
				System.out.println("Enter security code");
				code = in.nextLine();
				if(code.equals("quit")) {
					return false;
				}
				valid = code.length() == 3;
				if(!valid) {
					System.out.println("Not a valid security code. Security codes must be exactly 3 digits.");
				}
			}
			payment = new CreditCard(name, cardNum, expDate, code);
			ccnValid = payment.validate();
			if(!ccnValid) {
				System.out.println("Credit card failed to validate. Try another card or type 'quit' to cancel.");
			}
		}
		Subscription sub = new Subscription(firstName, lastName, address, email);
		sub.addPaymentInformation(payment);
		return addSub(sub);
	}

	public boolean removeSubscription() {
		Scanner in = new Scanner(System.in);
		System.out.println("Enter email for subscription to be removed.");
		String email = in.nextLine();
		while(!email.equals("quit")) {
			if(removeSub(email)) {
				return true;
			}
			else {
				System.out.println("There is no subscription associated with that email. Enter a new email address or enter quit to cancel.");
			}
		}
		return false;
	}

