package newspaper.managers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import newspaper.models.Article;
import newspaper.models.Newspaper;
import newspaper.ui.Command;

/**
 * The class that handles retractions of articles and newspapers.
 * @author Alexander Irlbeck
 * Works as of 11/10/20
 */
public class Retract implements Commandable
{
	/**
	 * The list of all retracted articles.
	 */
	private HashMap<String,Article> articleRetractions;
	/**
	 * The list of all retracted newspapers.
	 */
	private HashMap<String,Newspaper> newspaperRetractions;
	/**
	 * Constructor for the retract class.
	 * @param papers The app.java's newspaper manager
	 * @param arts The app.java's article manager
	 * Works as of 11/10/20
	 */
	public Retract(NewspaperManager papers, ArticleManager arts)
	{
		articleRetractions = new HashMap<String,Article>();
		newspaperRetractions = new HashMap<String,Newspaper>();
		init(papers,arts);
	}
	/**
	 * Allows the user to retract an article or newspaper.
	 * @param clearance The user's clearance level
	 * @param papers The app.java's newspaper manager
	 * @param arts The app.java's article manager
	 * @return Whether or not the functions succeeded.
	 * Works as of 11/10/20
	 */
	public boolean retract(int clearance, NewspaperManager papers, ArticleManager arts)
	{
		if(clearance < 8)
		{
			System.out.println("You are not authorized to access this function.");
			return false;
		}
		@SuppressWarnings("resource")
		Scanner scan = new Scanner(System.in);
		System.out.println("Would you like to retract an article (a) or a newspaper (n). Enter 'q' to exit.");
		String in = scan.nextLine();
		while(in.compareToIgnoreCase("q")!=0)
		{
			if(in.compareToIgnoreCase("a")==0)
			{
				ArrayList<Article> list = arts.getArticles();
				for(int i = 0; i < list.size(); i++)
				{
					System.out.println(i+": "+list.get(i).getName());
				}
				System.out.println("Choose a number from above to retract an article.");
				String spot = scan.nextLine();
				int index;
				try
				{
					index = Integer.parseInt(spot);
				}
				catch(Exception NumberFormatException)
				{
					System.out.println(spot+" could not be interpretted as a number.");
					in = scan.nextLine();
					continue;
				}
				if(articleRetractions.containsKey(list.get(index).getName()))
				{
					System.out.println(list.get(index).getName()+" was already retracted.");
					in = scan.nextLine();
					continue;
				}
				articleRetractions.put(list.get(index).getName(), list.get(index));
				save();
			}
			else if(in.compareToIgnoreCase("n")==0)
			{
				HashMap<String, Newspaper> temp = papers.getVolIss();
				ArrayList<Newspaper> list = new ArrayList<Newspaper>();
				for(int i = 0; i < temp.keySet().size(); i++)
				{
					list.add(temp.get((String)temp.keySet().toArray()[i]));
				}
				for(int i = 0; i < list.size(); i++)
				{
					System.out.println(i+": "+list.get(i).getInfo()[0]+", "+list.get(i).getInfo()[1]);
				}
				System.out.println("Choose a number from above to retract a newspaper.");
				String spot = scan.nextLine();
				int index;
				try
				{
					index = Integer.parseInt(spot);
				}
				catch(Exception NumberFormatException)
				{
					System.out.println(spot+" could not be interpretted as a number.");
					in = scan.nextLine();
					continue;
				}
				if(newspaperRetractions.containsKey(list.get(index).getInfo()[0]+", "+list.get(index).getInfo()[1]))
				{
					System.out.println(list.get(index).getInfo()[0]+", "+list.get(index).getInfo()[1]+" was already retracted.");
					in = scan.nextLine();
					continue;
				}
				newspaperRetractions.put(list.get(index).getInfo()[0]+", "+list.get(index).getInfo()[1], list.get(index));
				save();
			}
			else
			{
				System.out.println(in+" could not be recognized as 'a' or 'n'");
			}
			in = scan.nextLine();
		}
		return true;
	}
	/**
	 * Returns the list of retracted articles.
	 * @return The list of retracted articles.
	 * Works as of 11/10/20
	 */
	public ArrayList<Article> getArticles()
	{
		ArrayList<Article> retual = new ArrayList<Article>();
		ArrayList<String> keys = new ArrayList<String>();
		for(int i =0; i < articleRetractions.size(); i++)
		{
			keys.add((String) articleRetractions.keySet().toArray()[i]);
		}
		while(keys.size()>0)
		{
			retual.add(articleRetractions.get(keys.remove(0)));
		}
		return retual;
	}
	/**
	 * Returns the list of retracted newspapers.
	 * @return The list of retracted newspapers.
	 * Works as of 11/10/20
	 */
	public ArrayList<Newspaper> getPapers()
	{
		ArrayList<Newspaper> retual = new ArrayList<Newspaper>();
		ArrayList<String> keys = new ArrayList<String>();
		for(int i =0; i < newspaperRetractions.size(); i++)
		{
			keys.add((String) newspaperRetractions.keySet().toArray()[i]);
		}
		while(keys.size()>0)
		{
			retual.add(newspaperRetractions.get(keys.remove(0)));
		}
		return retual;
	}
	/**
	 * Saves the current changes to the retractions.
	 * @return Whether or not the functions succeeded.
	 * Works as of 11/10/20
	 */
	private boolean save()
	{
		String builder="";
		for(int i=0;i<articleRetractions.keySet().size();i++)
		{
			String cur = (String) articleRetractions.keySet().toArray()[i];
			builder = builder+"\n"+"Article"+"\n"+cur+"\n";
		}
		for(int i=0;i<newspaperRetractions.keySet().size();i++)
		{
			String cur = (String) newspaperRetractions.keySet().toArray()[i];
			builder = builder+"\n"+"Newspaper"+"\n"+cur+"\n";
		}
		BufferedWriter write;
		try {
			write = new BufferedWriter (new FileWriter("../Database/Retractions.txt"));
			write.write(builder);
			write.close();
		} catch (IOException e) {
			System.out.println("Retractions initialization file corrupted or missing. Please contact tech support.");
			return false;
		}
		return true;
	}
	/**
	 * Initializes the class from the retractions init file.
	 * @param papers The app.java's newspaper manager
	 * @param arts The app.java's article manager
	 * @return Whether or not the functions succeeded.
	 * Works as of 11/10/20
	 */
	private boolean init(NewspaperManager papers, ArticleManager arts)
	{
		File f = new File("../Database/Retractions.txt");
		Scanner scan;
		try {
			scan = new Scanner(f);
		} catch (FileNotFoundException e) {
			System.out.println("Retraction initialization file corrupted or missing. Please contact tech support.");
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
			String type = lines.remove(0);
			String id;
			if(lines.size()>0)
			{
				id = lines.remove(0);
				if(type.compareToIgnoreCase("article")==0)
				{
					Article a = arts.getArticle(id);
					if(a!=null)
					{
						articleRetractions.put(id,a);
					}
				}
				else if(type.compareToIgnoreCase("newspaper")==0)
				{
					Newspaper n = papers.getVolIss().get(id);
					if(n!=null)
					{
						newspaperRetractions.put(id,n);
					}
				}
				else
				{
					System.out.println("Retraction initialization file corrupted. Please contact tech support.");
					scan.close();
					return false;
				}
			}
			else
			{
				System.out.println("Retraction initialization file corrupted. Please contact tech support.");
				scan.close();
				return false;
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
