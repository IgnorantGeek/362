package newspaper.managers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Random;
import newspaper.models.Employee;

public class Report {
	private HashMap<Employee, String> employeeToMessage;
	public Report()
	{
		employeeToMessage = new HashMap<Employee, String>();
		init();
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
	private boolean init()
	{
		//TODO
		return false;
	}
}
