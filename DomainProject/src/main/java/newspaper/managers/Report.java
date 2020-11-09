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

public class Report {
	private HashMap<Employee, String> employeeToMessage;
	public Report(EmployeeManager e)
	{
		employeeToMessage = new HashMap<Employee, String>();
		init(e);
	}
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
	public boolean addReport(EmployeeManager e)
	{
		@SuppressWarnings("resource")
		Scanner in = new Scanner(System.in);
		System.out.println("Here are all employees:");
		ArrayList<Employee> list = new ArrayList<Employee>();
		for(int i = 0; i < e.getEmployeeCount(); i++)
		{
			list.add((Employee) e.getRegistry().keySet().toArray()[i]);
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
			message = message + in.nextLine() + "\n";
		}
		employeeToMessage.put(list.get(index),message);
		save();
		return true;
	}
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
}
