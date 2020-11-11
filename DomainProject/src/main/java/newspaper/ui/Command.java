package newspaper.ui;

public class Command
{
    private final String command;
    private final String[] options;

    public Command(String command, String[] options)
    {
        this.command = command;
        this.options = options;
    }

    public String[] getOptions() {
        return options;
    }

    public String getCommand() {
        return command;
    }
}
