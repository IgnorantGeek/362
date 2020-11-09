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
import newspaper.models.Article;
import newspaper.models.Newspaper;

public class Retract {
	private HashMap<String,Article> articleRetractions;
	private HashMap<String,Newspaper> newspaperRetractions;
	public Retract()
	{
		articleRetractions = new HashMap<String,Article>();
		newspaperRetractions = new HashMap<String,Newspaper>();
		init();
	}
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
		//TODO
		return true;
	}
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
	private boolean init()
	{
		//TODO
		return false;
	}
}
