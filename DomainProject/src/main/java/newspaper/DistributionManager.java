package newspaper;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class DistributionManager
{
    private int customerCount;
    private String databasePath;
    private ArrayList<Distributor> distributors;

    public DistributionManager(String path)
    {
        this.databasePath = path;
        this.distributors = new ArrayList<Distributor>();
    }

    /**
     * init - Initializes the AdManager, loads the system config
     * @return - 0:  Success
     *         - -1: Error
     */
    public int init()
    {
        // Create the customers database folder if it does not exist
        File cust = new File(databasePath + "/Customers");

        if (!cust.exists())
        {
            if (!cust.mkdir()) return -2;
            customerCount = 0;
        }
        else customerCount = cust.list().length;

        return 0;
    }

    public int addDistributor(String name, int paperCount)
    {
        Distributor dist = new Distributor(customerCount++, paperCount, name);

        if (dist.write() < 0) return -1;
        return 0;
    }

    public int removeDistributor(int distId)
    {
        File custDir = new File(this.databasePath + "/Customers");

        if (custDir.exists())
        {
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
                        File distFile = new File(this.databasePath + "/Customers/" + custDir.list()[j]);

                        Scanner scan = new Scanner(distFile);

                        scan.nextLine();

                        if (scan.nextLine().compareTo("type=distributor") != 0)
                        {
                            System.out.println("Error, Customer with id: " + distId + "is not a Distributor");
                            scan.close();
                            return -1;
                        }
                        scan.close();
                        distFile.delete();
                    } catch (Exception e) {
                        System.out.println("Could not remove distributor with ID: " + distId);
                        System.out.println(e.getMessage());
                    }
                }
            }
        }
        return 0;
    }
}
