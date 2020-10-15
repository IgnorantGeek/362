package newspaper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import org.json.simple.JSONObject;

public class AdManager
{
    ArrayList<Ad> ads;
    String databasePath;
    int customerCount;
    NewspaperManager paperRef;

    public AdManager(String rootDir, NewspaperManager manager)
    {
        ads = new ArrayList<Ad>();
        databasePath = new String(rootDir + "/Ads/");
        paperRef = manager;
    }


    /**
     * init - Initializes the AdManager, loads the system config
     * @return - 0:  Success
     *         - -1: Error
     */
    public int init()
    {
        // main method (CLI)
        File root = new File(databasePath);

        // If there is no root dir, build
        if (!root.exists())
        {
            // Validate database path
            if (databasePath == null || databasePath == "")
            {
                return -1;
            }

            // Create files
            File root_dir = new File(this.databasePath);
            if (!root_dir.mkdir()) return -2;

            // Create/Update the system config file
        }
        // Else read the config file
        else
        {
            File sysConfig = new File(databasePath + "/adman.json");

            if (!sysConfig.exists())
            {
                return writeConfig();
            }
            // Set customer count
            else
            {
                try 
                {
                    Scanner sysScan = new Scanner(sysConfig);

                    String whole = new String();

                    while (sysScan.hasNextLine())
                    {
                        whole += sysScan.nextLine();
                    }

                    sysScan.close();

                    System.out.println("CONFIG READ: " + whole);
                } catch (FileNotFoundException e)
                {
                    System.out.println("ADMAN: FATAL ERROR - NO SYSTEM CONFIG FOUND. EXITING.");
                    return -1;
                }
            }
        }

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

    // hold on
    private int writeConfig()
    {
        try 
        {
            BufferedWriter writer = new BufferedWriter(new FileWriter(this.databasePath + "adman.json"));

            HashMap<String, Object> sysConfigHash = new HashMap<String, Object>();

            sysConfigHash.put("CustCount", this.customerCount);

            JSONObject sysConfigJSON = new JSONObject(sysConfigHash);

            writer.write(sysConfigJSON.toJSONString());

            System.out.println(sysConfigJSON.toJSONString());

            writer.close();
        } catch (IOException e) {
            System.out.println("ADMAN: BUILD FAILED - COULD NOT CREATE CONFIG FILE");
            return -3;
        }

        return 0;
    }
}
