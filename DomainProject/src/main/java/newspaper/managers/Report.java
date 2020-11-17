package newspaper.managers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;

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
	private EmployeeManager eman;
	/**
	 * The constructor for the Report class.
	 * @param e the employee manager to get used.
	 * Works as of 11/10/20
	 */
	public Report(EmployeeManager e)
	{
		employeeToMessage = new HashMap<Employee, String>();
		init(e);
		this.eman = e;
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

	public String readReport(int employeeID)
	{
		StringBuilder build = new StringBuilder();
		Employee find = eman.getEmployee(employeeID);

		Set<Employee> reports = employeeToMessage.keySet();
		int i = 0;
		for (Employee report : reports)
		{
			if (report.Id() == find.Id())
			{
				if (i == 0)
				{
					build.append("Reports for Employee: ").append(employeeID);
				}
				build.append('\n').append(i).append(":  ").append(employeeToMessage.get(report));
				i++;
			}
		}

		if (i == 0) build.append("Report internal Error: No reports for Employee ").append(employeeID);
		return build.toString();
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

	public String addReport(int EmployeeID, String message)
	{
		StringBuilder build = new StringBuilder();

		if (!eman.checkID(EmployeeID))
		{
			build.append("Report cmd Error: no Employee found with ID ").append(EmployeeID);
			return build.toString();
		}

		employeeToMessage.put(eman.getEmployee(EmployeeID), message);
		build.append("Successfully added new Report for Employee ").append(EmployeeID);

		return build.toString();
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

	public String removeReport(Employee loggedIn, int dropId)
	{
		Employee check = eman.getEmployee(dropId);

		if (loggedIn.Id() == check.Id())
			return "Report internal Error: You are not allowed to delete reports on yourself.";

		employeeToMessage.remove(check);
		return "Removed Report for employee " + dropId;
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
				for (Employee employee : a.getRegistry().values())
				{
					if(employee.FullName().compareTo(name)==0)
					{
						toggle = 1;
						employeeToMessage.put(employee, message);
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
		StringBuilder build = new StringBuilder();

		switch (command.getCommand())
		{
			case "read":
				if (command.getOptions().size() < 1)
				{
					build.append("Report cmd Error: Missing required argument\n");
					build.append("Expected - read <employeeID>");
					break;
				}
				String empIDStr = command.getOptions().get(0);
				int readID;

				try {
					readID = Integer.parseInt(empIDStr);
				}
				catch (NumberFormatException e)
				{
					build.append("Report cmd Error: Employee ID value '").append(empIDStr);
					build.append("' is not a valid number");
					break;
				}

				if (eman.checkID(readID))
				{
					build.append(readReport(readID));
				}
				else build.append("Report cmd Error: No Employee found with ID: ").append(readID);
				break;

			case "add":
				// add a new report
				if (command.getOptions().size() < 2)
				{
					build.append("Report cmd Error: Missing required argument\n");
					build.append("Expected - add <employeeID> <message>");
					break;
				}

				int addID;

				try{
					addID = Integer.parseInt(command.getOptions().get(0));
				}
				catch (NumberFormatException e)
				{
					build.append("Report cmd Error: Employee ID argument '").append(command.getOptions().get(0));
					build.append("' is not a valid number");
					break;
				}

				build.append(addReport(addID, command.getOptions().get(1)));
				break;

			case "remove":
				if (command.getOptions().size() < 1)
				{
					build.append("Report cmd Error: Missing required argument\n");
					build.append("Expected - remove <employeeID>");
				}
				else if (command.getOptions().size() == 1)
				{
					int check;

					try {
						check = Integer.parseInt(command.getOptions().get(0));
					} catch (NumberFormatException e)
					{
						build.append("Report cmd Error: EmployeeID value is not a number: ").append(e.getMessage());
						break;
					}

					build.append(removeReport(loggedIn, check));
				}
				else
				{
					build.append("Report cmd Error: Too many arguments\n");
					build.append("Expected - remove <employeeID>");
				}
				break;
			default:
				build.append("No binding for command '").append(command.getCommand()).append("'");
		}
		return build.toString();
	}
}
