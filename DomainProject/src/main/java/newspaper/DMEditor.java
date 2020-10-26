package newspaper;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.HashMap;

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
			String message;
			type = lines.remove(0);
			if(type.compareTo("Article")==0)
			{
				if(lines.size()>0)
				{
					identifier = lines.remove(0);
					if(lines.size()>0)
					{
						message = lines.remove(0);
						if(articleMessages.containsKey(identifier))
						{
							String oldMessages = articleMessages.get(identifier);
							oldMessages = oldMessages +"\n\n"+ message;
							articleMessages.replace(identifier, oldMessages);
						}
						else
						{
							articleMessages.put(identifier, message);
						}
					}
					else
					{
						System.out.println("Messages initialization file corrupted. Please contact tech support.");
						return false;
					}
				}
				else
				{
					System.out.println("Messages initialization file corrupted. Please contact tech support.");
					return false;
				}
			}
			else if(type.compareTo("Newspaper")==0)
			{
				if(lines.size()>0)
				{
					identifier = lines.remove(0);
					if(lines.size()>0)
					{
						message = lines.remove(0);
						if(newspaperMessages.containsKey(identifier))
						{
							String oldMessages = newspaperMessages.get(identifier);
							oldMessages = oldMessages +"\n\n"+ message;
							newspaperMessages.replace(identifier, oldMessages);
						}
						else
						{
							newspaperMessages.put(identifier, message);
						}
					}
					else
					{
						System.out.println("Messages initialization file corrupted. Please contact tech support.");
						return false;
					}
				}
				else
				{
					System.out.println("Messages initialization file corrupted. Please contact tech support.");
					return false;
				}
			}
		}
		scan.close();
		return true;
	}
}
