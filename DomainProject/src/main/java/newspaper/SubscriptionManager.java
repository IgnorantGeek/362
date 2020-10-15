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


/**
 * Manages methods for interacting with the subscriber file system.
 * @author Jonah Armstrong
 */
public class SubscriptionManager {
	/**
	 * Stores the Subscriptions in memory
	 */
	private HashMap<String,Subscription> Subscriptions;
	//path to the Subscriptions.txt file
	private String directory;
	/**
	 * Loads the subscriptions from file into memory and initializes vairiables
	 */
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
	/**
	 * Adds a subscription to file and memory
	 * @param s Subscription to be added
	 */
	public boolean addSub(Subscription s) {
		if(s != null && getSub(s.email) == null) {
			Subscriptions.put(s.email, s);
			return writeSub(s);

		}
		return false;
	}
	/**
	 * Adds a subscription to file
	 * @param s Subscription to be added
	 */
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
	
	/**
	 * Removes a Subscription from file and memory
	 * @param email email address of subscription to be removed
	 */
	public boolean removeSub(String email) {
		if(getSub(email) == null) {
			return false;
		}
		Subscriptions.remove(email);
		Collection<Subscription> subs = Subscriptions.values();
		PrintWriter out = null;
		try {
		    out = new PrintWriter(new BufferedWriter(new FileWriter(directory, false))); int i = 0;
		    for(Subscription sub : subs) {
		    	if(i > 0)
		    		out.print("\n");
		    	out.print(sub.toString());
		    	i++;
			}
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

	/**
	 * Returns true if email is formatted correctly
	 * @param email email address to be validated
	 * @return True if correct, False if not.
	 */
	public boolean validateEmail(String email) {
		int i = 0;
		while(i < email.length() && email.charAt(i) != '@') {
			i++;
		}
		while(i < email.length() && email.charAt(i) != '.') {
			i++;
		}
		if (i < email.length() - 1) {
			return true;
		}
		return false;
	}

	/**
	 * Returns a sub that matches the given email. Null if the email isn't in memory.
	 * @param email email address to be validated
	 * @return True if correct, False if not.
	 */
	public Subscription getSub(String email) {
		if(email == null) {
			return null;
		}
		return Subscriptions.get(email);
	}

	/**
	 * Validates that a credit card number is formatted correctly
	 * @param ccn Credit card number to be validated
	 * @return True if correct, False if not.
	 */
	public boolean validateCcn(String ccn) {
		if (ccn.length() != 16)
			return false;
		for (int i = 0; i < ccn.length(); i++) {
			if (ccn.charAt(i) == ' ')
				return false;
		}
		return true;
	}
	
	/**
	 * Validates that a date is formatted correctly
	 * @param date date to be validated
	 * @return True if correct, False if not.
	 */
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
	
	/**
	 * Gets input from the user in order to add a subscription to file and memory.
	 * @return true if add was successful
	 */
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
	/**
	 * Gets input from the user in order to remove a subscription from file and memory.
	 * @return true if add was successful
	 */

	public boolean removeSubscription() {
		Scanner in = new Scanner(System.in);
		String email = "";
		while(!email.equals("quit")) {
			System.out.println("Enter email for subscription to be removed.");
			email = in.nextLine();
			if(removeSub(email)) {
				return true;
			}
			else {
				System.out.println("There is no subscription associated with that email. Enter a new email address or enter quit to cancel.");
			}
		}
		return false;
	}
}
