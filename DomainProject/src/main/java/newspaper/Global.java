package newspaper;

import java.lang.StringBuilder;
import java.util.Random;

public class Global
{
    public static String DB_PATH = "../Database/";
    public static String EMPLOYEE_DB_PATH = "../Database/Employees/";
    public static String TASK_DB_PATH = "../Database/Tasks/Tasks.txt";
    public static String DISTRIBUTOR_DB_PATH = "../Database/Distributors/";
    public static String ADVERTISER_DB_PATH = "../Database/Advertisers/";
    public static String PRINTINGPRESS_DB_PATH = "../Database/PrintingPress.txt";
    public static String FINANCE_DB_PATH = "../Database/Finance.txt";
    public static double DEFAULT_WAGE = 7.25;
    public static String generateID()
    {
        int lowerlet  = 65;
        int upperlet  = 90;
        int lowernum  = 48;
        int uppernum  = 57;
        int maxLen = 16;
        Random random = new Random();
        StringBuilder buf = new StringBuilder(maxLen);
        for (int i = 0; i < maxLen; i++) 
        {
            int rand = random.nextInt();
            int randomLimitedInt;
            if (rand % 2 == 0)
            {
                randomLimitedInt = random.nextInt(upperlet-lowerlet) + lowerlet;
            }
            else
            {
                randomLimitedInt = random.nextInt(uppernum-lowernum) + lowernum;
            }
            buf.append((char) randomLimitedInt);
        }
        
        return buf.toString();
    }

    /**
     * Flushes the console (only works in a terminal)
     */
    public static void flushConsole()
    {
        System.out.print("\033[H\033[2J");  
        System.out.flush();
    }
}
