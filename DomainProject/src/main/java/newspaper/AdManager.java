package newspaper;

import java.io.File;
import java.util.ArrayList;

public class AdManager
{
    ArrayList<Ad> ads;
    String databasePath;
    NewspaperManager paperRef;
    boolean initialized;

    public AdManager(String rootDir, NewspaperManager manager)
    {
        ads = new ArrayList<Ad>();
        databasePath = new String(rootDir + "/Ads");
        paperRef = manager;
        initialized = false;
    }

    public int build()
    {
        // Validate database path
        if (databasePath == null || databasePath == "")
        {
            return -1;
        }

        // Create files
        File root_dir = new File(this.databasePath);
        if (!root_dir.mkdir())
        {
            return -2;
        }

        System.out.println("ADMANAGER BUILD SUCCESSFUL");
        
        initialized = true;
        return 0;
    }

    public int run()
    {
        // main method (CLI)
        File root = new File(databasePath);

        // If there is no root dir, build
        if (!root.exists()) build();


        // TODO

        System.out.println("ALL GOOD");

        return 0;
    }

    private int newAd(int[] paper_identifer, String imageName)
    {
        // Locals
        Ad in;

        // Validate class
        if (paperRef == null || databasePath == null || databasePath == "")
        {
            return -1;
        }
        
        // Validate params
        if (paperRef.findPaper(paper_identifer) == null)
        {
            return -2;
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
