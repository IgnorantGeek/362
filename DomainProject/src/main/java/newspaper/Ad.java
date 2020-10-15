package newspaper;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * An ad that will appear in a given newspaper
 */
public class Ad
{
    private String advertID;  // Unique id for this ad
    private int[]  paperID;   // Paper identifier
    private String img_name;  // Name of the image file
    private int    type = 0;

    public Ad(String advertID, int[] paperID)
    {
        this.advertID = advertID;
        this.paperID = paperID;
        this.img_name = advertID + "_img.png";
    }

    public Ad(String advertID, int[] paperID, String img_name)
    {
        this.advertID = advertID;
        this.paperID = paperID;
        this.img_name = img_name;
    }

    public Ad(String advertID, int[] paperID, String img_name, int location_code)
    {
        this.advertID = advertID;
        this.paperID = paperID;
        this.img_name = img_name;
        this.type = location_code;
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

    // TODO
    public int write()
    {
        // Write this ad out to a config file
        String build = "";
        build += paperID[0] + ", " + paperID[1] + ", " + paperID[2] + ", " + paperID[3] + ", " + paperID[4] + "\n";
        build += img_name + "\n";
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
}