package newspaper;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

public class Main {
	public static class main {
		public static final String softwareName = "Untitled newspaper management software";
		static SubscriptionManager subManager = new SubscriptionManager();
		static ArticleManager aman = new ArticleManager();
		
		public static void main(String args[]) {
			
			welcome();
			Scanner in = new Scanner(System.in);
			String command = "";
			
			while(!command.equals("quit")) {
				command = in.nextLine();
				if(command.equals("help")) {
					System.out.println("Type 'quit' to quit software.");
					System.out.println("Type 'addSubscription' to add a new subscriber.");
					System.out.println("Type 'removeSubscription' to remove a subscriber.");
					System.out.println("Type 'printSubscriptions' to see a list of all current subscribers.");
					System.out.println("Type 'createArticle' to add a new article.");
					System.out.println("Type 'editArticle' to add a new article.");
					System.out.println("Type 'printArticles' to print the names of all unfinished articles.");
				}
				
				else if(command.equals("removeSubscription")) {
					if(removeSubscription()) {
						System.out.println("Subscription successfully removed");
					}
					else {
						System.out.println("No subscription was removed");
					}
				}
				
				else if(command.equals("printArticles")) {
				}
				else if(command.equals("addSubscription")) {
					if (addSubscription()) {
						System.out.println("Subscription successfully added");
					}
					else {
						System.out.println("No subscription was added");
					}
				}
				
				else if(command.equals("printSubscriptions")) {
					Collection<Subscription> subs = subManager.getAllSubs();
					if(subs.size() > 0) {
						for(Subscription sub: subs) {
							System.out.println(sub.toString());
						}
					}
					else
						System.out.println("There are no currently active subscriptions.");
				}
				
				else if(command.equals("createArticle")) {
					System.out.println("Feature not yet implemented.");
				}
				
				else if(command.equals("editArticle")) {
					System.out.println("Enter name of the article to edit.");
					String name = in.nextLine();
					Article art = aman.getArticle(name);
					if(art == null) {
						System.out.println("There is no article with that name");
					}
					else {
						try {
							Desktop.getDesktop().edit(new File(art.path()));
						} catch (IOException e) {
							System.out.println("File could not be opened.");
						}
					}
				}
				
				else if(command.equals("")) {}
				
				else if(!command.equals("quit")) {
					System.out.println("Command not recognized. Type help for a list of commands.");
				}
				
			}
			
			in.close();
			System.out.println(softwareName + " is now exiting.");

		}
		
		private static boolean removeSubscription() {
			Scanner in = new Scanner(System.in);
			System.out.println("Enter email for subscription to be removed.");
			String email = in.nextLine();
			while(!email.equals("quit")) {
				if(subManager.removeSub(email)) {
					return true;
				}
				else {
					System.out.println("There is no subscription associated with that email. Enter a new email address or enter quit to cancel.");
				}
			}
			return false;
		}

		private static boolean addSubscription() {
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
				valid = subManager.validateEmail(email);
				if(!valid) {
					System.out.println("Not a valid email address. Format should be as follows: 'address@example.dom'. Enter quit to cancel.");
				}
				
				else if(subManager.getSub(email) != null) {
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
					valid = subManager.validateCcn(cardNum);
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
					valid = subManager.validateDate(expDate);
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
			return subManager.addSub(sub);
		}

		public static void welcome() {
			System.out.println("");
			System.out.println("");
			System.out.println("");
			System.out.println("");
			System.out.println("");
			System.out.println("");
			System.out.println("");
			System.out.println("");
			System.out.println("");
			System.out.println(softwareName + " is now running. Type 'help' for a list of commands. Type 'quit' to quit.");
		}

	}

}
