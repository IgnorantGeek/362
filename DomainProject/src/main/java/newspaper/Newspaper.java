
package main.java.newspaper;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
/**
 * A class that represents a physical newspaper.
 * @author Alexander Irlbeck
 * **Works as of 10/14/20
 */
public class Newspaper {
	/**
	 * Contains the volume(index 0), issue(index 1), day(index 2), month(index 3), year(index 4)
	 */
	private int[] info;
	/**
	 * An array for all the pages in the newspaper.
	 */
	public ArrayList <String> pages;
	/**
	 * Whether or not the newspaper was published.
	 */
	private boolean finalized;
	/**
	 * Constructor that creates a new newspaper object with no pages.
	 * @param volume - volume of the newspaper
	 * @param issue - issue of the newspaper
	 * @param day - publishing date of the newspaper
	 * @param month - publishing month of the newspaper
	 * @param year - publishing year of the newspaper
	 * @param finalized - whether or not the paper has been published.
	 * **Works as of 10/14/20
	 */
	public Newspaper(int volume, int issue, int day, int month, int year, boolean finalized)
	{
		info=new int[5];
		info[0]=volume;
		info[1]=issue;
		info[2]=day;
		info[3]=month;
		info[4]=year;
		this.finalized=finalized;
		pages = new ArrayList<String>();
	}
	/**
	 * Returns if the paper was published.
	 * @return Returns if the paper was published or not.
	 * **Works as of 10/14/20
	 */
	public boolean getPublished()
	{
		return finalized;
	}
	/**
	 * Allows an actor to finalize a paper if they are cleared to do so. First lets the actor view it, then it prompts for a final yes/no on publishing it.
	 * @param clearance The clearance of the actor
	 * @return returns if the method successfully finalized the paper or not.
	 * **Works as of 10/14/20
	 */
	@SuppressWarnings("resource")//System.in should not be closed before the program has finished.
	public boolean finalizePaper(int clearance)
	{
		if(finalized)
		{
			System.out.println("Paper has already been finalized.");
			return false;
		}
		if (clearance>4)
		{
			boolean ended = false;
			while(!ended)
			{
				printPage();
				System.out.println("Finalize the paper? (Yes/No)");
				Scanner scan = new Scanner(System.in);
				String answer = scan.next();
				answer=answer.toLowerCase();
				if(answer.compareTo("yes")==0)
				{
					ended=true;
					finalized=true;
					System.out.println("Paper has been finalized.");
					return true;
				}
				else if(answer.compareTo("no")==0)
				{
					ended=true;
					System.out.println("Paper has not been finalized.");
					return false;
				}
				else
				{
					System.out.print("Response was not yes or no. Please respond with yes or no.");
				}
			}
		}
		System.out.println("You are not authorized to finalize this paper. Please speak to your Manager on why this is.");
		return false;
	}
	/**
	 * Allows an actor to read a paper if they are cleared to do so.
	 * @param clearance The clearance of the actor.
	 * @return returns if the method successfully read out the paper or not.
	 * **Works as of 10/14/20
	 */
	public boolean readPaper(int clearance)
	{
		if(!finalized)
		{
			if(clearance>3)
			{
				printPage();
			}
			else
			{
				System.out.println("The paper has not been published yet, so you can not access it at this time.");
				return false;
			}
		}
		else
		{
			if (clearance >1)
			{
				printPage();
			}
			else
			{
				System.out.println("You need a subscription to view newspapers online.");
				return false;
			}
		}
		return false;
	}
	/**
	 * Allows the consumer to order this paper individually. Takes their payment info, runs it through the Sales class, takes their name and address, and sends it to Newspaper_Orders.txt
	 * to be handled by the distributors.
	 * @return Returns whether or not the operation succeeded successfully or not.
	 * **Works as of 10/14/20
	 */
	public boolean orderPaper()
	{
		System.out.println("Would you like to input a Credit Card('CC'), enter PayPal('p'), or cancel('c') to complete your order?");
		@SuppressWarnings("resource")//System.in should not be closed before the program has finished.
		Scanner scan = new Scanner(System.in);
		String in=scan.nextLine();
		Sale sale=new Sale();
		if(in.compareTo("CC")==0)
		{
			System.out.println("Credit Card selected. Please input your full name, credit card number, CVV, and expiration date all seperated by hitting enter.");
			String name = scan.nextLine();
			String cardNum=scan.nextLine();
			String cvv = scan.nextLine();
			String exp = scan.nextLine();
			boolean tried = sale.cardSale(cardNum, cvv, exp, name);
			if(tried)
			{
				System.out.println("Payment successful.");
				System.out.println("Please enter the shipping address.");
				String destination = scan.nextLine();
				System.out.println("Destination confirmed for "+destination);
				System.out.println("If any problems occur, contact us at newspaperSales@gmail.com");
				try {
					Writer edit = new BufferedWriter(new FileWriter("../Database/NewspaperPages/Newspaper_Orders.txt", true));
					edit.append("Order for "+name+" is to be delivered at " + destination);
					edit.close();
				} catch (IOException e) {
					System.out.println("Newspaper_Orders.txt file not found. Please contact technical support.");
					return false;
				}
				return true;
			}
			else
			{
				System.out.println("Payment failed. Terminating payment.");
				return false;
			}
		}
		else if(in.compareTo("p")==0)
		{
			System.out.println("PayPal selected. Opening now:");
			if(sale.payPal())
			{
				System.out.println("Payment successful.");
				System.out.println("Please enter your name.");
				String name = scan.nextLine();
				System.out.println("Please enter the shipping address.");
				String destination = scan.nextLine();
				System.out.println("Destination confirmed for "+destination);
				System.out.println("If any problems occur, contact us at newspaperSales@gmail.com");
				try 
				{
					Writer edit = new BufferedWriter(new FileWriter("../Database/NewspaperPages/Newspaper_Orders.txt", true));
					edit.append("Order for "+name+" is to be delivered at " + destination);
					edit.close();
				}
				catch(IOException e)
				{
					System.out.println("Newspaper_Orders.txt file not found. Please contact technical support.");
					return false;
				}
				return true;
			}
			else
			{
				System.out.println("Payment failed. Terminating payment.");
				return false;
			}
		}
		else if(in.compareTo("c")==0)
		{
			System.out.println("Payment successfully cancelled.");
		}
		else
		{
			System.out.println("No approved option was inserted. Automatically cancelling order.");
		}
		return false;
	}
	/**
	 * Developer method that sets the newspaper to be published, in a method that a user can not see.
	 * **Works as of 10/14/20
	 */
	protected void setFinal()
	{
		finalized=true;
	}
	/**
	 * A helper method for read paper that shows the page in the newspaper as a pop-up.
	 * @return returns whether or not the program successfully displayed a picture.
	 * **Works as of 10/14/20
	 */
	private boolean printPage()
	{
		if(pages.size()>0)
		{
			System.out.println("Please input the page you want to visit between 1 and "+pages.size()+" or");
			System.out.println("'Quit', 'quit', 'q' or 'Q' if you want to stop looking at this newspaper's");
			System.out.println("pages.");
			@SuppressWarnings("resource")//System.in should not be closed before the program has finished.
			Scanner scan = new Scanner(System.in);
			String input=scan.next();
			while(!(0==input.compareTo("Quit"))&&!(0==input.compareTo("quit"))&&!(0==input.compareTo("q"))&&!(0==input.compareTo("Q")))
			{
				if(!(0==input.compareTo("Quit"))&&!(0==input.compareTo("quit"))||!(0==input.compareTo("q"))||!(0==input.compareTo("Q")))
				{
					int in;
					try
					{
						in = Integer.parseInt(input);
						if(in>0 && in<=pages.size())
						{
							JFrame f = new JFrame();
							String address = pages.get(in-1);
							ImageIcon image = new ImageIcon(address);
							JLabel l = new JLabel(image);
							f.add(l);
							f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
							f.pack();
							f.setVisible(true);
						}
						else
						{
							System.out.println(in+" is not between 1 and " + pages.size()+". Please input a page number between 1 and "+pages.size()+".");
						}
					}
					catch(Exception NumberFormatException)
					{
						System.out.println(input+" is not a valid number or a recognized quit keyword.");
						System.out.println("Please input the page you want to visit between 1 and "+pages.size()+"or");
						System.out.println("'Quit', 'quit', 'q' or 'Q' if you want to stop looking at this newspaper's");
						System.out.println("pages.");
					}
				}
				input=scan.next();
			}
			return true;
		}
		else
		{
			System.out.println("This newspaper does not have any pages in it. Please contact us to see why this is.");
			return false;
		}
	}
	/**
	 * Returns the info in the newspaper for use elsewhere. For the developer only.
	 * @return Returns the info in the newspaper for use elsewhere.
	 * **Works as of 10/14/20
	 */
	public int[] getInfo()
	{
		return info;
	}
}
