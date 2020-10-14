package newspaper;

import java.io.IOException;
import java.util.Scanner;

/**
 * Handle the IOException properly
 */
public class App
{
    public static void main( String[] args ) throws IOException
    {
        NewspaperManager man = new NewspaperManager();
        ArticleManager aman = new ArticleManager();
        AdManager adManager = new AdManager("../Database", man);

        Scanner in = new Scanner(System.in);

        System.out.println("Welcome. Enter the number of the sytem to work with:");
        System.out.println("1: NewspaperManager\n2: ArticleManager\n3: AdManager");

        String input = in.nextLine();

        switch(input)
        {
            case "1":
                // Alex this will be the main entry point for newspaperManager
                man.search();
                break;
            case "2":
                aman.addArticle();
                break;
            case "3":
                adManager.run();
                break;
            case "q":
            case "quit":
            case "Quit":
            case "exit":
            case "Exit":
                System.out.println("Goodbye.");
                return;
        }

        in.close();
    }
}
