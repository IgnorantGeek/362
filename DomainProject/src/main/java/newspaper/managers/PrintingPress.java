package newspaper.managers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import newspaper.Global;
import newspaper.models.Employee;
import newspaper.models.Newspaper;
import newspaper.ui.Command;

public class PrintingPress implements Commandable {
    //ink in milliliters
    public double ink;
    //pieces of paper
    public int paper;
    //max ink in milliliters
    public double maxInk;
    //max pieces of paper
    public int maxPaper;

    NewspaperManager nman;


    /**
     *
     * @param ink ml of ink left
     * @param paper # of pieces of paper left
     */
    public PrintingPress(NewspaperManager nman) {
        this.nman = nman;
        File Archive =  new File(Global.PRINTINGPRESS_DB_PATH);
        try {
            Scanner s = new Scanner(Archive);
            String[] line = s.nextLine().split(",");
            int paper = Integer.parseInt(line[0].trim());
            double ink = Double.parseDouble(line[1].trim());
            this.ink = ink;
            this.paper = paper;
            s.close();
        } catch (FileNotFoundException e) {
            System.out.println("PrintingPress file is inaccessible.");
        }
        maxInk = 5000; //number hardcoded. It will be determined by hardware.
        maxPaper = 1000; //number hardcoded. It will be determined by hardware.
    }
    /**
     * resets ink to maximum level. Only to be called after ink is manually refilled.
     */
    public void refillInk() {
        ink = maxInk;
        save();
    }

    /**
     * resets paper to max level. Only to be used after manually refilled.
     */
    public void refillPaper() {
        paper = maxPaper;
        save();
    }

    /**
     * Prints a number of copies of given newspaper. Paper must be finalized.
     * @param n paper to be preinted
     * @param number number of copies to be printed
     * @return true if printed, false if there is insufficient resources
     */
    public boolean printPaper(Newspaper n, int number) {
        double inkCost = getInkCost(n) * number;
        int paperCost = getPaperCost(n) * number;

        if(inkCost > ink || paperCost > paper) {
            return false;
        }

        ink -= inkCost;
        paper -= paperCost;
        save();
        return true;
    }

    /*
     * Saves to current ink and paper level to file
     * @return true if successful, false if not
     */
    private boolean save() {
        PrintWriter out = null;
        try {
            out = new PrintWriter(new BufferedWriter(new FileWriter(Global.PRINTINGPRESS_DB_PATH, false)));
            out.println(toString());
        } catch (IOException e) {
            System.out.println("PrintingPress file corrupted. Seek IT help.");
            return false;
        } finally {
            if (out != null) {
                out.close();
            }
        }
        return true;
    }

    /**
     * Gets the amount of paper needed to print a copy of this paper. Not likely to be implemented,
     * always returns 10
     * @param n paper to be evaluated
     * @return number of pieces of paper needed to print one copy of newspaper n
     */
    private int getPaperCost(Newspaper n) {
        // TODO Auto-generated method stub
        return 10;
    }

    /**
     * Gets the amount of ink needed to print a copy of this paper. Not likely to be implemented,
     * always returns 1
     * @param n paper to be evaluated
     * @return ml of ink needed to print one copy of newspaper n
     */
    private int getInkCost(Newspaper n) {
        return 1;
    }

    @Override
    public String toString() {
        String result = "";
        result += paper;
        result += ", " + ink;
        return result;
    }
    @Override
    public String executeCommand(Employee loggedIn, Command command) {
        String result = "";
        switch (command.getCommand())
        {
            case "refill":
                String type = "";
                if(command.getOptions() != null && command.getOptions().size() == 1) {
                    type = command.getOptions().get(0);
                }
                if(type.equals("ink")) {
                    refillInk();
                    result += "Ink levels reset to maximum.";
                }
                else if(type.equals("paper")) {
                    refillPaper();
                    result += "Paper levels reset to maximum";
                }
                else {
                    result += "Refill command must give exactly 1 option, ink or paper.";
                }
                break;

            case "view":
                String vtype = "";
                if(command.getOptions() != null && command.getOptions().size()== 1) {
                    vtype = command.getOptions().get(0);
                }
                if(vtype.equals("ink")) {
                    result += "Ink levels: " + ink;
                }
                else if(vtype.equals("paper")) {
                    result += "Paper levels: " + paper;
                }
                else {
                    result += "View command must give exactly 1 option, ink or paper.";
                }
                break;

            case "print":
                int num = 0;
                if(command.getOptions() != null && command.getOptions().size() == 1) {
                    num = Integer.parseInt(command.getOptions().get(0));
                }
                if(num != 0) {
                    Newspaper n = nman.search();
                    if(printPaper(n, num)) {
                        result = "Print order succesful.";
                    }
                    else {
                        result = "Insufficient paper or ink to fulfill print order. Refill and try again.";
                    }
                }
                else {
                    result = "Print command must give exactly 1 option, number of papers to be printed."
                            + " 0 is not a valid number. Paper is selected with a search.";
                }
                break;

            default:
                return null;
        }

        // Good return
        return result;
    }
}
