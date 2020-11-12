package newspaper.managers;

import newspaper.Global;
import newspaper.models.Advertiser;
import newspaper.models.Customer;
import newspaper.models.Distributor;
import newspaper.models.Employee;
import newspaper.ui.Command;

import java.io.File;
import java.util.Collection;
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
        if (registry.containsKey(customerId))
        {
            registry.remove(customerId);

            File customerFile = new File(this.databasePath + customerId + ".txt");

            if (customerFile.delete()) return 0;
            else return -2;
        } else return -1;
    }

    public Distributor newDistributor(String name, int paperCount)
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
                if (command.getOptions() == null || command.getOptions().size() < 1)
                {
                    // Not enough arguments
                    build.append("Customer cmd Error: Not enough arguments\n");
                    build.append("Expected - add [-a <advertiserName>] [-d <distributorName> <paperCount>]");
                    break;
                }

                String flag = command.getOptions().get(0);

                switch (flag)
                {
                    case "-a":
                    case "-A":
                        // Check for name
                        if (command.getOptions().size() < 2)
                        {
                            // missing the paperCount param
                            build.append("Customer cmd Error: Not enough arguments for new Advertiser\n");
                            build.append("Expected - add -a <advertiserName>");
                            return build.toString();
                        }

                        Advertiser advertiser = newAdvertiser(command.getOptions().get(1));

                        if (advertiser == null)
                        {
                            build.append("Customer internal Error: failed to create new Advertiser");
                            return build.toString();
                        }

                        registry.put(advertiser.id(), advertiser);
                        build.append("Successfully added new Advertiser with ID: ").append(advertiser.id());
                        break;
                    case "-d":
                    case "-D":
                        // Check for two following params
                        if (command.getOptions().size() < 3)
                        {
                            // missing the paperCount param
                            build.append("Customer cmd Error: Not enough arguments for new Distributor\n");
                            build.append("Expected - add -d <distributorName> <paperCount>");
                            return build.toString();
                        }

                        // Parse int
                        int paperCount;
                        try {
                            paperCount = Integer.parseInt(command.getOptions().get(2));
                        }
                        catch (NumberFormatException e)
                        {
                            build.append("Customer cmd Error: PaperCount argument not a number");
                            break;
                        }

                        Distributor distributor = newDistributor(command.getOptions().get(1), paperCount);

                        if (distributor == null)
                        {
                            build.append("Customer internal Error: Error adding new Distributor");
                            break;
                        }

                        registry.put(distributor.id(), distributor);
                        build.append("Successfully added new Distributor with ID: ").append(distributor.id());
                        break;
                    default:
                        build.append("Customer cmd Error: No binding found for flag '").append(flag);
                        build.append("'");
                }
                break;
            case "remove":
                if (command.getOptions() == null
                        ||  command.getOptions().size() < 1)
                {
                    build.append("Customer cmd Error: Not enough arguments\n");
                    build.append("Expected - remove <id1> <id2> ... <idn>");
                    break;
                }
                for (String op : command.getOptions())
                {
                    int ID;

                    try {
                        ID = Integer.parseInt(op);
                    }
                    catch (NumberFormatException e)
                    {
                        build.append("Argument '").append(op).append("' not a valid number. Skipped\n");
                        continue;
                    }

                    int dropStatus = removeCustomer(ID);

                    if (dropStatus == 0)
                    {
                        build.append("Successfully dropped Customer: ").append(ID).append('\n');
                    }
                    else if (dropStatus == -1)
                    {
                        build.append("Customer internal Error: No Customer found with ID ").append(ID);
                        build.append(". Skipped\n");
                    }
                    else if (dropStatus == -2)
                    {
                        build.append("Employee internal Error: Error writing Customer ").append(ID);
                        build.append(" to database. Skipped\n");
                    }
                }
                break;
            case "update":
                // TODO
                break;
            case "list":
                Collection<Customer> customers = registry.values();
                build.append("Customers:\n");
                for (Customer c : customers)
                {
                     if (c instanceof Distributor)
                     {
                         Distributor distributor = (Distributor) c;
                         build.append("-Distributor----------------\n");
                         build.append(distributor.id()).append(":  ").append(distributor.nameString());
                         build.append("\nPaperCount: ").append(distributor.paperCount()).append('\n');
                         build.append("----------------------------\n");
                     }
                     else if (c instanceof Advertiser)
                     {
                         Advertiser advertiser = (Advertiser) c;
                         build.append("-Advertiser-----------------\n");
                         build.append(advertiser.id()).append(":  ").append(advertiser.Name()).append('\n');
                         build.append("----------------------------\n");
                     }
                }
                break;
            default:
                build.append("No binding for command '").append(command.getCommand()).append("'");
        }
        return build.toString();
    }

    public HashMap<Integer, Customer> getRegistry() { return registry; }
}
