package project;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class ArticleManager 
{
	/**
	 * Key is the name of the article.
	 */
	private HashMap<String,Article> nameToAll;
	private HashMap<int[],ArrayList<Article>> volumeAndIssueToName;
	private HashMap<int[],ArrayList<Article>> dayMonYearToName;
	private ArrayList<Article> articles;
	public ArticleManager()
	{
		nameToAll=new HashMap<String, Article>();
		volumeAndIssueToName=new HashMap<int[], ArrayList<Article>>();
		dayMonYearToName=new HashMap<int[], ArrayList<Article>>();
		articles = new ArrayList<Article>();
		init();
	}
	public Article[] search()
	{
		System.out.println("Enter '1' to look for an article by its title, '2' to look for an article by its description,");
		System.out.println("'3' to look for an article based off of it volume/issue appearance, or '4' to look up an article");
		System.out.println("based off of its publishing date.");
		Scanner in = new Scanner(System.in);
		boolean going = true;
		while(going)
		{
			going=false;
			int num = in.nextInt();
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
					in.close();
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
					in.close();
					return retual;
				}
			}
			else if(num==3)
			{
				System.out.println("Please enter the article's volume and issue, seperated by a '/' with no spaces.");
				String input = in.nextLine();
				int[] volIss= new int[2];
				String[] temp = input.split("/");
				volIss[0] = Integer.parseInt(temp[0]);
				volIss[1] = Integer.parseInt(temp[1]);
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
					in.close();
					return retual;
				}
			}
			else if(num==4)
			{
				System.out.println("Please enter the article's publishing date, exactly as follows: 'month/day/year'.");
				String input = in.nextLine();
				int[] date= new int[3];
				String[] temp = input.split("/");
				date[1] = Integer.parseInt(temp[1]);
				date[0] = Integer.parseInt(temp[0]);
				date[2] = Integer.parseInt(temp[2]);
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
					in.close();
					return retual;
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
	public boolean addArticle()
	{
		System.out.println("Please enter your the path to the article you want to add.");
		Scanner scan = new Scanner(System.in);
		String fileName = scan.next();
		String[] split = fileName.split("/");
		File f = new File(fileName);
		Scanner fileCopy;
		try {
			fileCopy = new Scanner(f);
		} catch (FileNotFoundException e) {
			System.out.println("File could not be found. Ending add article process.");
			scan.close();
			fileCopy.close();
			return false;
		}
		ArrayList<String> lines = new ArrayList<String>();
		while(fileCopy.hasNextLine())
		{
			lines.add(fileCopy.nextLine());
		}
		fileCopy.close();
		String path = "../Database/Articles/"+split[split.length-1];
		File out = new File(path);
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
			scan.close();
			return false;
		}
		System.out.println("Insert the volume.");
		int volume = scan.nextInt();
		System.out.println("Insert the issue.");
		int issue = scan.nextInt();
		System.out.println("Insert the day.");
		int day = scan.nextInt();
		System.out.println("Insert the month.");
		int month = scan.nextInt();
		System.out.println("Insert the year.");
		int year = scan.nextInt();
		System.out.println("Insert the name of the article.");
		String name = scan.nextLine();
		System.out.println("Insert the description.");
		String desc = scan.nextLine();
		Article toAdd = new UnfinishedArticle(name, desc, volume, issue, day, month, year, false, path);
		articles.add(toAdd);
		nameToAll.put(name, toAdd);
		int[] temp = new int[2];
		temp[0]=volume;
		temp[1]=issue;
		volumeAndIssueToName.get(temp).add(toAdd);
		temp=new int[3];
		temp[0]=day;
		temp[1]=month;
		temp[2]=year;
		dayMonYearToName.get(temp).add(toAdd);
		String builder = name + ", " + desc + ", " + volume + ", " + issue + ", " + day + ", " + month + ", " + year + ", " + false + ", " + path;
		BufferedWriter write = new BufferedWriter (new FileWriter("../Database/Articles/ArticlesInit.txt",true));
		write.append(builder);
		write.close();
		scan.close();
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
			int[] volumeAndIssue = new int[2];
			int[] date = new int[3];
			volumeAndIssue[0] = volume;
			volumeAndIssue[1] = issue;
			date[0]=day;
			date[1]=month;
			date[2]=year;
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
	}
}
