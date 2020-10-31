package newspaper.managers;

import java.io.BufferedWriter;
import java.io.File;
import newspaper.models.Article;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.HashMap;
import newspaper.models.Newspaper;

public class DMEditor {
	/**
	 * The key is the name of the article, with the data portion being the message.
	 */
	private HashMap<String,String> articleMessages;
	/**
	 * The key is the "volume_issue" of the newspaper, with the data portion being the message.
	 */
	private HashMap<String,String> newspaperMessages;

	public DMEditor()
	{
		articleMessages = new HashMap<String,String>();
		newspaperMessages = new HashMap<String,String>();
		init();
	}

	public boolean addArticleComment(Article article)
	{
		System.out.println("Please enter your message here. Enter 'q' without anthing else on the line to quit.");
		@SuppressWarnings("resource")
		Scanner scan = new Scanner(System.in);
		String message = "";
		String in = scan.nextLine();
		while(in.compareToIgnoreCase("q")!=0)
		{
			message = message + in + "\n";
			in = scan.nextLine();
		}
		if(articleMessages.containsKey(article.getName()))
		{
			String old = articleMessages.get(article.getName());
			old = old + "\n\n" + message;
			articleMessages.replace(article.getName(), old);
		}
		else
		{
			articleMessages.put(article.getName(), message);
		}
		String builder="";
		builder = builder + "Article\n"
				+ article.getName() + "\n"
				+ message;
		BufferedWriter write;
		try {
			write = new BufferedWriter (new FileWriter("../Database/MessagesInit.txt"));
			write.append(builder);
			write.close();
		} catch (IOException e) {
			System.out.println("Feedback initialization file corrupted or missing. Please contact tech support.");
			return false;
		}
		System.out.println("Thank You!");
		return true;
	}

	public boolean addPaperComment(Newspaper paper)
	{
		System.out.println("Please enter your message here. Enter 'q' without anthing else on the line to quit.");
		@SuppressWarnings("resource")
		Scanner scan = new Scanner(System.in);
		String message = "";
		String in = scan.nextLine();
		while(in.compareToIgnoreCase("q")!=0)
		{
			message = message + in + "\n";
			in = scan.nextLine();
		}
		String id = paper.getInfo()[0]+"_"+paper.getInfo()[1];
		if(newspaperMessages.containsKey(id))
		{
			String old = newspaperMessages.get(id);
			old = old + "\n\n" + message;
			newspaperMessages.replace(id, old);
		}
		else
		{
			newspaperMessages.put(id, message);
		}
		String builder="";
		builder = builder + "Newspaper\n"
				+ id + "\n"
				+ message;
		BufferedWriter write;
		try {
			write = new BufferedWriter (new FileWriter("../Database/MessagesInit.txt"));
			write.append(builder);
			write.close();
		} catch (IOException e) {
			System.out.println("Feedback initialization file corrupted or missing. Please contact tech support.");
			return false;
		}
		System.out.println("Thank You!");
		return true;
	}

	public boolean readAllComments(int clearance)
	{
		System.out.println("Here are all comments on Articles with its name.");
		String[] articleKeys = (String[]) articleMessages.keySet().toArray();
		for(int i = 0; i<articleKeys.length; i++)
		{
			System.out.println(articleKeys[i]);
			System.out.println(articleMessages.get(articleKeys[i]));
		}
		System.out.println("Here are all comments on Newspapers with its volume and issue.");
		String[] newspaperKeys = (String[]) newspaperMessages.keySet().toArray();
		for(int i = 0; i<newspaperKeys.length; i++)
		{
			System.out.println(newspaperKeys[i]);
			System.out.println(newspaperMessages.get(newspaperKeys[i]));
		}
		return true;
	}

	public boolean removeComments(int clearance)
	{
		@SuppressWarnings("resource")
		Scanner scan = new Scanner(System.in);
		while(true)
		{
			System.out.println("If you want to delete a Article type 'a'. If you want to delete a Newspaper type 'n'.");
			System.out.println("If you want to stop deleting comments, enter 'q'.");
			String choice = scan.nextLine();
			if(choice.compareToIgnoreCase("a")==0)
			{
				System.out.println("Choose the number corresponding with the article name.");
				String[] articleKeys = (String[]) articleMessages.keySet().toArray();
				for(int i = 0; i<articleKeys.length; i++)
				{
					System.out.println(i+" "+articleKeys[i]);
				}
				String arrChoice = scan.nextLine();
				int spot = -1;
				try
				{
					spot = Integer.parseInt(arrChoice);
				}
				catch(Exception NumberFormatException)
				{
					System.out.println(arrChoice+" could not be recognized as an integer. Restarting sequence.");
					continue;
				}
				if(spot<0||spot>articleKeys.length-1)
				{
					System.out.println(spot+" is out of range of available choices. Restarting sequence.");
					continue;
				}
				articleMessages.remove(articleKeys[spot]);
				save();
			}
			else if(choice.compareToIgnoreCase("n")==0)
			{
				System.out.println("Choose the number corresponding with the newspapers volume and issue.");
				String[] newspaperKeys = (String[]) newspaperMessages.keySet().toArray();
				for(int i = 0; i<newspaperKeys.length; i++)
				{
					System.out.println(i+" "+newspaperKeys[i]);
				}
				String arrChoice = scan.nextLine();
				int spot = -1;
				try
				{
					spot = Integer.parseInt(arrChoice);
				}
				catch(Exception NumberFormatException)
				{
					System.out.println(arrChoice+" could not be recognized as an integer. Restarting sequence.");
					continue;
				}
				if(spot<0||spot>newspaperKeys.length-1)
				{
					System.out.println(spot+" is out of range of available choices. Restarting sequence.");
					continue;
				}
				articleMessages.remove(newspaperKeys[spot]);
			}
			else if(choice.compareToIgnoreCase("q")==0)
			{
				break;
			}
			else
			{
				System.out.println(choice+" is not recognized.");
			}
		}
		save();
		return true;
	}
	
	private void save()
	{
		String builder="";
		String[] articleKeys = (String[]) articleMessages.keySet().toArray();
		String[] newspaperKeys = (String[]) newspaperMessages.keySet().toArray();
		for(int i=0;i<articleKeys.length;i++)
		{
			builder = builder + "Article\n"
					          + articleKeys[i] + "\n"
					          + articleMessages.get(articleKeys[i]) + "\n";
		}
		for(int i=0;i<newspaperKeys.length;i++)
		{
			builder = builder + "Newspaper\n"
					          + newspaperKeys[i] + "\n"
					          + newspaperMessages.get(newspaperKeys[i]) + "\n";
		}
		BufferedWriter write;
		try {
			write = new BufferedWriter (new FileWriter("../Database/MessagesInit.txt"));
			write.write(builder);
			write.close();
		} catch (IOException e) {
			System.out.println("Messages initialization file corrupted or missing. Please contact tech support.");
		}
	}

	private boolean init()
	{
		File f = new File("../Database/MessagesInit.txt");
		Scanner scan;
		try {
			scan = new Scanner(f);
		} catch (FileNotFoundException e) {
			System.out.println("Messages initialization file corrupted or missing. Please contact tech support.");
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
			String type;
			String identifier;
			String message = "";
			type = lines.remove(0);
			if(type.compareTo("Article")==0)
			{
				if(lines.size()>0)
				{
					identifier = lines.remove(0);
					if(lines.size()>0)
					{
						String cur = lines.remove(0);
						if(articleMessages.containsKey(identifier))
						{
							message = articleMessages.get(identifier) + "\n\n";
						}
						while (cur.compareTo("Article")!=0&&cur.compareTo("Newspaper")!=0&&lines.size()>0)
						{
							message = message + cur +"\n";
							cur = lines.remove(0);
						}
					}
					else
					{
						System.out.println("Messages initialization file corrupted. Please contact tech support.");
						scan.close();
						return false;
					}
				}
				else
				{
					System.out.println("Messages initialization file corrupted. Please contact tech support.");
					scan.close();
					return false;
				}
				if(articleMessages.containsKey(identifier))
				{
					articleMessages.replace(identifier, message);
				}
				else
				{
					articleMessages.put(identifier, message);
				}
			}
			else if(type.compareTo("Newspaper")==0)
			{
				if(lines.size()>0)
				{
					identifier = lines.remove(0);
					if(lines.size()>0)
					{
						String cur = lines.remove(0);
						if(newspaperMessages.containsKey(identifier))
						{
							message = newspaperMessages.get(identifier) + "\n\n";
						}
						while (cur.compareTo("Article")!=0&&cur.compareTo("Newspaper")!=0&&lines.size()>0)
						{
							message = message + cur +"\n";
							cur = lines.remove(0);
						}
					}
					else
					{
						System.out.println("Messages initialization file corrupted. Please contact tech support.");
						scan.close();
						return false;
					}
				}
				else
				{
					System.out.println("Messages initialization file corrupted. Please contact tech support.");
					scan.close();
					return false;
				}
				if(newspaperMessages.containsKey(identifier))
				{
					newspaperMessages.replace(identifier, message);
				}
				else
				{
					newspaperMessages.put(identifier, message);
				}
			}
		}
		scan.close();
		return true;
	}
}
