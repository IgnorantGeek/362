package newspaper.managers;

import newspaper.models.Ad;
import newspaper.models.Advertiser;
import newspaper.Global;
import newspaper.ui.Command;

import java.io.File;
import java.util.HashMap;

/**
 * Ad Manager class
 */
public class AdManager implements Commandable
{
    private HashMap<String, Ad> ads;
    private final String databasePath;
    private int customerCount;

    public AdManager()
    {
        ads = new HashMap<String, Ad>();
        databasePath = Global.DB_PATH;
        this.init();
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

                Ad insert = new Ad(adFile);

                this.ads.put(insert.getAdvertID(), insert);
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

    public int newAd(int[] paperIdentifier, String imageName, int advertiserID)
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
            in = new Ad(Global.generateID(), paperIdentifier, advertiserID);
        }
        else in = new Ad(Global.generateID(), paperIdentifier, advertiserID, imageName);
        
        if (in.write() < 0)
        {
            // error writing ad to database (file)
            return -3;
        }

        // Insert Ad into list
        this.ads.put(in.getAdvertID(), in);

        // Success
        return 0;
    }
    
    public HashMap<String, Ad> getAll() {
    	return ads;
    }

    @Override
    public int executeCommand(Command command)
    {
        return 0;
    }
}
