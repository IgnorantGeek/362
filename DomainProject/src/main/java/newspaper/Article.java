package newspaper;

public abstract class Article {
	private String name;
	private String desc;
	private int volume;
	private int issue;
	private int day;
	private int month;
	private int year;
	private boolean finalized;
	private String path;
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
	public int getVolume()
	{
		return volume;
	}
	public int getIssue()
	{
		return issue;
	}
	public int getDay()
	{
		return day;
	}
	public int getMonth()
	{
		return month;
	}
	public int getYear()
	{
		return year;
	}
	public boolean isFinalized()
	{
		return finalized;
	}
	public String getName()
	{
		return name;
	}
	public String getDesc()
	{
		return desc;
	}
	public String path()
	{
		return path;
	}
	abstract boolean readArticle(int clearance);
	abstract boolean finalizeArticle(int clearance);
	abstract boolean editArticle(int clearance);
}