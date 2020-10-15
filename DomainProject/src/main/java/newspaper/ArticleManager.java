package newspaper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
/**
 * 
 * @authors Alexander Irlbeck and Jonah Armstrong.
 *
 */
public class ArticleManager 
{
	/**
	 * Key is the name of the article.
	 */
	private HashMap<String,Article> nameToAll;
	/**
	 * Key is the "volume, issue" of the article.
	 */
	private HashMap<String,ArrayList<Article>> volumeAndIssueToName;
	/**
	 * Key is the publishing date of the article in the form of "day, month, year".
	 */
	private HashMap<String,ArrayList<Article>> dayMonYearToName;
	/**
	 * A list of all articles in the database.
	 */
	private ArrayList<Article> articles;
	public ArticleManager()
	{
		nameToAll=new HashMap<String, Article>();
		volumeAndIssueToName=new HashMap<String, ArrayList<Article>>();
		dayMonYearToName=new HashMap<String, ArrayList<Article>>();
		articles = new ArrayList<Article>();
		init();
	}
	public Article[] search()
	{
		System.out.println("Enter '1' to look for an article by its title, '2' to look for an article by its description,");
		System.out.println("'3' to look for an article based off of it volume/issue appearance, or '4' to look up an article");
		System.out.println("based off of its publishing date.");
		@SuppressWarnings("resource")//System.in should not be closed before the program has finished.
		Scanner in = new Scanner(System.in);
		boolean going = true;
		while(going)
		{
			going=false;
			int num = 0;
			String next = in.nextLine();
			try
			{
				num = Integer.parseInt(next);
			}
			catch(Exception NumberFormatException)
			{
				System.out.println(next+" is not a valid number. Cancelling current search.");
				return null;
			}
			if(num==1)
			{
				System.out.println("Please enter the article's name.");
				String input = in.nextLine();
				if(!nameToAll.containsKey(input))
				{
					System.out.println("Title could not be found. Press '1' to search again or select another searching method.");
					going=true;
				}
				else
				{
					Article[] retual= new Article[1];
					retual[0]=nameToAll.get(input);
					return retual;
				}
			}
			else if(num==2)
			{
				System.out.println("Please enter the article's description.");
				String input = in.nextLine();
				ArrayList<Article> inBetween = new ArrayList<Article>();
				for(int i = 0; i<articles.size();i++)
				{
					int closeness=0;
					String cur = articles.get(i).getDesc();
					for(int j = 0; j<cur.length(); j++)
					{
						if(input.charAt(j)==cur.charAt(j))
						{
							closeness++;
						}
					}
					if(closeness > cur.length()/2)
					{
						inBetween.add(articles.get(i));
					}
				}
				if(inBetween.size()<1)
				{
					System.out.println("The description could not be found to reasonable matching. Press '2' to search again or select another searching method.");
					going=true;
				}
				else
				{
					Article[] retual= new Article[inBetween.size()];
					for(int i = 0; i<retual.length; i++)
					{
						retual[i]=inBetween.get(i);
					}
					return retual;
				}
			}
			else if(num==3)
			{
				System.out.println("Please enter the article's volume and issue, seperated by a '/' with no spaces.");
				String input = in.nextLine();
				String volIss= "";
				String[] temp = input.split("/");
				volIss = volIss+temp[0]+", ";
				volIss = volIss+temp[1];
				if(!volumeAndIssueToName.containsKey(volIss))
				{
					System.out.println("Volume and issue could not be found. Press '3' to search again or select another searching method.");
					going=true;
				}
				else
				{
					Article[] retual= new Article[volumeAndIssueToName.get(volIss).size()];
					for(int i = 0; i<retual.length; i++)
					{
						retual[i]=volumeAndIssueToName.get(volIss).get(i);
					}
					return retual;
				}
			}
			else if(num==4)
			{
				System.out.println("Please enter the article's publishing date, exactly as follows: 'month/day/year'.");
				String input = in.nextLine();
				String date= "";
				String[] temp = input.split("/");
				date = date+temp[1]+", ";
				date = date+temp[0]+", ";
				date = date+temp[2]+", ";
				if(!dayMonYearToName.containsKey(date))
				{
					System.out.println("The date could not be found. Press '4' to search again or select another searching method.");
					going=true;
				}
				else
				{
					Article[] retual= new Article[dayMonYearToName.get(date).size()];
					for(int i = 0; i<retual.length; i++)
					{
						retual[i]=dayMonYearToName.get(date).get(i);
					}
					return retual;
				}
			}
			else
			{
				System.out.println("The number inputed is not in the the approved options. Please an acceptable number.");
				going=true;
			}
		}
		return null;
	}
	public boolean addArticle()
	{
		System.out.println("Please enter your the path to the article you want to add.");
		@SuppressWarnings("resource")//System.in should not be closed before the program has finished.
		Scanner scan = new Scanner(System.in);
		String fileName = scan.nextLine();
		String[] split = fileName.split("/");
		File f = new File(fileName);
		Scanner fileCopy;
		try {
			fileCopy = new Scanner(f);
		} catch (FileNotFoundException e) {
			System.out.println("File could not be found. Ending add article process.");
			return false;
		}
		ArrayList<String> lines = new ArrayList<String>();
		while(fileCopy.hasNextLine())
		{
			lines.add(fileCopy.nextLine());
		}
		fileCopy.close();
		String path = "../Database/Articles/"+split[split.length-1];
		try {
			BufferedWriter write = new BufferedWriter (new FileWriter(path));
			write.write(lines.remove(0));
			write.close();
			write = new BufferedWriter (new FileWriter(path,true));
			while (lines.size()>0)
			{
				write.append(lines.remove(0));
			}
			write.close();
		} catch (IOException e) {
			System.out.println("Error in creating file occured. Contact tech support.");
			return false;
		}
		System.out.println("Insert the name of the article.");
		String name = scan.nextLine();
		System.out.println("Insert the description.");
		String desc = scan.nextLine();
		System.out.println("Insert the volume.");
		int volume = 0;
		String next = scan.nextLine();
		try
		{
			volume = Integer.parseInt(next);
		}
		catch(Exception NumberFormatException)
		{
			System.out.println(next+" is not a valid number. Ending add.");
			return false;
		}
		System.out.println("Insert the issue.");
		int issue = 0;
		next=scan.nextLine();
		try
		{
			issue = Integer.parseInt(next);
		}
		catch(Exception NumberFormatException)
		{
			System.out.println(next+" is not a valid number. Ending add.");
			return false;
		}
		System.out.println("Insert the day.");
		int day = 0;
		next=scan.nextLine();
		try
		{
			day = Integer.parseInt(next);
		}
		catch(Exception NumberFormatException)
		{
			System.out.println(next+" is not a valid number. Ending add.");
			return false;
		}
		System.out.println("Insert the month.");
		int month =0;
		next=scan.nextLine();
		try
		{
			month = Integer.parseInt(next);
		}
		catch(Exception NumberFormatException)
		{
			System.out.println(next+" is not a valid number. Ending add.");
			return false;
		}
		System.out.println("Insert the year.");
		int year = 0;
		next=scan.nextLine();
		try
		{
			year = Integer.parseInt(next);
		}
		catch(Exception NumberFormatException)
		{
			System.out.println(next+" is not a valid number. Ending add.");
			return false;
		}
		Article toAdd = new UnfinishedArticle(name, desc, volume, issue, day, month, year, false, path);
		articles.add(toAdd);
		nameToAll.put(name, toAdd);
		String temp = volume + ", " + issue;
		if(volumeAndIssueToName.containsKey(temp))
		{
			if(volumeAndIssueToName.get(temp).contains(toAdd))
			{
				System.out.println("Article is already in the system.");
				return false;
			}
			else
			{
				volumeAndIssueToName.get(temp).add(toAdd);
			}
		}
		else
		{
			ArrayList<Article> tempArr = new ArrayList<Article>();
			tempArr.add(toAdd);
			volumeAndIssueToName.put(temp,tempArr);
		}
		temp=day+", "+month+", "+year;
		if(dayMonYearToName.containsKey(temp))
		{
			if(dayMonYearToName.get(temp).contains(toAdd))
			{
				System.out.println("Article is already in the system.");
				return false;
			}
			else
			{
				dayMonYearToName.get(temp).add(toAdd);
			}
		}
		else
		{
			ArrayList<Article> tempArr = new ArrayList<Article>();
			tempArr.add(toAdd);
			dayMonYearToName.put(temp,tempArr);
		}
		String builder = "\n"+name + ", " + desc + ", " + volume + ", " + issue + ", " + day + ", " + month + ", " + year + ", " + false + ", " + path;
		try {
			BufferedWriter write = new BufferedWriter (new FileWriter("../Database/Articles/ArticlesInit.txt",true));
			write.append(builder);		
			write.close();
		} catch (IOException e) {
			System.out.println("Error occured in adding page to the init file. Please contact technical support.");
			return false;
		}
		fileCopy.close();
		return true;
	}
	private boolean init()
	{
		File f = new File("../Database/Articles/ArticlesInit.txt");
		Scanner scan;
		try {
			scan = new Scanner(f);
		} catch (FileNotFoundException e) {
			System.out.println("Article initialization file corrupted or missing. Please contact tech support.");
			return false;
		}
		while(scan.hasNextLine())
		{
			String line = scan.nextLine();
			String[] in = line.split(", ");
			String name = in[0];
			String desc = in[1];
			int volume = Integer.parseInt(in[2]);
			int issue = Integer.parseInt(in[3]);
			int day = Integer.parseInt(in[4]);
			int month = Integer.parseInt(in[5]);
			int year = Integer.parseInt(in[6]);
			boolean finalized=in[7].compareTo("true")==0;
			String path = in[8];
			Article toAdd;
			if(finalized)
			{
				toAdd=new FinishedArticle(name, desc, volume, issue, day, month, year, finalized, path);
			}
			else
			{
				toAdd=new UnfinishedArticle(name, desc, volume, issue, day, month, year, finalized, path);
			}
			nameToAll.put(in[0],toAdd);
			String volumeAndIssue = volume+", "+issue;
			String date = day+", "+month+", "+year;
			articles.add(toAdd);
			if(volumeAndIssueToName.containsKey(volumeAndIssue))
			{
				volumeAndIssueToName.get(volumeAndIssue).add(toAdd);
			}
			else
			{
				ArrayList<Article> temp = new ArrayList<Article>();
				temp.add(toAdd);
				volumeAndIssueToName.put(volumeAndIssue, temp);
			}
			if(dayMonYearToName.containsKey(date))
			{
				dayMonYearToName.get(volumeAndIssue).add(toAdd);
			}
			else
			{
				ArrayList<Article> temp = new ArrayList<Article>();
				temp.add(toAdd);
				dayMonYearToName.put(date, temp);
			}
		}

		scan.close();
		return true;
	}
}
