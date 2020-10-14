package newspaper;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Newspaper {
	private int[] info;
	public ArrayList <String> pages;
	private boolean finalized;
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
	public boolean getPublished()
	{
		return finalized;
	}
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
	public boolean orderPaper() throws IOException
	{
		System.out.println("Would you like to input a Credit Card('CC'), enter PayPal('p'), or cancel('c') to complete your order?");
		@SuppressWarnings("resource")//System.in should not be closed before the program has finished.
		Scanner scan = new Scanner(System.in);
		String in=scan.next();
		Sales sale=new Sales();
		if(in.compareTo("CC")==0)
		{
			System.out.println("Credit Card selected. Please input your full name, credit card number, CVV, and expiration date all seperated by hitting enter.");
			String name = scan.nextLine();
			String cardNum=scan.next();
			int cvv = scan.nextInt();
			int exp = scan.nextInt();
			boolean tried = sale.cardSale(cardNum, cvv, exp, name);
			if(tried)
			{
				System.out.println("Payment successful.");
				System.out.println("Please enter the shipping address.");
				String destination = scan.nextLine();
				System.out.println("Destination confirmed for "+destination);
				System.out.println("If any problems occur, contact us at newspaperSales@gmail.com");
				Writer edit = new BufferedWriter(new FileWriter("../Database/NewspaperPages/Newspaper_Orders.txt", true));
				edit.append("Order for "+name+" is to be delivered at " + destination);
				edit.close();
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
				Writer edit = new BufferedWriter(new FileWriter("../Database/NewspaperPages/Newspaper_Orders.txt", true));
				edit.append("Order for "+name+" is to be delivered at " + destination);
				edit.close();
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
				input=scan.next();
				if(!(0==input.compareTo("Quit"))&&!(0==input.compareTo("quit"))||!(0==input.compareTo("q"))||!(0==input.compareTo("Q")))
				{
					int in;
					try
					{
						in = Integer.parseInt(input);
						if(in>0 && in<=pages.size())
						{
							String address = pages.get(in-1);
							JFrame f = new JFrame();
							ImageIcon image = new ImageIcon(address);
							JLabel l = new JLabel(image);
							f.add(l);
							f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
							f.pack();
							f.setVisible(true);
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
			}
			return true;
		}
		else
		{
			System.out.println("This newspaper does not have any pages in it. Please contact us to see why this is.");
			return false;
		}
	}
}
