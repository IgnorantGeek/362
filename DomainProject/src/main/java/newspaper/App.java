package newspaper;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
		NewspaperManager man = new NewspaperManager();
		man.search().readPaper(7);
    }
}
