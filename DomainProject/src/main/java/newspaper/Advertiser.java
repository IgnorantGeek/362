package newspaper;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Advertiser extends Customer implements Writeable
{
    private String name;

    public Advertiser(String name, int id)
    {
        super(id);
        this.name = name;
    }

    public String Name() { return name; }

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
}
