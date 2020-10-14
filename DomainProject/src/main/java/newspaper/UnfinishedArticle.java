package newspaper;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class UnfinishedArticle extends Article
{
    public UnfinishedArticle(String name, String desc, int volume, int issue, int day, int month, int year, boolean finalized, String path)
    {
        super(name, desc, volume, issue, day, month, year, finalized, path);
    }
    
    @Override
	public boolean readArticle(int clearance) {
		File f = new File(path());
		try {
			Scanner scan = new Scanner(f);
			while (scan.hasNextLine());
			{
				System.out.println(scan.nextLine());
			}
			scan.close();
			return true;
		} 
		catch (FileNotFoundException e) {
			System.out.println("Unknown error occured. Please contact us at newspaperHelp@gmail.com");
		}
		return false;
	}

	@Override
	public boolean finalizeArticle(int clearance) {
		System.out.println("This article has already been published.");
		return false;
	}

	@Override
	public boolean editArticle(int clearance) {
		System.out.println("This article has already been published. No further edits may be made.");
		return false;
	}
}
