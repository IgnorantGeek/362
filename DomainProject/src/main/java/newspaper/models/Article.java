package newspaper.models;

/**
 * Abstract class holds information for all article types, and gives some basic functions for them.
 * @author Alexander Irlbeck
 * **Works as of 10/14/20
 */
public abstract class Article {
	/**
	 * The title of the article.
	 */
	private String name;
	/**
	 * The description of the article
	 */
	private String desc;
	/**
	 * The volume the article appears in
	 */
	private int volume;
	/**
	 * The issue the article appears in
	 */
	private int issue;
	/**
	 * The publishing day the article appears in
	 */
	private int day;
	/**
	 * The publishing month the article appears in
	 */
	private int month;
	/**
	 * The publishing year the article appears in
	 */
	private int year;
	/**
	 * Whether or not the article was published
	 */
	protected boolean finalized;
	/**
	 * The path to the article in the Database
	 */
	private String path;
	/**
	 * Assigns all the variables to be held.
	 * @param name The title of the article
	 * @param desc The description of the article
	 * @param volume The volume the article appears in
	 * @param issue The issue the article appears in
	 * @param day The publishing day the article appears in
	 * @param month The publishing month the article appears in
	 * @param year The publishing year the article appears in
	 * @param finalized Whether or not the article was published
	 * @param path The path to the article in the Database
	 * **Works as of 10/14/20
	 */
	public Article(String name, String desc, int volume, int issue, int day, int month, int year, boolean finalized, String path)
	{
		this.volume=volume;
		this.issue=issue;
		this.day=day;
		this.month=month;
		this.year=year;
		this.finalized=finalized;
		this.name=name;
		this.desc=desc;
		this.path=path;
	}
	/**
	 * Returns the volume of the article
	 * @return Returns the volume of the article
	 * **Works as of 10/14/20
	 */
	public int getVolume()
	{
		return volume;
	}
	/**
	 * Returns the issue of the article
	 * @return Returns the volume of the article
	 * **Works as of 10/14/20
	 */
	public int getIssue()
	{
		return issue;
	}
	/**
	 * Returns the publishing day of the article
	 * @return Returns the volume of the article
	 * **Works as of 10/14/20
	 */
	public int getDay()
	{
		return day;
	}
	/**
	 * Returns the publishing month of the article
	 * @return Returns the volume of the article
	 * **Works as of 10/14/20
	 */
	public int getMonth()
	{
		return month;
	}
	/**
	 * Returns the publishing year of the article
	 * @return Returns the volume of the article
	 * **Works as of 10/14/20
	 */
	public int getYear()
	{
		return year;
	}
	/**
	 * Returns the publishing status of the article
	 * @return Returns the volume of the article
	 * **Works as of 10/14/20
	 */
	public boolean isFinalized()
	{
		return finalized;
	}
	/**
	 * Returns the Title of the article
	 * @return Returns the volume of the article
	 * **Works as of 10/14/20
	 */
	public String getName()
	{
		return name;
	}
	/**
	 * Returns the description of the article
	 * @return Returns the volume of the article
	 * **Works as of 10/14/20
	 */
	public String getDesc()
	{
		return desc;
	}
	/**
	 * Returns the path of the article in the database
	 * @return Returns the volume of the article
	 * **Works as of 10/14/20
	 */
	public String path()
	{
		return path;
	}
	/**
	 * Prints out the article line for line in the console
	 * @param clearance Clearance level of the actor
	 * @return whether or not the method succeeded
	 */
	public abstract boolean readArticle(int clearance);
	/**
	 * Finalizes the article.
	 * @param clearance Clearance level of the actor
	 * @return whether or not the method succeeded
	 */
	public abstract boolean finalizeArticle(int clearance);
	/**
	 * Edits the article.
	 * @param clearance Clearance level of the actor
	 * @return whether or not the method succeeded
	 */
	public abstract boolean editArticle(int clearance);
}