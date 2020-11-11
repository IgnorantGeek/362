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
import newspaper.ui.Command;

/**
 * A class that represents messages to the editor.
 * @author Alexander Irlbeck
 * Works as of 11/1/20
 */
public class DMEditor implements Commandable
{
	/**
	 * The key is the name of the article, with the data portion being the message.
	 */
	private HashMap<String,String> articleMessages;
	/**
	 * The key is the "volume_issue" of the newspaper, with the data portion being the message.
	 */
	private HashMap<String,String> newspaperMessages;
	
	/**
	 * Default constructor
	 * Works as of 11/1/20
	 */
	public DMEditor()
	{
		articleMessages = new HashMap<String,String>();
		newspaperMessages = new HashMap<String,String>();
		init();
	}

	/**
	 * Allows the user to send a message to the editor.
	 * @param article the article to get commented on
	 * @return whether or not it succeeded
	 * Works as of 11/1/20
	 */
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
			write = new BufferedWriter (new FileWriter("../Database/MessagesInit.txt",true));
			write.append(builder);
			write.close();
		} catch (IOException e) {
			System.out.println("Feedback initialization file corrupted or missing. Please contact tech support.");
			return false;
		}
		System.out.println("Thank You!");
		return true;
	}
	/**
	 * Adds a paper message to editor.
	 * @param paper to add to
	 * @return whether or not this succeeded
	 * Works as of 11/1/20
	 */
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
			write = new BufferedWriter (new FileWriter("../Database/MessagesInit.txt",true));
			write.append(builder);
			write.close();
		} catch (IOException e) {
			System.out.println("Feedback initialization file corrupted or missing. Please contact tech support.");
			return false;
		}
		System.out.println("Thank You!");
		return true;
	}
	
	/**
	 * Reads out all messages to the editor.
	 * @param clearance the clearance of the user
	 * @return whether or not the operation succeeded
	 * Works as of 11/1/20
	 */
	public boolean readAllComments(int clearance)
	{
		System.out.println("Here are all comments on Articles with its name.");
		String[] articleKeys = new String[articleMessages.size()];
		for (int i=0; i<articleMessages.size();i++)
		{
			articleKeys[i] = (String) articleMessages.keySet().toArray()[i];
		}
		for(int i = 0; i<articleKeys.length; i++)
		{
			System.out.println(articleKeys[i]);
			System.out.println(articleMessages.get(articleKeys[i]));
		}
		System.out.println("Here are all comments on Newspapers with its volume and issue.");
		String[] newspaperKeys = new String[newspaperMessages.size()];
		for (int i=0; i<newspaperMessages.size();i++)
		{
			newspaperKeys[i] = (String) newspaperMessages.keySet().toArray()[i];
		}
		for(int i = 0; i<newspaperKeys.length; i++)
		{
			System.out.println(newspaperKeys[i]);
			System.out.println(newspaperMessages.get(newspaperKeys[i]));
		}
		return true;
	}

	/**
	 * Removes a message to the editor, or multiple then saves it.
	 * @param clearance the clearance level of the user
	 * @return whether or not the operation succeeded
	 * Works as of 11/1/20
	 */
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
				String[] articleKeys = new String[articleMessages.size()];
				for (int i=0; i<articleMessages.size();i++)
				{
					articleKeys[i] = (String) articleMessages.keySet().toArray()[i];
				}
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
				String[] newspaperKeys = new String[newspaperMessages.size()];
				for (int i=0; i<newspaperMessages.size();i++)
				{
					newspaperKeys[i] = (String) newspaperMessages.keySet().toArray()[i];
				}
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
				newspaperMessages.remove(newspaperKeys[spot]);
				save();
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
		return true;
	}
	
	/**
	 * Saves the work so far to the init file.
	 * Works as of 11/1/20
	 */
	private void save()
	{
		String builder="";
		String[] articleKeys = new String[articleMessages.size()];
		for (int i=0; i<articleMessages.size();i++)
		{
			articleKeys[i] = (String) articleMessages.keySet().toArray()[i];
		}
		String[] newspaperKeys = new String[newspaperMessages.size()];
		for (int i=0; i<newspaperMessages.size();i++)
		{
			newspaperKeys[i] = (String) newspaperMessages.keySet().toArray()[i];
		}
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

	/**
	 * Initializes the messages to the editor from its file.
	 * @return whether or not the operation succeeded
	 * Works as of 11/1/20
	 */
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

	@Override
	public String executeCommand(Command command)
	{
		// TODO
		return null;
	}
}
