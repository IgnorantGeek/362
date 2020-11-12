package newspaper.managers;

import newspaper.Global;
import newspaper.models.Advertiser;
import newspaper.models.Customer;
import newspaper.models.Distributor;
import newspaper.models.Employee;
import newspaper.ui.Command;

import java.io.File;
import java.util.HashMap;
import java.util.Scanner;

public class CustomerManager implements Commandable
{
    private int customerCount = 0;
    private HashMap<Integer, Customer> registry;
    private String databasePath;
    EmployeeManager eman;

    public CustomerManager(EmployeeManager eman)
    {
        this.databasePath = Global.DISTRIBUTOR_DB_PATH;
        this.registry = new HashMap<>();
        this.eman = eman;
        this.init();
    }
    public int init()
    {
        // Create the customers database folder if it does not exist
        File customerFile = new File(databasePath);

        if (!customerFile.exists())
        {
            if (!customerFile.mkdir()) return -2;
            customerCount = 0;
        }
        else
        {
            customerCount = customerFile.list().length;
            for (String dFileName : customerFile.list())
            {
                Customer build = buildFromFile(dFileName);

                if (build == null)
                {
                    System.out.println("Customer Init Error: Failed to add customer File: " + dFileName);
                    continue;
                }
                StringBuilder distID = new StringBuilder();
                int id;

                for (int i = 0; i < dFileName.length(); i++)
                {
                    if (dFileName.charAt(i) == '.') break;
                    else distID.append(dFileName.charAt(i));
                }
                try {
                    id = Integer.parseInt(distID.toString());
                }
                catch (NumberFormatException e)
                {
                    System.out.println("Customer Init Error: Filename invalid: " + dFileName);
                    continue;
                }
                registry.put(id, build);
            }
        }

        return 0;
    }

    public int removeCustomer(int customerId)
    {
        File rootDir = new File(this.databasePath);

        if (rootDir.exists())
        {
            if (registry.containsKey(customerId))
            {
                registry.remove(customerId);
            }
            else return -2;
        }
        else return -1;
        return 0;
    }

    public Distributor addDistributor(String name, int paperCount)
    {
        Distributor insert = new Distributor(customerCount++, paperCount, name);

        if (insert.write() < 0) return null;
        registry.put(insert.id(), insert);
        return insert;
    }

    public Advertiser newAdvertiser(String name)
    {
        Advertiser insert = new Advertiser(name, customerCount++);

        if (insert.write() < 0) return null;
        registry.put(insert.id(), insert);
        return insert;
    }

    private Customer buildFromFile(String fileName)
    {
        File distFile = new File(databasePath + fileName);
        try
        {
            Scanner scan = new Scanner(distFile);

            String name = scan.nextLine();
            String type = scan.nextLine();

            if (type.compareTo("type=distributor") == 0)
            {
                int paperCount = Integer.parseInt(scan.nextLine());

                StringBuilder idStr = new StringBuilder();
                int id;
                for (int i = 0; i < fileName.length(); i++)
                {
                    if (fileName.charAt(i) == '.') break;
                    idStr.append(fileName.charAt(i));
                }

                try {
                    id = Integer.parseInt(idStr.toString());
                }
                catch (NumberFormatException e)
                {
                    return null;
                }

                scan.close();
                return new Distributor(id, paperCount, name);
            }
            else if (type.compareTo("type=advertiser") == 0)
            {
                StringBuilder idStr = new StringBuilder();
                int id;
                for (int i = 0; i < fileName.length(); i++)
                {
                    if (fileName.charAt(i) == '.') break;
                    idStr.append(fileName.charAt(i));
                }

                try {
                    id = Integer.parseInt(idStr.toString());
                }
                catch (NumberFormatException e)
                {
                    return null;
                }

                scan.close();
                return new Advertiser(name, id);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public String executeCommand(Employee loggedIn, Command command)
    {
        StringBuilder build = new StringBuilder();

        switch (command.getCommand())
        {
            case "add":
                if (command.getOptions() == null || command.getOptions().size() < 2)
                {
                    // Not enough arguments
                    build.append("Not enough arguments\n");
                    build.append("Expected - add [-a <advertiserName>] [-d <distributorName> <paperCount>]");
                    break;
                }

                for (int i = 0; i < command.getOptions().size(); i+=2)
                {
                    String flag = command.getOptions().get(i);
                }
                break;
            case "remove":
                break;
            case "update":
                break;
            case "list":
                break;
            default:
                build.append("No binding for command '").append(command.getCommand()).append("'");
        }
        return null;
    }
}
