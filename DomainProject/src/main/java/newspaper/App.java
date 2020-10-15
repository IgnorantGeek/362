package newspaper;

import java.io.IOException;
import java.util.Scanner;

/**
 * Handle the IOException properly
 */
public class App
{
	public static void main( String[] args ) throws IOException
	{
		NewspaperManager man = new NewspaperManager();
		ArticleManager aman = new ArticleManager();
		AdManager adManager = new AdManager("../Database", man);

		// Intialize adManager 
		adManager.init();

		Scanner in = new Scanner(System.in);

		System.out.println("Welcome to the FakeNews Newspaper Management System. What would you like to do?");
		System.out.println("1: Edit/Publish a Newspaper\n2: Edit/Create an Article\n3: Enter a New Ad Sale\n4: Add/Remove a Distribution Subscription\nq: Quit");

		while (true)
		{
			String input = in.nextLine();

			switch(input)
			{
			case "1":
				boolean going=true;
				while(going)
				{
					System.out.println("NewspaperManager -- Enter 'q' to quit. Enter '1' to search for a newspaper and read it."
							+ "\nEnter '2' to add a newspaper. Enter '3' to search for a paper and then edit it. Enter '4' to"
							+ "\nsave your current progress. Enter '5' to search for a paper and publish it. Enter '6' to search and order"
							+ "\na newspaper.");
					input = in.nextLine();
					System.out.println("Enter your clearance level.");
					int clearance = 0;
					String next = in.nextLine();
					try
					{
						clearance = Integer.parseInt(next);
					}
					catch(Exception NumberFormatException)
					{
						System.out.println(next+" is not a valid number. Cancelling current attempt.");
						continue;
					}
					switch(input)
					{
					case "q":
						going = false;
						break;
					case "1":
						man.search().readPaper(clearance);
						break;
					case "2":
						man.addPaper(clearance);
						break;
					case "3":
						Newspaper temp = man.search();
						man.editPaper(temp, clearance);
						break;
					case "4":
						man.save();
						break;
					case "5":
						man.search().finalizePaper(clearance);
						break;
					case "6":
						man.search().orderPaper();
						break;
					}
				}
				break;
			case "2":
				boolean go=true;
				while(go)
				{
					System.out.println("ArticleManager -- Enter 'q' to quit. Enter '1' to search for an article, choose form a list of articles, and read one."
							+ "\nEnter '2' to add an article. Enter '3' to search for an article and then edit it. Enter '4' to search for an article and publish it.");
					input = in.nextLine();
					System.out.println("Enter your clearance level.");
					int clearance = 0;
					String next = in.nextLine();
					try
					{
						clearance = Integer.parseInt(next);
					}
					catch(Exception NumberFormatException)
					{
						System.out.println(next+" is not a valid number. Cancelling current attempt.");
						continue;
					}
					switch(input)
					{
					case "q":
						go = false;
						break;
					case "1":
						Article[] arr= aman.search();
						for(int i=0; i<arr.length;i++)
						{
							System.out.println(i+": "+arr[i].getName()+ ", "+arr[i].getDesc());
						}
						next=in.nextLine();
						int num=0;
						try
						{
							num = Integer.parseInt(next);
						}
						catch(Exception NumberFormatException)
						{
							System.out.println(next+" is not a valid number. Cancelling current attempt.");
							continue;
						}
						arr[num].readArticle(clearance);
						break;
					case "2":
						aman.addArticle();
						break;
					case "3":
						Article[] Arr= aman.search();
						for(int i=0; i<Arr.length;i++)
						{
							System.out.println(i+": "+Arr[i].getName()+ ", "+Arr[i].getDesc());
						}
						next=in.nextLine();
						int Num=0;
						try
						{
							Num = Integer.parseInt(next);
						}
						catch(Exception NumberFormatException)
						{
							System.out.println(next+" is not a valid number. Cancelling current attempt.");
							continue;
						}
						Arr[Num].editArticle(clearance);
						break;
					case "4":
						Article[] ARR= aman.search();
						for(int i=0; i<ARR.length;i++)
						{
							System.out.println(i+": "+ARR[i].getName()+ ", "+ARR[i].getDesc());
						}
						next=in.nextLine();
						int NUM=0;
						try
						{
							NUM = Integer.parseInt(next);
						}
						catch(Exception NumberFormatException)
						{
							System.out.println(next+" is not a valid number. Cancelling current attempt.");
							continue;
						}
						ARR[NUM].finalizeArticle(clearance);
						break;
					}
				}			break;
			case "3":
				boolean ad = true;
				while (ad)
				{
					System.out.println("AdManager -- Enter 'q' to quit. Enter '1' to enter a new Ad. Enter '2' to search for an Ad by reference number");
					input = in.nextLine();

					switch(input)
					{
						case "1":
							System.out.println("Is this ad from a new Customer? (y/n)");
							input = in.nextLine();
							
							if (input.compareTo("y") == 0)
							{
								// Create a new customer
								System.out.println("");
							}
							else if (input.compareTo("n") == 0)
							{
								// Enter the customer id

							}
							else
							{
								// default case
							}
					}
				}
				break;
			case "4":
				// distributor manager
				break;
			case "q":
			case "quit":
			case "Quit":
			case "exit":
			case "Exit":
				System.out.println("Goodbye.");
				in.close();
				return;
			}


		System.out.println("\nNow what would you like to do?");
		System.out.println("1: Edit/Publish a Newspaper\n2: Edit/Create an Article\n3: Enter a New Ad Sale\n4: Add/Remove a Distribution Subscription\nq: Quit");
		}
	}
}
