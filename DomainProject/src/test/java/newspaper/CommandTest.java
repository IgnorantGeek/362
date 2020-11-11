package newspaper;

import newspaper.ui.CommandProcessor;

public class CommandTest
{
    public static void main(String[] args)
    {
        CommandProcessor cp = new CommandProcessor();

        System.out.println(cp.processCommand("Test"));
        System.out.println("Working Directory = " + System.getProperty("user.dir"));
    }
}
