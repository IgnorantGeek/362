package newspaper.models;

/**
 * An interface for Objects that get written to the database
 */
public interface Writeable
{
    // Write to database
    public int write();
    public int delete();
}
