package newspaper;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class NewspaperManager 
{
	private HashMap<String,Newspaper> volumeAndIssue;
	private HashMap<String,Newspaper> dayMonYear;
	public NewspaperManager()
	{
		volumeAndIssue=new HashMap<String, Newspaper>();
		dayMonYear=new HashMap<String, Newspaper>();
		init();
	}
	public Newspaper search()
	{
		System.out.println("Enter '1' to look for a newspaper by volume/issue, '2' to look for a newspaper by its publishing date.");
		Scanner in = new Scanner(System.in);
		boolean going = true;
		while(going)
		{
			going=false;
			int num = in.nextInt();
			if(num==1)
			{
				System.out.println("Please enter the newspaper's volume.");
				int volume = in.nextInt();
				System.out.println("Please enter the newspaper's issue.");
				int issue = in.nextInt();
				String volIss = volume+", " + issue;
				if(!volumeAndIssue.containsKey(volIss))
				{
					System.out.println("Volume and issue could not be found. Press '1' to search again or select another searching method.");
					going=true;
				}
				else
				{
					in.close();
					return volumeAndIssue.get(volIss);
				}
			}
			else if(num==2)
			{
				System.out.println("Please enter the newspaper's day.");
				int day = in.nextInt();
				System.out.println("Please enter the newspaper's month.");
				int month = in.nextInt();
				System.out.println("Please enter the newspaper's year.");
				int year = in.nextInt();
				String date = day+", "+month+", "+year;
				if(!dayMonYear.containsKey(date))
				{
					System.out.println("Volume and issue could not be found. Press '1' to search again or select another searching method.");
					going=true;
				}
				else
				{
					in.close();
					return dayMonYear.get(date);
				}
			}
			else
			{
				System.out.println("The number inputed is not in the the approved options. Please an acceptable number.");
				going=true;
			}
		}
		in.close();
		return null;
	}
	public Newspaper addPaper(int clearance)
	{
		Scanner scan = new Scanner(System.in);
		int volume = scan.nextInt();
		int issue = scan.nextInt();
		int day = scan.nextInt();
		int month = scan.nextInt();
		int year = scan.nextInt();
		Newspaper toAdd = new Newspaper(volume, issue, day, month, year, false);
		System.out.println("If you want to enter editing pages mode, type anything but 'q'. If you want to exit editing mode, enter 'q'.");
		String next = scan.next();
		String added = "";
		while(!next.equals("q"))
		{
			String temp = editPaper(toAdd,clearance);
			if(temp!=null)
			{
				added= added+", "+temp;
			}
		}
		scan.close();
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
		String builder = + volume + ", " + issue + ", " + day + ", " + month + ", " + year + ", " + false + ", " + added;
		BufferedWriter write;
		try {
			write = new BufferedWriter (new FileWriter("../Database/NewspaperPages/NewspaperInit.txt",true));
			write.append(builder);
			write.close();
		} catch (IOException e) {
			System.out.println("Article initialization file corrupted or missing. Please contact tech support.");
			return null;
		}
		scan.close();
		return toAdd;
	}
	private boolean init()
	{
		Scanner scan;
		try {
			File f = new File("./newspaper/Database/NewspaperPages/NewspaperInit.txt");
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
				toAdd.pages.add(in[i]);
			}
			boolean finalized=in[5].compareTo("true")==0;
			if(finalized)
			{
				toAdd.finalizePaper(40);
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
		}
		scan.close();
		return true;
	}
	private String editPaper(Newspaper n,int clearance)
	{
		if(n.getPublished())
		{
			System.out.println("Paper has already been finalized.");
			return null;
		}
		if(clearance>3)
		{
			System.out.println("Please choose a page between 1 and " + (n.pages.size()-1) + " to edit a specific page, or 'a' to add to the end of the newspaper.");
			Scanner in = new Scanner(System.in);
			String input = in.next();
			if (input.equals("a"))
			{
				System.out.println("Insert the address for your new page. (.png only)");
				input = in.next();
				ImageIcon image = new ImageIcon(input);
				Image between = image.getImage();
				BufferedImage image2 = (BufferedImage)between;
				String[] list = input.split("/");
				File outputFile = new File("../Database/NewspaperPages/"+list[list.length-1]);
				try {
					ImageIO.write(image2, "png", outputFile);
					in.close();
					return "../Database/NewspaperPages/"+list[list.length-1];
				} catch (IOException e) {
					System.out.println("Image could not be found and/or was not in the correct format.");
					in.close();
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
				if(converted>n.pages.size()||converted<1)
				{
					System.out.println("Would you like to delete ('d') the page or replace it ('r')?");
					input=in.next();
					if(input.equals("r"))
					{
						String address = n.pages.get(converted-1);
						System.out.println("Insert the address for your replacement page. (.png only)");
						input = in.next();
						ImageIcon image = new ImageIcon(input);
						Image between = image.getImage();
						BufferedImage image2 = (BufferedImage)between;
						File outputFile = new File(address);
						try {
							ImageIO.write(image2, "png", outputFile);
							in.close();
							return address;
						} catch (IOException e) {
							System.out.println("Image could not be found and/or was not in the correct format.");
							in.close();
							return null;
						}
					}
					else if(input.equals("d"))
					{
						String removed = n.pages.remove(converted-1);
						System.out.println("Page was successfully removed.");
						in.close();
						return removed;
					}
					else
					{
						System.out.println("Paper has already been finalized.");
						in.close();
						return null;
					}
				}
				else
				{
					System.out.println("Inputed number out of range of current pages. Ending edit session.");
					in.close();
					return null;
				}
			}
		}
		return null;
	}
}