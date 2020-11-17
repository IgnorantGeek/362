package newspaper.managers;

import newspaper.models.Employee;
import newspaper.ui.Command;

/**
 * Interface for a manager to issue a command from the CommandProcessor
 */
public interface Commandable
{
    String executeCommand(Employee loggedIn, Command command);
}
