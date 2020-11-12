package newspaper.managers;

import newspaper.models.Employee;
import newspaper.ui.Command;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Random;

/**
 * Class that represents the feedback for the company.
 * @author Alexander Irlbeck
 * Works as of 11/1/20
 */
public class Feedback implements Commandable
{
	/**
	 * The email to rating.
	 */
	private HashMap<String, Integer> emailToRating;
	/**
	 * The email to the message
	 */
	private HashMap<String, String> emailToMessage;
	
	/**
	 * Constructor that initializes everything.
	 * Works as of 11/1/20
	 */
	public Feedback()
	{
		emailToRating = new HashMap<String, Integer>();
		emailToMessage = new HashMap<String, String>();
		init();
	}
	
	/**
	 * If the clearance is high enough, allows the user to delete an inappropriately worded feedback item.
	 * @param clearance the user's clearance
	 * @return whether or not the function succeeded.
	 * Works as of 11/1/20
	 */
	public boolean removeFeedback(int clearance)
	{
		if (clearance > 4)
		{
			if(emailToRating.size()<0&&emailToMessage.size()<0)
			{
				System.out.println("No feedback has been given recently.");
				return true;
			}
			@SuppressWarnings("resource")
			Scanner scan = new Scanner(System.in);
			boolean going = true;
			while(going)
			{
				String[] keys = new String[emailToRating.size()];
				for (int i=0; i<emailToRating.size();i++)
				{
					keys[i] = (String) emailToRating.keySet().toArray()[i];
				}
				System.out.println("Please pick the nubmer next to the review from the following:");
				for (int i=0; i<emailToRating.keySet().size(); i++)
				{
					System.out.println(i+": "+keys[i]+" "+emailToRating.get(keys[i])+" "+emailToMessage.get(keys[i]));
				}
				System.out.println("...Or enter 'q' to quit.");
				String input = scan.nextLine();
				if(input.compareToIgnoreCase("q")==0)
				{
					going = false;
				}
				else
				{
					int in = 0;
					try
					{
						in = Integer.parseInt(input);
						going = false;
					}
					catch(Exception NumberFormatException)
					{
						System.out.println(in+" could not be interpretted as a number.");
						continue;
					}
					if(in>keys.length||in<0)
					{
						System.out.println(in+" Number must be between 0 and "+(keys.length-1));
						continue;
					}
					String toUse = keys[in];
					System.out.println(toUse+"'s review was selected. Would you like to delete it? (y/n)");
					String delete = scan.nextLine();
					if(delete.compareToIgnoreCase("y")==0)
					{
						emailToRating.remove(toUse);
						emailToMessage.remove(toUse);
					}
				}
			}
			String builder="";
			for(int i=0;i<emailToRating.keySet().size();i++)
			{
				String cur = (String) emailToRating.keySet().toArray()[i];
				builder = builder+cur+"\n"+emailToRating.get(cur)+"\n"+emailToMessage.get(cur)+"\n";
			}
			BufferedWriter write;
			try {
				write = new BufferedWriter (new FileWriter("../Database/FeedbackInit.txt"));
				write.write(builder);
				write.close();
			} catch (IOException e) {
				System.out.println("Feedback initialization file corrupted or missing. Please contact tech support.");
				return false;
			}
			return true;
		}
		else
		{
			System.out.println("You do not have access to this function.");
			return false;
		}
	}

	public String removeFeedback(Employee loggedIn, int feedbackID)
	{
		StringBuilder build = new StringBuilder();

		return build.toString();
	}
	
	/**
	 * Allows the user to input feedback
	 * @return whether or not the function was successful.
	 * Works as of 11/1/20
	 */
	public boolean giveFeedback()
	{
		@SuppressWarnings("resource")
		Scanner scan = new Scanner(System.in);
		System.out.println("Thank you for wanting to give us your feedback. Do you want to give us an email with your feedback? (y/n)");
		String yesOrNo = scan.nextLine();
		String email;
		int rating = 0;
		String message = "No comment";
		if(yesOrNo.compareToIgnoreCase("y")==0)
		{
			System.out.println("Please input your email.");
			email=scan.nextLine();
		}
		else
		{
			Random rand = new Random();
			email = "Anonymous User #"+Math.abs(rand.nextInt());
		}
		boolean going = true;
		System.out.println("Please score us on a scale of 0 to 10.");
		while(going)
		{
			String in = scan.nextLine();
			try
			{
				rating = Integer.parseInt(in);
				going = false;
			}
			catch(Exception NumberFormatException)
			{
				System.out.println(in+" could not be interpretted as a number. Please enter a number between 0 and 10.");
			}
		}
		if(rating>10)
		{
			System.out.println("We are glad that you feel so good about our company!");
			rating = 10;
			System.out.println("Would you like to tell us anything else? (y/n)");
		}
		else if(rating<0)
		{
			System.out.println("We are sorry that we did not meet your expectations.");
			rating = 0;
			System.out.println("Would you like to tell us where we went wrong? (y/n)");
		}
		else
		{
			System.out.println("Would you like to give us any suggestions you would like to give us? (y/n)");
		}
		String input = scan.nextLine();
		if(input.compareToIgnoreCase("y")==0)
		{
			System.out.println("Please input your review on one line:");
			message = scan.nextLine();
		}
		BufferedWriter write;
		String builder = email + "\n" + rating + "\n" + message + "\n";
		try {
			write = new BufferedWriter (new FileWriter("../Database/FeedbackInit.txt",true));
			write.append(builder);
			write.close();
		} catch (IOException e) {
			System.out.println("Review could not be processed. Please contact us @ cservice@newspaper.org.");
			return false;
		}
		emailToRating.put(email,rating);
		emailToMessage.put(email, message);
		System.out.println("Review was successfully processed. Thank you!");
		return true;
	}
	
	/**
	 * Displays all the feedback and average of ratings.
	 * @return whether or not the function succeeded.
	 * Works as of 11/1/20
	 */
	public boolean displayFeedback()
	{
		float averageRating = 0;
		if(emailToRating.size()==0) 
		{
			System.out.println("No reviews yet. please come back later."); 
			return false;
		}
		String[] keys = new String[emailToRating.size()];
		for (int i=0; i<emailToRating.size();i++)
		{
			keys[i] = (String) emailToRating.keySet().toArray()[i];
		}
		for(int i = 0; i<keys.length;i++)
		{
			averageRating+=emailToRating.get(keys[i]);
		}
		if(averageRating!=0)
		{
			averageRating = (averageRating/keys.length);
		}
		System.out.println(String.format("The average rating for us is %.2f",averageRating));
		System.out.println("");
		System.out.println("Reviews:");
		for(int i = 0; i<keys.length; i++)
		{
			System.out.println(keys[i]);
			System.out.println(emailToMessage.get(keys[i]));
			System.out.println("");
		}
		return true;
	}
	
	/**
	 * Initializes the object from the Feedback init file.
	 * @return whether or not the function succeeded
	 * Works as of 11/1/20
	 */
	private boolean init()
	{
		File f = new File("../Database/FeedbackInit.txt");
		Scanner scan;
		try {
			scan = new Scanner(f);
		} catch (FileNotFoundException e) {
			System.out.println("Feedback initialization file corrupted or missing. Please contact tech support.");
			return false;
		}
		ArrayList<String> lines = new ArrayList<String>();
		while(scan.hasNextLine())
		{
			String line = scan.nextLine();
			lines.add(line);
		}
		while(lines.size()>0)
		{
			String email = lines.remove(0);
			int rating;
			String message;
			if(lines.size()>0)
			{
				String temp = lines.remove(0);
				try
				{
					rating = Integer.parseInt(temp);
				}
				catch(Exception NumberFormatException)
				{
					System.out.println("Feedback initialization file corrupted or missing. Please contact tech support.");
					scan.close();
					return false;
				}
				if(lines.size()>0)
				{
					message = lines.remove(0);
					if(emailToRating.containsKey(email))
					{
						emailToRating.replace(email, rating);
					}
					else
					{
						emailToRating.put(email, rating);
					}
					if(emailToMessage.containsKey(email))
					{
						emailToMessage.replace(email, message);
					}
					else
					{
						emailToMessage.put(email, message);
					}
				}
				else
				{
					System.out.println("Feedback initialization file corrupted or missing. Please contact tech support.");
					scan.close();
					return false;
				}
			}
			else
			{
				System.out.println("Feedback initialization file corrupted. Please contact tech support.");
				scan.close();
				return false;
			}
		}
		scan.close();
		return true;
	}

	@Override
	public String executeCommand(Employee loggedIn, Command command)
	{
		// TODO
		return null;
	}
}
