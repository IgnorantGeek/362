package newspaper;

/**
 * An ad that will appear in a given newspaper
 */
public class Ad
{
    private String advertID;  // Unique id for this ad
    private int[]  paperID;   // Paper identifier
    private String img_name;  // Name of the image file

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

    // TODO
    public int write()
    {

        return 0;
    }
}