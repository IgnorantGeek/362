package main.java.newspaper;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
/**
 * Represents a published article.
 * @author Alexander Irlbeck
 * **Works as of 10/14/20
 */
public class FinishedArticle extends Article 
{
	/**
	 * Assigns all the variables to be held. Ignores the finalized parameter because a finished article was finalized.
	 * @param name The title of the article
	 * @param desc The description of the article
	 * @param volume The volume the article appears in
	 * @param issue The issue the article appears in
	 * @param day The publishing day the article appears in
	 * @param month The publishing month the article appears in
	 * @param year The publishing year the article appears in
	 * @param finalized Whether or not the article was published
	 * @param path The path to the article in the Database
	 * **Works as of 10/14/20
	 */
	public FinishedArticle(String name, String desc, int volume, int issue, int day, int month, int year,boolean finalized, String path) 
	{
		super(name, desc, volume, issue, day, month, year, true, path);
	}

	@Override
	public boolean readArticle(int clearance) 
	{
		if(clearance>1)
		{
			File f = new File(path());
			try 
			{
				Scanner scan = new Scanner(f);
				while (scan.hasNextLine())
				{
					System.out.println(scan.nextLine());
				}
				scan.close();
				return true;
			} 
			catch (FileNotFoundException e) 
			{
				System.out.println("Unknown error occured. Please contact us at newspaperHelp@gmail.com");
			}
		}
		return false;
	}

	@Override
	public boolean finalizeArticle(int clearance) 
	{
		finalized=true;
		return true;
	}

	@Override
	/**
	 * A published article can not be edited.
	 */
	public boolean editArticle(int clearance) 
	{
		System.out.println("This article has already been published. No further edits may be made.");
		return false;
	}

}
