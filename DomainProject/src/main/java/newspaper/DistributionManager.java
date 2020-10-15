package newspaper;

import java.io.File;
import java.util.ArrayList;

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

    public int addDistributor()
    {

        return 0;
    }

    public int removeDistributor()
    {
        return 0;
    }
}
