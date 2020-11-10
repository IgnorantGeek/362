package newspaper;

import newspaper.managers.*;
import newspaper.models.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

/**
 * Handle the IOException properly
 */
public class App
{
	public static void main( String[] args ) throws IOException
	{
		NewspaperManager nman = new NewspaperManager();
		ArticleManager aman = new ArticleManager();
		AdManager adman = new AdManager();
		SubscriptionManager sman = new SubscriptionManager();
		DistributionManager dman = new DistributionManager();
		DMEditor messageToEditor = new DMEditor();
		Feedback feedback = new Feedback();
		EmployeeManager eman = new EmployeeManager(10); // Reserve ids 0-9 for testing
		FinancialManager fman = new FinancialManager(eman,adman,sman,dman);
		Report rport = new Report(eman);
		Retract ract = new Retract(nman, aman);
		// Flush screen and begin outer loop
		Global.flushConsole();
		System.out.println("Welcome to the FakeNews! NewsPaper Management System.\n");
		Scanner in = new Scanner(System.in);

		outer:
			// Login
			while (true)
			{
				System.out.println("Please enter your login info. Or type 'quit' to exit the system.");
				System.out.print("User ID: ");
				String useridString = in.nextLine();
				if (useridString.toLowerCase().compareTo("quit") == 0
						||  useridString.toLowerCase().compareTo("q") == 0)
				{
					in.close();
					System.out.println("Goodbye.");
					return;
				}
				int user_id;
				try
				{
					user_id = Integer.parseInt(useridString);
				}
				catch(Exception NumberFormatException)
				{
					System.out.println(useridString+" is not a valid number.");
					continue;
				}
				System.out.print("Password: ");
				String password = in.nextLine();

				Employee loggedIn = eman.validateLogin(user_id, password);

				// Main activity
				if (loggedIn != null)
				{
					Global.flushConsole();
					System.out.println("Welcome, " + loggedIn.FullName() + ". What would you like to do?");
					System.out.println("1: Edit/Publish/View a Newspaper\n2: Edit/Create/View an Article\n3: Enter a New Ad Sale" +
							"\n4: Add/Remove a Subscription\n5: Add/Remove a Distributor\n6: To see our reviews\n7: Add/Remove/Update an Employee"
							+ "\n8: Review Financial Records\n9: Report an Employee\n10: Remove a report\n11: See all employee reports\nq: Logout");

					while (true)
					{
						String input = in.nextLine();
						String outMsg = null;

						switch(input)
						{
						case "1":
							System.out.println("NewspaperManager -- Enter 'q' to quit. Enter '1' to search for a newspaper and read it."
									+ "\nEnter '2' to add a newspaper. Enter '3' to search for a paper and then edit it. Enter '4' to"
									+ "\nsave your current progress. Enter '5' to search for a paper and publish it. Enter '6' to search and order"
									+ "\na newspaper. Enter '7' to retract a newspaper.");
							input = in.nextLine();
							if (input.compareTo("q") == 0) break;
							int clearance = 10;
							String next;
							switch(input)
							{
							case "q":
								break;
							case "1":
								Newspaper paper = nman.search();
								if (paper == null) break;
								if(ract.getPapers().contains(paper))
								{
									System.out.println("The paper has been retracted and is no longer viewable due to an error on our part.");
									break;
								}
								paper.readPaper(clearance);
								System.out.println("Would you like to tell the editor anything about this paper? (y/n)");
								next = in.nextLine();
								if(next.compareToIgnoreCase("y")==0)
								{
									messageToEditor.addPaperComment(paper);
								}
								break;
							case "2":
								nman.addPaper(clearance);
								break;
							case "3":
								Newspaper temp = nman.search();
								if(ract.getPapers().contains(temp))
								{
									System.out.println("The paper has been retracted and is no longer editable due to an error on our part.");
									break;
								}
								nman.editPaper(temp, clearance);
								break;
							case "4":
								nman.save();
								break;
							case "5":
								Newspaper temp1 = nman.search();
								if(ract.getPapers().contains(temp1))
								{
									System.out.println("The paper has been retracted and is no longer finalizable due to an error on our part.");
									break;
								}
								temp1.finalizePaper(clearance);
								break;
							case "6":
								Newspaper temp2 = nman.search();
								if(ract.getPapers().contains(temp2))
								{
									System.out.println("The paper has been retracted and is no longer orderable due to an error on our part.");
									break;
								}
								temp2.orderPaper();
								break;
							case "7":
								ract.retract(clearance, nman, aman);
							}
							break;
						case "2":
							System.out.println("ArticleManager -- Enter 'q' to quit. Enter '1' to search for an article, choose form a list of articles, and read one."
									+ "\nEnter '2' to add an article. Enter '3' to search for an article and then edit it. Enter '4' to search for an article and publish it."
									+ "\nEnter '5' to retract an article.");
							input = in.nextLine();
							if (input.compareTo("q") == 0) break;
							System.out.println("Enter your clearance level.");
							clearance = 10;
							next = in.nextLine();
							switch(input)
							{
							case "q":
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
								if(ract.getArticles().contains(arr[num]))
								{
									System.out.println("The article has been retracted and is no longer readable due to an error on our part.");
									break;
								}
								arr[num].readArticle(clearance);
								System.out.println("Would you like to tell the editor anything about this article? (y/n)");
								next = in.nextLine();
								if(next.compareToIgnoreCase("y")==0)
								{
									messageToEditor.addArticleComment(arr[num]);
								}
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
								if(ract.getArticles().contains(Arr[Num]))
								{
									System.out.println("The article has been retracted and is no longer editable due to an error on our part.");
									break;
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
								if(ract.getArticles().contains(ARR[NUM]))
								{
									System.out.println("The article has been retracted and is no longer finalizable due to an error on our part.");
									break;
								}
								ARR[NUM].finalizeArticle(clearance);
								break;
							case "5":
								ract.retract(clearance, nman, aman);
							}
							break;
						case "3":
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
									System.out.println("Enter the name of the Advertiser:");
									input = in.nextLine();
									String name = input;

									System.out.println("Enter the issue of the paper for this Ad:");
									input = in.nextLine();
									int issue = Integer.parseInt(input);

									System.out.println("Enter the volume of the paper for this Ad:");
									input = in.nextLine();
									int volume = Integer.parseInt(input);

									// Check paper
									int[] paperInfo = {issue, volume, 0, 0, 0};

									System.out.println("Enter the Ad image filename:");
									input = in.nextLine();

									if (input.compareTo("") == 0)
									{
										System.out.println("Warning! You created an Ad with no image. This will appear blank on the paper.");
									}

									// Create the Ad and Advertiser, add both to db
									Advertiser newAdvertiser = adman.newAdvertiser(name);
									adman.newAd(paperInfo, input, newAdvertiser.id());
								}
								else if (input.compareTo("n") == 0)
								{
									// Enter the customer id
									System.out.println("Please enter the ID of the customer to append this add to");

									input = in.nextLine();

									int custID=0;
									try
									{
										custID = Integer.parseInt(input);
									}
									catch(Exception NumberFormatException)
									{
										System.out.println(input+" is not a valid number. Cancelling current attempt.");
										continue;
									}

									File custRoot = new File("../Database/Customers");

									if (custRoot.exists())
									{
										boolean found = false;
										for (String custFile : custRoot.list())
										{
											if (custFile.compareTo(custID + ".txt") == 0)
											{
												found = true;
												break;
											}
										}
										if (!found)
										{
											System.out.println("Could not find an active Customer with ID: " + custID);
										}
									}
									else
									{
										System.out.println("Fatal error, Customer database not initialized. Exiting session.");
										continue;
									}

									System.out.println("Enter the issue of the paper for this Ad:");
									input = in.nextLine();
									int issue = Integer.parseInt(input);

									System.out.println("Enter the volume of the paper for this Ad:");
									input = in.nextLine();
									int volume = Integer.parseInt(input);

									// Check paper
									int[] paperInfo = {issue, volume, 0, 0, 0};

									System.out.println("Enter the Ad image filename:");
									input = in.nextLine();

									if (input.compareTo("") == 0)
									{
										System.out.println("Warning! You created an Ad with no image. This will appear blank on the paper.");
									}

									adman.newAd(paperInfo, input, custID);
								}
								else
								{
									System.out.println("Not a valid response.");
								}
								break;

							case "2":
								break;

							case "q":
								break;
							}		
							break;
						case "4":
							System.out.println("SubscriptionManager -- Enter 'q' to quit. Enter '1' to print all current subscriptions."
									+ "\nEnter '2' to add a new subscription. Enter '3' to remove a subscription.");
							input = in.nextLine();
							switch(input)
							{
							case "q":
							case "quit":
							case "Quit":
							case "exit":
							case "Exit":
								break;
							case "1":
								Collection<Subscription> subs = sman.getAllSubs();
								if(subs.size() > 0) {
									for(Subscription sub: subs) {
										System.out.println(sub.toString());
									}
									System.out.println();
								}
								else
									System.out.println("There are no currently active subscriptions.");

								break;
							case "2":
								if (sman.addSubscription()) {
									System.out.println("Subscription successfully added");
								}
								else {
									System.out.println("No subscription was added");
								}

								break;
							case "3":
								if(sman.removeSubscription()) {
									System.out.println("Subscription successfully removed");
								}
								else {
									System.out.println("No subscription was removed");
								}
								break;
							}
							break;
						case "5":
							// Add or remove a new distributor
							System.out.println("Would you like to add or remove a Distributor? (a/r)");

							input = in.nextLine();

							switch (input)
							{
							case "a":
							case "add":
							case "Add":
							case "ADD":
								System.out.println("Enter the name of the new Distributor");

								String name = in.nextLine();

								System.out.println("Enter the number of papers to send to this Distributor");
								String paperStr = in.nextLine();

								int pc;
								try
								{
									pc = Integer.parseInt(paperStr);
								}
								catch(Exception NumberFormatException)
								{
									System.out.println(input+" is not a valid number. Cancelling current attempt.");
									continue;
								}

								if (dman.addDistributor(name, pc) == 0)
								{
									System.out.println("Added new Distributor: " + name);
								}
								break;

							case "r":
							case "remove":
							case "Remove":
							case "REMOVE":
								System.out.println("Enter the ID of the Distributor you would like to drop");

								input = in.nextLine();

								if (dman.removeDistributor(Integer.parseInt(input)) < 0)
								{
									System.out.println("Failed to remove Distributor with ID: " + input +" no entry found.");
								}
								else System.out.println("Successfully remove Distributor: " + input);
								break;
							default:
								System.out.println("Seriously?");
							}

							break;
						case "6":
							System.out.println("Welcome to our reviews section. Would you like to see all of our reviews (enter 1), enter a new review (enter 2), or");
							System.out.println("delete a review (enter 3).");
							input = in.nextLine();
							clearance = 10;
							switch(input)
							{
							case "1":
								feedback.displayFeedback();
								break;
							case "2":
								feedback.giveFeedback();
								break;
							case "3":
								feedback.removeFeedback(clearance);
								break;
							default:
								System.out.println("Not a valid option. Exiting.");
							}
							break;
						case "7":
							// Add or remove an employee
							System.out.println("Would you like to (1) add, (2) remove, or (3) update an employee?");

							input = in.nextLine();

							switch (input)
							{
							case "1":
								// Add an employee
								System.out.println("Please enter the full name of the new employee:");
								String name = in.nextLine();

								int supervisorID = loggedIn.Id();
								String newPassword = "";
								System.out.println("Are you the supervisor for this employee? (y/n)");
								input = in.nextLine();

								if (input.compareTo("y") == 0)
								{
									while (true)
									{
										System.out.println("Please enter the password for the new user:");
										newPassword = in.nextLine();

										System.out.println("Please re-enter your password:");
										input = in.nextLine();

										if (newPassword.compareTo(input) == 0)
										{
											break;
										}
										else
										{
											System.out.println("The passwords you entered did not match!\n");
										}
									}
								}
								else if (input.compareTo("n") == 0)
								{
									while (true)
									{
										System.out.println("Please enter the ID of the employee who will be the supervisor for this employee:");
										String idString = in.nextLine();

										// Search the employee db for this employee
										if (!eman.checkID(Integer.parseInt(idString)))
										{
											System.out.println("The ID that you entered is not a valid ID.\n");
											break;
										}
										else supervisorID = Integer.parseInt(idString);
									}

									// Get password
									String newEmpPassword = "";
									boolean run = true;
									while (run)
									{
										System.out.println("Please enter the password for the new user:");
										newEmpPassword = in.nextLine();

										System.out.println("Please re-enter your password:");
										input = in.nextLine();

										if (newEmpPassword.compareTo(input) == 0)
										{
											run = false;
										}
										else
										{
											System.out.println("The passwords you entered did not match!\n");
										}
									}

									int id = eman.addHourlyEmployee(name, newEmpPassword, supervisorID, 10);

									// This isn't working and I don't know why. Still adds the employee
									// But the output message does not work
									if (id < 0)
									{
										outMsg = "Warning! There was an error writing the new employee to the database";
									}
									else
									{
										outMsg = "Successfully added new Employee with ID: " + id;
									}
								}								
								break;
							case "2":
								System.out.println("Enter the ID of the employee to remove");

								input = in.nextLine();

								int dropID = Integer.parseInt(input);

								if (eman.checkID(dropID))
								{
									// Check the authorization for the logged in user
									if (eman.checkPrivilege(loggedIn, dropID))
									{
										if (eman.dropEmployee(loggedIn, dropID) == 0) outMsg = "Successfully dropped Employee with ID " + dropID;
										else outMsg = "An internal error occurred";
									}
									else
									{
										outMsg = "You are not authorized to remove this Employee";
									}
								}
								else
								{
									outMsg = "Could not find Employee with ID: " + input;
								}
								break;
							case "3":
								System.out.println("Enter the ID of the employee to update");

								input = in.nextLine();

								int updateID = Integer.parseInt(input);

								if (eman.checkID(updateID))
								{
									// Check the authorization for the logged in user
									if (eman.checkPrivilege(loggedIn, updateID))
									{
										System.out.println("Would you like to update (1) Name, (2) Password, or (3) Supervisor?");

										input = in.nextLine();

										int updateChoice = Integer.parseInt(input);

										switch (updateChoice)
										{
										case 1:
											System.out.println("Enter the new name for this Employee");

											String newEmployeeName = in.nextLine();

											if (eman.updateFullName(updateID, newEmployeeName)) outMsg = "Successfully updated Employee information";
											else outMsg = "Error updating Employee";
											break;

										case 2:
											System.out.println("Enter the new password for this Employee");

											String updatePassword = in.nextLine();

											boolean run = true;
											while (run)
											{
												System.out.println("Please re-enter the same password");

												String checkPw = in.nextLine();

												if (checkPw.compareTo(updatePassword) == 0) run = false;
												else
												{
													System.out.println("Error! Passwords did not match. Enter the new password again");

													updatePassword = in.nextLine();
												}
											}

											if (eman.updatePassword(updateID, updatePassword)) outMsg = "Successfully updated Employee information";
											else outMsg = "Error updating Employee";
											break;

										case 3:
											System.out.println("Enter the ID of the new supervisor for this user");

											String idString = in.nextLine();

											int updateSupervisorID = Integer.parseInt(idString);

											if (eman.checkID(updateSupervisorID))
											{
												if (eman.updateSupervisorId(updateID, updateSupervisorID)) outMsg = "Successfully updated Employee information";
												else outMsg = "Error updating Employee";
											}
											else outMsg = "No supervisor found with ID " + idString;
											break;
										default:
											outMsg = "Error, not a valid choice for Update Employee";
											break;
										}
									}
									else
									{
										outMsg = "You are not authorized to update this Employee";
									}
								}
								else
								{
									outMsg = "Could not find Employee with ID: " + input;
								}
								break;
							default:
								outMsg = "The choice you entered is not a valid response";
								break;
							}
							break;

						case "8":
							System.out.println("Financial reports avaliable: 1 for Payroll, 2 for monthly financial report.");
							input = in.nextLine();
							switch (input)
							{
							case "1":
								System.out.println("Payroll so far for this month:");
								System.out.println(fman.payRoll());
								boolean answered = false;
								while(!answered) {
									System.out.println("Reset hours for monthly workers? y for yes, n for no.");
									input = in.nextLine();

									switch (input)
									{
									case "y":
									case "Y":
									case "yes":
									case "Yes":
									case "YES":
										answered = true;
										fman.resetEmployeeHours();
										System.out.println("Hours reset.");
										break;
									case "n":
									case "N":
									case "no":
									case "No":
									case "NO":
										answered = true;
										System.out.println("Hours not reset.");
									default:
										System.out.println("Not a valid input");
									}
								}
								break;
							case "2":
								System.out.println(fman.FinancialReport());
								System.out.println("Press enter to return to main menu");
								input = in.nextLine();
								break;
							case "q":
							case "quit":
							case "Quit":
							case "exit":
							case "Exit":
								break;
							}
							break;
						case "9": 
							rport.addReport(eman);
							break;
						case "10":
							ArrayList<Employee> list = new ArrayList<Employee>();
							for(int i = 0; i < rport.getReports().size(); i++)
							{
								list.add((Employee)rport.getReports().keySet().toArray()[i]);
								System.out.println(i+": "+list.get(i).FullName());
							}
							System.out.println("Select a number from above to report them.");
							int loc = 0;
							try
							{
								loc = Integer.parseInt(in.nextLine());
							}
							catch(Exception NumberFormatException)
							{
								System.out.println("Input could not be read as an integer. Terminating current process.");
								break;
							}
							Employee found = list.get(loc);
							rport.removeReport(found, loggedIn);
							break;
						case "11":
							clearance = 10;
							rport.readReports(clearance);
							break;
						case "q":
						case "quit":
						case "Quit":
						case "exit":
						case "Exit":
							Global.flushConsole();
							loggedIn = null;
							continue outer;
						default:
							outMsg = "No binding for input: " + input;
						}

						Global.flushConsole();
						// Print an output message that can be read after flushing the screen
						if (outMsg != null)
						{
							System.out.println(outMsg + '\n');
						}
						System.out.println("Now what would you like to do?");
						System.out.println("1: Edit/Publish/View a Newspaper\n2: Edit/Create/View an Article\n3: Enter a New Ad Sale" +
								"\n4: Add/Remove a Subscription\n5: Add/Remove a Distributor\n6: To see our reviews\n7: Add/Remove/Update an Employee"
								+ "\n8: Review Financial Records\n9: Report an Employee\n10: Remove a report\n11: See all employee reports\nq: Logout");
					}
				}
				else
				{
					System.out.println("Login Failed! Invalid credentials.");
				}
			}
	}
}
