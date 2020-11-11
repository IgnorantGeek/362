package newspaper.ui;

import newspaper.managers.*;

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
        String[] cmdArr = commandStr.split(" ");
        String out;

        // Check for required manager and command input
        if (cmdArr.length < 2)
        {
            return "Error. Not enough arguments.\nRequired format:\n<manager> <command> <options>*";
        }

        // Create a command object
        String subCmd = cmdArr[1];
        String[] options = null;
        if (cmdArr.length > 2) options = Arrays.copyOfRange(cmdArr, 2, cmdArr.length - 1);

        Command command = new Command(subCmd, options);
        switch(cmdArr[0])
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
                out = "No command found for " + cmdArr[0];
        }
        return out;
    }
}
