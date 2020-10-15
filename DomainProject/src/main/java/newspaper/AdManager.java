package main.java.newspaper;

import java.io.File;
import java.util.ArrayList;

public class AdManager
{
    private ArrayList<Ad> ads;
    private String databasePath;
    private int customerCount;

    public AdManager(String rootDir)
    {
        ads = new ArrayList<Ad>();
        databasePath = new String(rootDir);
    }


    /**
     * init - Initializes the AdManager, loads the system config
     * @return - 0:  Success
     *         - -1: Error
     */
    public int init()
    {
        // main method (CLI)
        File root = new File(databasePath + "/Ads");

        // If there is no root dir, build
        if (!root.exists())
        {
            // Validate database path
            if (databasePath == null || databasePath == "")
            {
                return -1;
            }

            // Create files
            if (!root.mkdir()) return -2;
        }

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

    public int newAdvertiser(String name)
    {
        Advertiser insert = new Advertiser(name, customerCount++);

        return insert.write();
    }

    public int newAd(int[] paper_identifer, String imageName)
    {
        // Locals
        Ad in;

        // Validate class
        if (databasePath == null || databasePath == "")
        {
            return -1;
        }

        if (imageName == null || imageName == "")
        {
            in = new Ad(Global.generateID(), paper_identifer);
        }
        else in = new Ad(Global.generateID(), paper_identifer, imageName);
        
        if (in.write() < 0)
        {
            // error writing ad to database (file)
            return -3;
        }

        // Insert Ad into list
        this.ads.add(in);

        // Success
        return 0;
    }
}
