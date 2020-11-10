package newspaper.ui;

import java.util.ArrayList;

public class Command
{
    private String command;
    private ArrayList<String> options;

    public Command(String command, ArrayList<String> options)
    {
        this.command = command;
        this.options = new ArrayList<>(options);
    }

    public ArrayList<String> getOptions() {
        return options;
    }

    public String getCommand() {
        return command;
    }

    public void addOption(String opt)
    {
        this.options.add(opt);
    }
}
