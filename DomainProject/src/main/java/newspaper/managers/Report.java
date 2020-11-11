package newspaper.managers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import newspaper.models.Employee;
import newspaper.ui.Command;

/**
 * A class that handles all employee reports.
 * @author Alexander Irlbeck
 * Works as of 11/10/20
 */
public class Report implements  Commandable
{
	/**
	 * A hashmap of all employee reports with their corresponding messages.
	 */
	private HashMap<Employee, String> employeeToMessage;
	/**
	 * The constructor for the Report class.
	 * @param e the employee manager to get used.
	 * Works as of 11/10/20
	 */
	public Report(EmployeeManager e)
	{
		employeeToMessage = new HashMap<Employee, String>();
		init(e);
	}
	/**
	 * Prints out all employees reported, prompts for a choice, then it prints out that employees report message.
	 * @param clearance The user's clearance level
	 * @return Returns whether or not the operation was successful
	 * Works as of 11/10/20
	 */
	public boolean readReports(int clearance)
	{
		@SuppressWarnings("resource")
		Scanner in = new Scanner(System.in);
		System.out.println("Here are all reported employees:");
		ArrayList<Employee> list = new ArrayList<Employee>();
		for(int i = 0; i < employeeToMessage.size(); i++)
		{
			list.add((Employee) employeeToMessage.keySet().toArray()[i]);
			System.out.println(i+": "+list.get(i).FullName());
		}
		System.out.println("Select a number from above to read their report(s).");
		String spot = in.nextLine();
		int index;
		try
		{
			index = Integer.parseInt(spot);
		}
		catch(Exception NumberFormatException)
		{
			System.out.println(spot+" could not be interpretted as a number.");
			return false;
		}
		System.out.println(list.get(index)+":\n"+employeeToMessage.get(list.get(index)));
		return true;
	}
	/**
	 * Allows the user to report another employee or themselves.
	 * @param e The employee manager to be used in APP.java
	 * @return Whether or not the operation was successful.
	 * Works as of 11/10/20
	 */
	public boolean addReport(EmployeeManager e)
	{
		@SuppressWarnings("resource")
		Scanner in = new Scanner(System.in);
		System.out.println("Here are all employees:");
		ArrayList<Employee> list = new ArrayList<Employee>();
		for(int i = 0; i < e.getEmployeeCount(); i++)
		{
			list.add(e.getRegistry().get((int)e.getRegistry().keySet().toArray()[i]));
			System.out.println(i+": "+list.get(i).FullName());
		}
		System.out.println("Select a number from above to report them.");
		String spot = in.nextLine();
		int index;
		try
		{
			index = Integer.parseInt(spot);
		}
		catch(Exception NumberFormatException)
		{
			System.out.println(spot+" could not be interpretted as a number.");
			return false;
		}
		System.out.println("Add your message to go with your report below (enter 'q' on a line by itself to stop reading in.");
		String cur=in.nextLine();
		String message = "";
		while(cur.compareToIgnoreCase("q")!=0)
		{
			message = message + cur + "\n";
			cur = in.nextLine();
		}
		employeeToMessage.put(list.get(index),message);
		save();
		return true;
	}
	/**
	 * Allows the user to remove an employee report, only if it is not themselves.
	 * @param e The employee to get removed
	 * @param me The current user
	 * @return Whether or not the operation was successful
	 * Works as of 11/10/20
	 */
	public boolean removeReport(Employee e, Employee me)
	{
		if(e.FullName().compareToIgnoreCase(me.FullName())==0)
		{
			System.out.println("You are not allowed to delete reports on yourself.");
			return false;
		}
		employeeToMessage.remove(e);
		save();
		return true;
	}
	/**
	 * Saves current progress made in the class.
	 * @return Whether or not the operation was successful
	 * Works as of 11/10/20
	 */
	private boolean save()
	{
		String builder="";
		for(int i=0;i<employeeToMessage.keySet().size();i++)
		{
			Employee cur = (Employee) employeeToMessage.keySet().toArray()[i];
			builder = builder+cur.FullName()+"\n"+employeeToMessage.get(cur)+"\n"+"q"+"\n";
		}
		BufferedWriter write;
		try {
			write = new BufferedWriter (new FileWriter("../Database/Complaints.txt"));
			write.write(builder);
			write.close();
		} catch (IOException e) {
			System.out.println("Complaints initialization file corrupted or missing. Please contact tech support.");
			return false;
		}
		return true;
	}
	/**
	 * Initializes the class.
	 * @param a The employee manager from app.java
	 * @return Whether or not the operation was successful
	 * Works as of 11/10/20
	 */
	private boolean init(EmployeeManager a)
	{
		File f = new File("../Database/Complaints.txt");
		Scanner scan;
		try {
			scan = new Scanner(f);
		} catch (FileNotFoundException e) {
			System.out.println("Complaints initialization file corrupted or missing. Please contact tech support.");
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
			String name = lines.remove(0);
			String message = "";
			if(lines.size()>0)
			{
				String in = lines.remove(0);
				while(in.compareToIgnoreCase("q")!=0)
				{
					message = message + "\n" + in;
					if(lines.size()<1)
					{
						System.out.println("Complaints initialization file corrupted. Please contact tech support.");
						scan.close();
						return false;
					}
					in = lines.remove(0);
				}
				int toggle = 0;
				for(int i = 0; i < a.getEmployeeCount();i++)
				{
					if(a.getRegistry().get(a.getRegistry().keySet().toArray()[i]).FullName().compareTo(name)==0)
					{
						toggle = 1;
						employeeToMessage.put(a.getRegistry().get(a.getRegistry().keySet().toArray()[i]), message);
					}
				}
				if(toggle==0)
				{
					System.out.println(name+" was not found. Continuing like normal.");
				}
			}
			else
			{
				System.out.println("Complaints initialization file corrupted. Please contact tech support.");
				scan.close();
				return false;
			}
		}
		scan.close();
		return true;
	}
	/**
	 * Returns the hashmap (only used for background operations.
	 * @return The hashmap
	 * Works as of 11/10/20
	 */
	public HashMap<Employee, String> getReports()
	{
		return employeeToMessage;
	}

	@Override
	public String executeCommand(Employee loggedIn, Command command)
	{
		// TODO
		return null;
	}
}
