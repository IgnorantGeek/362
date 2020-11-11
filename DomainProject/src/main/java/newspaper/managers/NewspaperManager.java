package newspaper.managers;

import newspaper.models.Newspaper;
import newspaper.ui.Command;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * A class that manages all newspapers in the program, grabs them from its initialization file, and after .close() is called, saves the settings in init file, then wipes everything.
 * @author Alexander Irlbeck
 * **Works as of 10/14/20
 */
public class NewspaperManager implements Commandable
{
	/**
	 * Input is in the format of "volume, issue" for the key, and the newspaper corresponds with that key.
	 */
	private HashMap<String,Newspaper> volumeAndIssue;
	/**
	 * Input is in the format of "day, month, year" for the key, and the newspaper corresponds with that key.
	 */
	private HashMap<String,Newspaper> dayMonYear;
	/**
	 * A list of every newspaper available.
	 */
	private ArrayList<Newspaper> news;
	public NewspaperManager()
	{
		volumeAndIssue=new HashMap<String, Newspaper>();
		dayMonYear=new HashMap<String, Newspaper>();
		news = new ArrayList<Newspaper>();
		init();
	}
	/**
	 * Searches current newspapers for a paper and will return it whether or not the actor should see it. Like a coming soon type of deal when it is not yet published.
	 * @return The newspaper that was found, or null if no newspaper was found.
	 * **Works as of 10/14/20
	 */
	public Newspaper search()
	{
		System.out.println("Enter '1' to look for a newspaper by volume/issue, '2' to look for a newspaper by its publishing date. Press 'q' to exit.");
		@SuppressWarnings("resource")//System.in should not be closed before the program has finished.
		Scanner in = new Scanner(System.in);
		String next=in.next();
		while(!next.equals("q"))
		{
			int num = 0;
			try
			{
				num = Integer.parseInt(next);
			}
			catch(Exception NumberFormatException)
			{
				System.out.println(next+" is not a valid number or 'q'. Please insert a valid number or 'q'.");
			}
			if(num==1)
			{
				System.out.println("Please enter the newspaper's volume.");
				int volume = 0;
				next = in.next();
				try
				{
					volume = Integer.parseInt(next);
				}
				catch(Exception NumberFormatException)
				{
					System.out.println(next+" is not a valid number. Cancelling current search.");
					System.out.println("Enter '1' to look for a newspaper by volume/issue, '2' to look for a newspaper by its publishing date. Press 'q' to exit.");
				}
				System.out.println("Please enter the newspaper's issue.");
				int issue = 0;
				next = in.next();
				try
				{
					issue = Integer.parseInt(next);
				}
				catch(Exception NumberFormatException)
				{
					System.out.println(next+" is not a valid number. Cancelling current search.");
					System.out.println("Enter '1' to look for a newspaper by volume/issue, '2' to look for a newspaper by its publishing date. Press 'q' to exit.");
				}
				String volIss = volume+", " + issue;
				if(!volumeAndIssue.containsKey(volIss))
				{
					System.out.println("Volume and issue could not be found. Press '1' to search again or select another searching method.");
				}
				else
				{
					return volumeAndIssue.get(volIss);
				}
			}
			else if(num==2)
			{
				System.out.println("Please enter the newspaper's day.");
				int day = 0;
				next = in.next();
				try
				{
					day = Integer.parseInt(next);
				}
				catch(Exception NumberFormatException)
				{
					System.out.println(next+" is not a valid number. Cancelling current search.");
					System.out.println("Enter '1' to look for a newspaper by volume/issue, '2' to look for a newspaper by its publishing date. Press 'q' to exit.");
				}
				System.out.println("Please enter the newspaper's month.");
				int month = 0;
				next = in.next();
				try
				{
					month = Integer.parseInt(next);
				}
				catch(Exception NumberFormatException)
				{
					System.out.println(next+" is not a valid number. Cancelling current search.");
					System.out.println("Enter '1' to look for a newspaper by volume/issue, '2' to look for a newspaper by its publishing date. Press 'q' to exit.");
				}
				System.out.println("Please enter the newspaper's year.");
				int year = 0;
				next = in.next();
				try
				{
					year = Integer.parseInt(next);
				}
				catch(Exception NumberFormatException)
				{
					System.out.println(next+" is not a valid number. Cancelling current search.");
					System.out.println("Enter '1' to look for a newspaper by volume/issue, '2' to look for a newspaper by its publishing date. Press 'q' to exit.");
				}
				String date = day+", "+month+", "+year;
				if(!dayMonYear.containsKey(date))
				{
					System.out.println("Date could not be found. Press '2' to search again or select another searching method.");
				}
				else
				{
					return dayMonYear.get(date);
				}
			}
			else
			{
				System.out.println("The number inputed is not in the the approved options. Please an acceptable number.");
			}
			next=in.next();
		}
		return null;
	}
	/**
	 * Adds a paper to the collection and allows for edits on that paper right away. Initialized as not finalized and is added to the init file with its pages.
	 * @param clearance The actors clearance level that allows for them to use this function or not.
	 * @return returns the finished newspaper, or null if the method was unsuccessful.
	 * **Works as of 10/14/20
	 */
	public Newspaper addPaper(int clearance)
	{
		if(clearance<3)
		{
			return null;
		}
		@SuppressWarnings("resource")//System.in should not be closed before the program has finished.
		Scanner scan = new Scanner(System.in);
		int volume = 0;
		System.out.println("Please enter the newspaper's volume.");
		String between = scan.next();
		try
		{
			volume = Integer.parseInt(between);
		}
		catch(Exception NumberFormatException)
		{
			System.out.println(between+" is not a valid number. Cancelling current add.");
			return null;
		}
		int issue = 0;
		System.out.println("Please enter the newspaper's issue.");
		between = scan.next();
		try
		{
			issue = Integer.parseInt(between);
		}
		catch(Exception NumberFormatException)
		{
			System.out.println(between+" is not a valid number. Cancelling current add.");
			return null;
		}
		int day = 0;
		System.out.println("Please enter the newspaper's day.");
		between = scan.next();
		try
		{
			day = Integer.parseInt(between);
		}
		catch(Exception NumberFormatException)
		{
			System.out.println(between+" is not a valid number. Cancelling current add.");
			return null;
		}
		int month = 0;
		System.out.println("Please enter the newspaper's month.");
		between = scan.next();
		try
		{
			month = Integer.parseInt(between);
		}
		catch(Exception NumberFormatException)
		{
			System.out.println(between+" is not a valid number. Cancelling current add.");
			return null;
		}
		int year = 0;
		System.out.println("Please enter the newspaper's year.");
		between = scan.next();
		try
		{
			year = Integer.parseInt(between);
		}
		catch(Exception NumberFormatException)
		{
			System.out.println(between+" is not a valid number. Cancelling current add.");
			return null;
		}
		Newspaper toAdd = new Newspaper(volume, issue, day, month, year, false);
		System.out.println("If you want to enter editing pages mode, type anything but 'q'. If you want to exit editing mode, enter 'q'.");
		String next = scan.next();
		String added = "";
		int counter=1;
		while(!next.equals("q"))
		{
			String temp = editPaper(toAdd,clearance);
			if(temp!=null)
			{
				if(counter==1)
				{
					added= added+temp;
				}
				else
				{
					added= added+", "+temp;
				}
				counter++;
			}
			System.out.println("If you want to enter editing pages mode, type anything but 'q'. If you want to exit editing mode, enter 'q'.");
			next = scan.next();
		}
		String volumeAndIssue = volume+", "+issue;
		String date = day+", "+month+", "+year;
		if((this.volumeAndIssue.containsKey(volumeAndIssue)&&this.volumeAndIssue.get(volumeAndIssue).getPublished())||(dayMonYear.containsKey(date)&&dayMonYear.get(date).getPublished()))
		{
			System.out.println("Published paper with this volume and issue already exist. Aborting process.");
			return null;
			
		}
		if(this.volumeAndIssue.containsKey(volumeAndIssue))
		{
			this.volumeAndIssue.replace(volumeAndIssue,toAdd);
		}
		
		else
		{
			this.volumeAndIssue.put(volumeAndIssue, toAdd);
		}
		if(dayMonYear.containsKey(date))
		{
			dayMonYear.replace(volumeAndIssue,toAdd);
		}
		else
		{
			dayMonYear.put(date, toAdd);
		}
		String builder ="\n" + volume + ", " + issue + ", " + day + ", " + month + ", " + year + ", " + false + ", " + added;
		BufferedWriter write;
		try {
			write = new BufferedWriter (new FileWriter("../Database/NewspaperPages/NewspaperInit.txt",true));
			write.append(builder);
			write.close();
		} catch (IOException e) {
			System.out.println("Newspaper initialization file corrupted or missing. Please contact tech support.");
			return null;
		}
		news.add(toAdd);
		return toAdd;
	}
	/**
	 * Initializes the class from the init file in the Database.
	 * @return Returns true if it was successful.
	 * **Works as of 10/14/20
	 */
	public boolean init()
	{
		Scanner scan;
		try {
			File f = new File("../Database/NewspaperPages/NewspaperInit.txt");
			scan = new Scanner(f);
		} catch (FileNotFoundException e) {
			System.out.println("Newspaper initialization file corrupted or missing. Please contact tech support.");
			return false;
		}
		while(scan.hasNextLine())
		{
			String line = scan.nextLine();
			String[] in = line.split(", ");
			int volume = Integer.parseInt(in[0]);
			int issue = Integer.parseInt(in[1]);
			int day = Integer.parseInt(in[2]);
			int month = Integer.parseInt(in[3]);
			int year = Integer.parseInt(in[4]);
			Newspaper toAdd = new Newspaper(volume, issue, day, month, year, false);
			for(int i = 6; i<in.length;i++)
			{
				if(!toAdd.pages.contains(in[i]))
				{
					toAdd.pages.add(in[i]);
				}
			}
			boolean finalized=in[5].compareTo("true")==0;
			if(finalized)
			{
				toAdd.setFinal();
			}
			String volumeAndIssue = volume+", "+issue;
			String date = day+", "+month+", "+year;
			if(this.volumeAndIssue.containsKey(volumeAndIssue))
			{
				this.volumeAndIssue.replace(volumeAndIssue,toAdd);
			}
			else
			{
				this.volumeAndIssue.put(volumeAndIssue, toAdd);
			}
			if(dayMonYear.containsKey(date))
			{
				dayMonYear.replace(volumeAndIssue,toAdd);
			}
			else
			{
				dayMonYear.put(date, toAdd);
			}
			news.add(toAdd);
		}
		scan.close();
		return true;
	}
	/**
	 * Allows for the actor to edit the newspaper n. The actor can add onto the end of the newspaper, or replace a page. Necessitates the need for .close() to be called
	 * because it does not update the init file.
	 * @param n The newspaper to edit
	 * @param clearance The clearance level of the actor
	 * @return The new address location of any changed files, or null if the method was unsuccessful.
	 * **Works as of 10/14/20
	 */
	public String editPaper(Newspaper n,int clearance)
	{
		if(n.getPublished())
		{
			System.out.println("Paper has already been finalized.");
			return null;
		}
		if(clearance>3)
		{
			System.out.println("Please choose a page between 1 and " + (n.pages.size()) + " to edit a specific page, or 'a' to add to the end of the newspaper.");
			@SuppressWarnings("resource")//System.in should not be closed before the program has finished.
			Scanner in = new Scanner(System.in);
			String input = in.next();
			if (input.equals("a"))
			{
				System.out.println("Insert the address for your new page. (.png only)");
				input = in.next();
				ImageIcon image = new ImageIcon(input);
				Image between = image.getImage();
				try
				{
					BufferedImage image2 = new BufferedImage(between.getWidth(null), between.getHeight(null), BufferedImage.TYPE_INT_ARGB);
					Graphics2D gr = image2.createGraphics();
					gr.drawImage(between, 0, 0, null);
					gr.dispose();
					String[] list = input.split("/");
					File outputFile = new File("../Database/NewspaperPages/"+list[list.length-1]);
					try {
						ImageIO.write(image2, "png", outputFile);
						return "../Database/NewspaperPages/"+list[list.length-1];
					} catch (IOException e) {
						System.out.println("Image could not be found and/or was not in the correct format.");
						return null;
					}
				}
				catch(IllegalArgumentException e)
				{
					System.out.println("Image could not be found and/or was not in the correct format.");
					return null;
				}
			}
			else
			{
				int converted=0;
				try
				{
					converted = Integer.parseInt(input);
				}
				catch(Exception NumberFormatException)
				{
					System.out.println(input+" is not a valid number. Please insert a valid number or 'a'.");
				}
				if(converted<=n.pages.size()&&converted>0)
				{
					System.out.println("Would you like to replace the current page (Press 'r')?");
					input=in.next();
					if(input.equals("r"))
					{
						String address = n.pages.get(converted-1);
						System.out.println("Insert the address for your replacement page. (.png only)");
						input = in.next();
						ImageIcon image = new ImageIcon(input);
						Image between = image.getImage();
						try
						{
							BufferedImage image2 = new BufferedImage(between.getWidth(null), between.getHeight(null), BufferedImage.TYPE_INT_ARGB);
							Graphics2D gr = image2.createGraphics();
							gr.drawImage(between, 0, 0, null);
							gr.dispose();
							File outputFile = new File(address);
							try {
								ImageIO.write(image2, "png", outputFile);
								return address;
							} catch (IOException e) {
								System.out.println("Image could not be found and/or was not in the correct format.");
								return null;
							}
						}
						catch(IllegalArgumentException e)
						{
							System.out.println("Image could not be found and/or was not in the correct format.");
							return null;
						}
						
					}
					else
					{
						System.out.println("Nothing has been changed.");
						return null;
					}
				}
				else
				{
					System.out.println("Inputed number out of range of current pages. Ending edit session.");
					return null;
				}
			}
		}
		return null;
	}
	/**
	 * Developer use only. Shorthand way of getting a paper.
	 * @param paperInfo All of the papers info
	 * @return The newspaper found, or null if nothing was found.
	 * **Works as of 10/14/20
	 * **Made by Nick Heisler
	 */
	public Newspaper findPaper(int[] paperInfo)
	{
		String vol_string = Integer.toString(paperInfo[0]) + Integer.toString(paperInfo[1]);
		String date_string = Integer.toString(paperInfo[2]) +
							 Integer.toString(paperInfo[3]) +
							 Integer.toString(paperInfo[4]);
		if (volumeAndIssue.containsKey(vol_string))
		{
			return volumeAndIssue.get(vol_string);
		}
		if (dayMonYear.containsKey(date_string))
		{
			return dayMonYear.get(date_string);
		}
		return null;
	}
	/**
	 * Rewrites the init file to show all changes that have been made to each newspaper in the database.
	 * @return Returns whether or not the method was successful.
	 * **Works as of 10/14/20
	 */
	public boolean save()
	{
		String builder="";
		for(int i=0;i<news.size();i++)
		{
			Newspaper cur = news.get(i);
			int[] info = cur.getInfo();
			builder = builder + info[0] + ", " + info[1] + ", " + info[2] + ", " + info[3] + ", " + info[4] + ", " + cur.getPublished();
			for(int j = 0; j<cur.pages.size();j++)
			{
				builder = builder + ", " + cur.pages.get(j);
			}
			if(i+1!=news.size())
			{
				builder = builder + "\n";
			}
		}
		BufferedWriter write;
		try {
			write = new BufferedWriter (new FileWriter("../Database/NewspaperPages/NewspaperInit.txt"));
			write.write(builder);
			write.close();
		} catch (IOException e) {
			System.out.println("Newspaper initialization file corrupted or missing. Please contact tech support.");
			return false;
		}
		return true;
	}
	/**
	 * Returns the volume and issue list of newspapers.
	 */
	public HashMap<String,Newspaper> getVolIss()
	{
		return volumeAndIssue;
	}
	
	/**
	 * Publishes newspaper n if it is possible.
	 * @param n The paper to be published
	 * @param clearance The clearance of the actor
	 * @return Whether or not the function succeeded
	 */
	public boolean publishPaper(Newspaper n, int clearance)
	{
		return n.finalizePaper(clearance);
	}
	
	@Override
	public String executeCommand(Command command)
	{
		// TODO
		return null;
	}
}