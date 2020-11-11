package newspaper;

import newspaper.models.Employee;
import newspaper.ui.CommandProcessor;

import java.util.Scanner;

public class Main
{
    public static void main(String[] args)
    {
        CommandProcessor cp = new CommandProcessor();
        Scanner in = new Scanner(System.in);

        Global.flushConsole();
        System.out.println("Welcome to the FakeNews! NewsPaper Management System.\n");
        // Login loop
        while (true)
        {
            // Print welcome message
            System.out.print("User ID: ");

            String loginIdStr = in.nextLine();
            int userId;

            if (loginIdStr.compareTo("quit") == 0
            ||  loginIdStr.compareTo("q") == 0
            ||  loginIdStr.compareTo("Quit") == 0
            ||  loginIdStr.compareTo("QUIT") == 0)
            {
                System.out.println("Goodbye.");
                break;
            }

            // Parse user ID number
            try
            {
                userId = Integer.parseInt(loginIdStr);
            }
            catch (NumberFormatException e)
            {
                // not a number error
                Global.flushConsole();
                System.out.println("Value entered is not a number. Enter a valid user id, or 'q' to exit the system");
                continue;
            }

            System.out.print("Password: ");
            String password = in.nextLine();

            Employee loggedIn = cp.eman.validateLogin(userId, password);

            if (loggedIn != null)
            {
                System.out.println("Welcome, " + loggedIn.FullName() + ". What would you like to do?");
            }
            else
            {
                Global.flushConsole();
                System.out.println("Invalid Login, User ID/Password incorrect.");
            }
        }

    }
}
