package newspaper.managers;

import newspaper.models.Ad;
import newspaper.models.Advertiser;
import newspaper.Global;
import java.io.File;
import java.util.ArrayList;

/**
 * Ad Manager class
 */
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
        else
        {
            // Initialize the ad structure, read all ads in /Ads/
            for (String fName : root.list())
            {
                File adFile = new File(databasePath + "/Ads/" + fName);

                this.ads.add(new Ad(adFile));
            }
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

    public Advertiser newAdvertiser(String name)
    {
        Advertiser insert = new Advertiser(name, customerCount++);

        if (insert.write() < 0) return null;

        return insert;
    }

    public int newAd(int[] paper_identifer, String imageName, int advertiserID)
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
            in = new Ad(Global.generateID(), paper_identifer, advertiserID);
        }
        else in = new Ad(Global.generateID(), paper_identifer, advertiserID, imageName);
        
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

    public int newAd(Ad ad)
    {
        if (ad.write() < 0)
        {
            // error writing ad to database (file)
            return -3;
        }

        // Insert Ad into list
        this.ads.add(ad);

        // Success
        return 0;
    }
}
