package newspaper.models;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * An ad that will appear in a given newspaper
 */
public class Ad implements Writeable
{
    private String advertID;  // Unique id for this ad
    private int[]  paperID;   // Paper identifier
    private String img_name;  // Name of the image file
    private int    advertiserID;
    private int    type = 0;

    public Ad(String advertID, int[] paperID, int advertiserID)
    {
        this.advertID = advertID;
        this.paperID = paperID;
        this.img_name = "";
        this.advertiserID = advertiserID;
    }

    public Ad(String advertID, int[] paperID, int advertiserID, String img_name)
    {
        this.advertID = advertID;
        this.paperID = paperID;
        this.img_name = img_name;
        this.advertiserID = advertiserID;
    }

    public Ad(String advertID, int[] paperID, int advertiserID, String img_name, int location_code)
    {
        this.advertID = advertID;
        this.paperID = paperID;
        this.img_name = img_name;
        this.type = location_code;
        this.advertiserID = advertiserID;
    }

    public Ad(File adFile)
    {
        // Build from file
        Scanner scan;
        try {
            scan = new Scanner(adFile);

            String[] whole = adFile.getName().split("_");

            this.paperID = new int[5];
            for (int i = 0; i < 5; i++)
            {
                paperID[i] = 0;
            }

            String id = "";
            for (int i = 0; i < 16; i++)
            {
                id += whole[1].charAt(i);
            }

            this.advertID = id;

            this.type = Integer.parseInt(scan.nextLine());

            this.img_name = scan.nextLine();

            this.advertiserID = Integer.parseInt(scan.nextLine());
        } catch (FileNotFoundException e) {
            System.out.println("Ad Warning: Could not locate ad file: " + e.getMessage());
        }
        
    }

    public int[] getPaper()
    {
        return paperID;
    }

    public String getAdvertID()
    {
        return advertID;
    }

    public String getImagePath()
    {
        return img_name;
    }

    public int getType()
    {
        return type;
    }

    public int write()
    {
        // Write this ad out to a config file
        String build = "";
        build += type + "\n" + img_name + "\n" + advertiserID + "\n";
        String fileName = paperID[0] + "" + paperID[1] + "" + paperID[2] + "" + paperID[3] + "" + paperID[4] + "_" + advertID + ".txt";

        // try to write to file
        try {
            BufferedWriter write = new BufferedWriter(new FileWriter("../Database/Ads/" + fileName));

            write.write(build);

            write.close();
        } catch (IOException e) {
            System.out.println("ERROR WRITING AD");
            return -1;
        }
        return 0;
    }

    public int delete()
    {
        // TODO
        return 1;
    }
}