package newspaper;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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
		this.finalized = true;
		return true;
	}

	@Override
	public boolean editArticle(int clearance) {
		try {
			Desktop.getDesktop().edit(new File(this.path()));
		} catch (IOException e) {
			System.out.println("File could not be eddited. Please contact us at newspaperHelp@gmail.com.");
		}
		return false;
	}
}
