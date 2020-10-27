package newspaper.models;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Distributor extends Customer implements Writeable
{
    private int paperCount; // Number of papers to recieve
    private String distName;

    public Distributor(int id, int paperCount, String name)
    {
        super(id);
        this.paperCount = paperCount;
        this.distName = name;
    }

    public int paperCount()
    {
        return paperCount;
    }

    public String nameString()
    {
        return distName;
    }

    @Override
    public int write()
    {
        // Write this distributor out to a config file
        String build = "";
        build += distName + "\n" + "type=distributor\n" + paperCount + "\n";
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
