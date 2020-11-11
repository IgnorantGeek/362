package newspaper.ui;

import newspaper.managers.*;

import java.util.ArrayList;
import java.util.Arrays;

public class CommandProcessor
{
    private NewspaperManager nman = new NewspaperManager();
    private ArticleManager aman = new ArticleManager();
    private AdManager adman = new AdManager();
    private SubscriptionManager sman = new SubscriptionManager();
    private DistributionManager dman = new DistributionManager();
    private DMEditor dme = new DMEditor();
    private Feedback fback = new Feedback();
    public EmployeeManager eman = new EmployeeManager(10); // Reserve ids 0-9 for testing, public for login
    private FinancialManager fman = new FinancialManager(eman,adman,sman,dman);
    private Report rport = new Report(eman);
    private Retract ract = new Retract(nman, aman);

    public CommandProcessor() { }

    public String processCommand(String commandStr)
    {
        // Param check
        if (commandStr == null
        ||  commandStr.compareTo("") == 0)
        {
            return "Error. Empty input string";
        }
        // Split the strings by spaces
        ArrayList<String> cmdArr = commandSplit(commandStr);
        String out;

        // Check for required manager and command input
        if (cmdArr.size() < 2)
        {
            return "Error. Not enough arguments.\nRequired format:\n<manager> <command> <options>*";
        }

        // Create a command object
        String manCmd = cmdArr.get(0);
        String subCmd = cmdArr.get(1);

        // Pop two off top of arrayList
        cmdArr.remove(0);
        cmdArr.remove(0);

        Command command = new Command(subCmd, cmdArr);
        switch(manCmd)
        {
            case "newspaper":
                out = nman.executeCommand(command);
                break;
            case "article":
                out = aman.executeCommand(command);
                break;
            case "advert":
                out = adman.executeCommand(command);
                break;
            case "subscription":
                out = sman.executeCommand(command);
                break;
            case "distributor":
                out = dman.executeCommand(command);
                break;
            case "dm":
                out = dme.executeCommand(command);
                break;
            case "feedback":
                out = fback.executeCommand(command);
                break;
            case "employee":
                out = eman.executeCommand(command);
                break;
            case "finance":
                out = fman.executeCommand(command);
                break;
            case "report":
                out = rport.executeCommand(command);
                break;
            case "react":
                out = ract.executeCommand(command);
                break;
            default:
                out = "No command found for " + manCmd;
        }
        return out;
    }

    private ArrayList<String> commandSplit(String commandStr)
    {
        ArrayList<String> out = new ArrayList<>();
        StringBuilder build = new StringBuilder();
        boolean quoteOpen = false;

        for (int i = 0; i < commandStr.length(); i++)
        {
            // Check for double or single quotes
            // Break by space except inside quotes
            if (commandStr.charAt(i) == '"'
            ||  commandStr.charAt(i) == '\'')
            {
                quoteOpen = !quoteOpen;
            }
            else if (commandStr.charAt(i) == ' ')
            {
                if (quoteOpen) build.append(commandStr.charAt(i));
                else
                {
                    out.add(build.toString());
                    build = new StringBuilder();
                }
            }
            else build.append(commandStr.charAt(i));
        }

        out.add(build.toString());
        return out;
    }
}
