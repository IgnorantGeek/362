package newspaper.ui;

import java.util.ArrayList;

public class Command
{
    private final String command;
    private final ArrayList<String> options;

    public Command(String command, ArrayList<String> options)
    {
        this.command = command;
        this.options = options;
    }

    public ArrayList<String> getOptions() {
        return options;
    }

    public String getCommand() {
        return command;
    }
}
