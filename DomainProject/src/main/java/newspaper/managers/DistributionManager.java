package newspaper.managers;

import newspaper.Global;
import newspaper.models.Distributor;
import newspaper.ui.Command;

import java.io.File;
import java.util.HashMap;
import java.util.Scanner;

public class DistributionManager implements Commandable
{
    private int customerCount;
    private HashMap<String, Distributor> registry;
    private String databasePath;

    public DistributionManager()
    {
        this.databasePath = Global.DISTRIBUTOR_DB_PATH;
        this.registry = new HashMap<String, Distributor>();
        this.init();
    }

    /**
     * init - Initializes the AdManager, loads the system config
     * @return - 0:  Success
     *         - -1: Error
     */
    public int init()
    {
        // Create the customers database folder if it does not exist
        File cust = new File(databasePath);

        if (!cust.exists())
        {
            if (!cust.mkdir()) return -2;
            customerCount = 0;
        }
        else 
        {
            customerCount = cust.list().length;
            for (String dFileName : cust.list())
            {
                Distributor build = buildFromFile(dFileName);
                String distID = "";

                for (int i = 0; i < dFileName.length(); i++)
                {
                    if (dFileName.charAt(i) == '.') break;
                    else distID += dFileName.charAt(i);
                }

                registry.put(distID, build);
            }
        }

        return 0;
    }

    public int addDistributor(String name, int paperCount)
    {
        Distributor dist = new Distributor(customerCount++, paperCount, name);

        if (dist.write() < 0) return -1;
        return 0;
    }

    public Distributor buildFromFile(String filename)
    {
        File distFile = new File(databasePath + filename);
        Distributor out = null;
        try 
        {
            Scanner scan = new Scanner(distFile);

            String name = scan.nextLine();
            String type = scan.nextLine();

            if (type.compareTo("type=distributor") != 0) 
            {
                scan.close();
                return null;
            }

            int paperCount = Integer.parseInt(scan.nextLine());

            String id = "";
            for (int i = 0; i < filename.length(); i++)
            {
                if (filename.charAt(i) == '.') break;
                id += filename.charAt(i);
            }

            out = new Distributor(Integer.parseInt(id), paperCount, name);

            scan.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return out;
    }

    public int removeDistributor(int distId)
    {
        File custDir = new File(this.databasePath);

        if (custDir.exists())
        {
            boolean found = false;
            for (int j = 0; j < custDir.list().length; j++)
            {
                String distributor = custDir.list()[j];
                String name = "";

                for (int i = 0; distributor.charAt(i)!= '.'; i++)
                {
                    name+= distributor.charAt(i);
                }

                if (distId == Integer.parseInt(name))
                {
                    try {
                        File distFile = new File(this.databasePath + custDir.list()[j]);

                        Scanner scan = new Scanner(distFile);

                        scan.nextLine();

                        if (scan.nextLine().compareTo("type=distributor") != 0)
                        {
                            System.out.println("Error, Customer with id: " + distId + "is not a Distributor");
                            scan.close();
                            return -1;
                        }
                        scan.close();
                        found = true;
                        distFile.delete();
                    } catch (Exception e) {
                        System.out.println("Could not remove distributor with ID: " + distId);
                        System.out.println(e.getMessage());
                    }
                }
            }
            if (!found) return -1;
        }
        return 0;
    }
    public HashMap<String, Distributor> getAll() {
    	return registry;
    }

    @Override
    public String executeCommand(Command command)
    {
        // TODO
        return null;
    }
}
