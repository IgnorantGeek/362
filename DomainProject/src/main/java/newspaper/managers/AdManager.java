package newspaper.managers;

import newspaper.models.Ad;
import newspaper.models.Advertiser;
import newspaper.Global;
import newspaper.models.Customer;
import newspaper.models.Employee;
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
    private CustomerManager cman;

    public AdManager()
    {
        ads = new HashMap<>();
        databasePath = Global.DB_PATH;
        this.init();
        this.cman = null;
    }

    public AdManager(CustomerManager cman)
    {
        ads = new HashMap<>();
        databasePath = Global.DB_PATH;
        this.init();
        this.cman = cman;
    }


    /**
     * init - Initializes the AdManager, loads the system config
     * @return - 0:  Success
     *         - -1: Error
     */
    public int init()
    {
        // main method (CLI)
        File root = new File(databasePath + "/Ads/");

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

    public Ad newAd(int[] paperIdentifier, String imageName, int advertiserID)
    {
        // Locals
        Ad out;

        // Validate class
        if (databasePath == null || databasePath.equals(""))
        {
            return null;
        }

        if (imageName == null || imageName.equals(""))
        {
            out = new Ad(Global.generateID(), paperIdentifier, advertiserID);
        }
        else out = new Ad(Global.generateID(), paperIdentifier, advertiserID, imageName);
        
        if (out.write() < 0)
        {
            // error writing ad to database (file)
            return null;
        }

        // Insert Ad into list
        this.ads.put(out.getAdvertID(), out);

        // Success
        return out;
    }
    
    public HashMap<String, Ad> getAll() {
    	return ads;
    }

    @Override
    public String executeCommand(Employee loggedIn, Command command)
    {
        StringBuilder build = new StringBuilder();
        switch (command.getCommand())
        {
            case "new":
                if (command.getOptions().size() < 3)
                {
                    build.append("Ad cmd Error: Not enough arguments.\n");
                    build.append("Expected - ad new <advertiserID> <volume> <issue> [imageFileName]");
                }
                else if (command.getOptions().size() == 3)
                {
                    int advertiserId, vol, issue;
                    try {
                        advertiserId = Integer.parseInt(command.getOptions().get(0));
                        vol = Integer.parseInt(command.getOptions().get(1));
                        issue = Integer.parseInt(command.getOptions().get(2));
                    } catch (NumberFormatException e)
                    {
                        build.append("Ad cmd Error: Volume/Issue not a valid number: ").append(e.getMessage());
                        break;
                    }

                    if (cman.getRegistry().containsKey(advertiserId))
                    {
                        if (cman.getRegistry().get(advertiserId) instanceof Advertiser)
                        {
                            int[] paperId = {vol, issue, 0, 0, 0};

                            newAd(paperId, null, advertiserId);

                            build.append("Warning! You created an Ad with an empty image name. No image will appear ");
                            build.append("in the finalized paper unless a valid image file is added");
                        }
                        else
                        {
                            build.append("Ad internal Error: Customer with ID ").append(advertiserId);
                            build.append(" is not of type Advertiser. Canceling.");
                        }
                    }
                    else
                    {
                        build.append("Ad internal Error: No advertiser found with ID: ").append(advertiserId);
                    }
                }
                else
                {
                    int advertiserId, vol, issue;
                    try {
                        advertiserId = Integer.parseInt(command.getOptions().get(0));
                        vol = Integer.parseInt(command.getOptions().get(1));
                        issue = Integer.parseInt(command.getOptions().get(2));
                    } catch (NumberFormatException e)
                    {
                        build.append("Ad cmd Error: Volume/Issue not a valid number: ").append(e.getMessage());
                        break;
                    }

                    if (cman.getRegistry().containsKey(advertiserId))
                    {
                        if (cman.getRegistry().get(advertiserId) instanceof Advertiser)
                        {
                            if (cman.getRegistry().get(advertiserId) instanceof Advertiser)
                            {
                                int[] paperId = {vol, issue, 0, 0, 0};

                                if (command.getOptions().get(3).equals(""))
                                {
                                    build.append("Warning! You created an Ad with an empty image name. No image will appear ");
                                    build.append("in the finalized paper unless a valid image file is added");
                                    newAd(paperId, null, advertiserId);
                                }
                                else
                                {
                                    Ad in = newAd(paperId, command.getOptions().get(3), advertiserId);
                                    build.append("Created new Ad: ").append(in.getAdvertID());
                                }
                            }
                            else
                            {
                                build.append("Ad internal Error: Customer with ID ").append(advertiserId);
                                build.append(" is not of type Advertiser. Canceling.");
                            }
                        }
                    }
                    else
                    {
                        build.append("Ad internal Error: No advertiser found with ID: ").append(advertiserId);
                    }
                }
                break;
            case "remove":
                break;
            case "list":
                build.append("Tracked Advertisements:");
                int i = 0;
                for (Ad ad : ads.values())
                {
                    build.append("\n-").append(i).append("--------------------------------------\n");
                    build.append("| ").append(ad.getAdvertID()).append("\n");
                    build.append("| imgFile: ").append(ad.getImagePath());
                    i++;
                }
                build.append("\n----------------------------------------\n");
                break;
            default:
                build.append("Retract cmd Error: No binding found for '").append(command.getCommand()).append("'");
        }
        return build.toString();
    }
}
