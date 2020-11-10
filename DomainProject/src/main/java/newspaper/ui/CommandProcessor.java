package newspaper.ui;

import newspaper.managers.*;

public class CommandProcessor
{
    private String[] commandList = {
            "newspaper",
            "article",
            "employee",
            "finance",
            "subscription",
            "distributor",
            "feedback",
            "advert"
    };
    private NewspaperManager nman = new NewspaperManager();
    private ArticleManager aman = new ArticleManager();
    private AdManager adman = new AdManager();
    private SubscriptionManager sman = new SubscriptionManager();
    private DistributionManager dman = new DistributionManager();
    private DMEditor messageToEditor = new DMEditor();
    private Feedback feedback = new Feedback();
    private EmployeeManager eman = new EmployeeManager(10); // Reserve ids 0-9 for testing
    private FinancialManager fman = new FinancialManager(eman,adman,sman,dman);
    private Report rport = new Report(eman);
    private Retract ract = new Retract(nman, aman);

    public CommandProcessor() { }
}
