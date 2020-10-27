package newspaper;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Advertiser extends Customer implements Writeable
{
    private String name;
    private ArrayList<Ad> ads;

    public Advertiser(String name, int id)
    {
        super(id);
        this.name = name;
    }

    public Advertiser(String name, int id, Ad ad)
    {
        super(id);
        this.name = name;
        this.ads.add(ad);
    }

    public String Name() { return name; }

    public ArrayList<Ad> Ads() { return ads; }

    public void insertAd(Ad ad)
    {
        this.ads.add(ad);
    }
    
    public int id() {
        return this.id();
    }

    @Override
    public int write()
    {
        // Write this distributor out to a config file
        String build = "";
        build += name + "\n" + "type=advertiser\n";
        String fileName = this.id() + ".txt";

        // try to write to file
        try {
            BufferedWriter write = new BufferedWriter(new FileWriter("../Database/Customers/" + fileName));

            write.write(build);

            write.close();
        } catch (IOException e) {
            System.out.println("ERROR WRITING AD");
            return -1;
        }
        return 0;
    }

    // TODO
    @Override
    public int delete()
    {
        return 0;
    }
}
