package newspaper.managers;

import newspaper.ui.Command;

/**
 * Interface for a manager to issue a command from the CommandProcessor
 */
public interface Commandable
{
    String executeCommand(Command command);
}
