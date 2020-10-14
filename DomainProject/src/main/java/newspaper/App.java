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
                Newspaper paper=man.search();
                if (paper!=null)
                {
                	paper.readPaper(20);
                }
                paper=man.search();
                if (paper!=null)
                {
                	paper.readPaper(1);
                }
                paper=man.search();
                if (paper!=null)
                {
                	paper.finalizePaper(1);
                }
                paper=man.search();
                if (paper!=null)
                {
                	paper.finalizePaper(5);
                }
                paper=man.search();
                if (paper!=null)
                {
                	paper.readPaper(1);
                }
                paper=man.search();
                if (paper!=null)
                {
                	paper.readPaper(2);
                }
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
                in.close();
                return;
        }

        in.close();
    }
}
