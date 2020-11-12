package newspaper.managers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Scanner;

import newspaper.Global;
import newspaper.models.Ad;
import newspaper.models.Customer;
import newspaper.models.Distributor;
import newspaper.models.Employee;
import newspaper.models.HourlyEmployee;
import newspaper.models.Subscription;
import newspaper.ui.Command;

public class FinancialManager implements Commandable
{
	HashMap<Integer, Employee> employeeRegistry;
	HashMap<String, Subscription> subs;
	HashMap<String, Ad> ads;
	HashMap<Integer, Customer> customers;

	double subPrice;
	double paperPrice;
	double adPrice;
	double printCost;
	double revenue;
	double expenses;

	public FinancialManager(EmployeeManager eman, AdManager adman, SubscriptionManager sman, CustomerManager cman) {
		employeeRegistry = eman.getRegistry();
		subs = sman.getAll();
		ads = adman.getAll();
		customers = cman.getRegistry();


		File Archive =  new File(Global.FINANCE_DB_PATH);
		try {
			Scanner s = new Scanner(Archive);
			String[] line = s.nextLine().split(",");
			this.subPrice = Double.parseDouble(line[0].trim());
			this.paperPrice = Double.parseDouble(line[1].trim());
			this.adPrice = Double.parseDouble(line[2].trim());
			this.printCost = Double.parseDouble(line[3].trim());
			s.close();
		} catch (FileNotFoundException e) {
			System.out.println("Finance file is inaccessible.");
		}
	}

	public void setSubPrice(double p) {
		subPrice = round(p);
		save();
	}

	public void setPaperPrice(double p) {
		paperPrice = round(p);
		save();
	}

	public void setAdPrice(double p) {
		adPrice = round(p);
		save();
	}

	public void setPrintCost(double p) {
		printCost = round(p);
		save();
	}

	public double getSubPrice() {
		return subPrice;
	}

	public double getPaperPrice() {
		return paperPrice;
	}

	public double getAdPrice() {
		return adPrice;
	}

	public double getPrintCost() {
		return printCost;
	}

	public String payRoll() {
		String result = "";
		Collection<Employee> vals = employeeRegistry.values();
		for(Employee e: vals) {
			double val =  e.getPaycheckValue();
			val = round(val);
			result += "\n";
			result += e.FullName() + ": "  + val;
		}
		return result;
	}

	public boolean resetEmployeeHours() {
		Collection<Employee> vals = employeeRegistry.values();
		for(Employee e: vals) {
			if(e instanceof HourlyEmployee) {
				((HourlyEmployee) e).resetHours();
			}
		}
		return true;
	}

	public String FinancialReport() {
		String result = "-------------------------\nRevenue:\n" + getRevenue() + "\n";
		result += "Expences:\n" + getExpences()  + "\n";
		result += "Total Profit:\n" + getProfit()  + "\n";
		result += "";

		return result;
	}

	private String getProfit() {
		String result = "";
		double profit = revenue - expenses;
		result += profit;
		return result;
	}

	private String getExpences() {
		expenses = 0;
		String result = "";
		double printCost = 0;
		double payrollCost = 0;

		Collection<Employee> eVals = employeeRegistry.values();
		for(Employee e: eVals) {
			double val =  e.getPaycheckValue();
			payrollCost += val;
		}

		Collection<Customer> dVals = customers.values();
		for(Customer d:dVals) {
			if (!(d instanceof Distributor)) continue;
			int papers = ((Distributor) d).paperCount();
			double val =  papers * this.printCost;
			printCost += val;
		}

		Collection<Subscription> sVals = subs.values();
		int subCount = sVals.size();
		double val =  subCount * this.printCost * 4;
		printCost += val;

		printCost = round(printCost);
		payrollCost = round(payrollCost);

		result += "printing costs: " + printCost + "\n";
		result += "payroll expences: " + payrollCost + "\n";
		expenses += printCost + payrollCost;
		result += "total: " + expenses + "\n";
		return result;
	}

	private String getRevenue() {
		String result = "";
		revenue = 0;
		double adRev = 0;
		double subRev = 0;
		double dstRev = 0;


		Collection<Ad> aVals = ads.values();
		adRev += aVals.size() * adPrice;
		adRev = round(adRev);

		Collection<Subscription> sVals = subs.values();
		int subCount = sVals.size();
		subRev =  subCount * subPrice;
		subRev = round(subRev);

		Collection<Customer> dVals = customers.values();
		for(Customer d:dVals) {
			if (!(d instanceof Distributor)) continue;
			int papers = ((Distributor) d).paperCount();
			double val =  papers * paperPrice;
			dstRev += val;
		}
		dstRev = round(dstRev);

		result += "income from ads: " + adRev + "\n";
		result += "income from subscriptions: " + subRev + "\n";
		result += "income from distribution: " + dstRev + "\n";
		revenue += adRev + subRev + dstRev;
		return result;
	}

	private double round(double a) {
		a = a*100;
		a = Math.round(a);
		a = a /100;
		return a;
	}

	private boolean save() {
		PrintWriter out = null;
		try {
			out = new PrintWriter(new BufferedWriter(new FileWriter(Global.FINANCE_DB_PATH, true)));
			out.print(toString());
		} catch (IOException e) {
			System.out.println("Finance file corrupted. Seek IT help.");
			return false;
		} finally {
			if (out != null) {
				out.close();
			}
		}
		return true;
	}
	@Override
	public String toString() {
		String result = "";
		result += subPrice;
		result += ", " + paperPrice;
		result += ", " + adPrice;
		result += ", " + printCost;
		return result;
	}

	@Override
	public String executeCommand(Employee loggedIn, Command command)
	{
		StringBuilder build = new StringBuilder();
		switch (command.getCommand())
		{
			case "get-revenue":
				build.append("Revenue Report:\n").append(getRevenue());
				break;
			case "get-expenses":
				build.append("Expenses Report:\n").append(getExpences());
				break;
			case "get-profit":
				build.append("Estimated Net Profits: ").append(getProfit());
				break;
			case "get-report":
				build.append("Financial Report:\n").append(FinancialReport());
				break;
			case "get-payroll":
				build.append("Payroll:").append(payRoll());
				break;
			case "reset-hours":
				resetEmployeeHours();
				build.append("Successfully reset Hourly-Employee hours");
				break;
			case "get-sub-price":
				build.append("Price of a subscription:\n").append(getSubPrice());
				break;
			case "get-ad-price":
				build.append("Price of an adn:\n").append(getAdPrice());
				break;
			case "get-paper-price":
				build.append("Price of a Newspaper:\n").append(getPaperPrice());
				break;
			case "get-print-cost":
				build.append("Post to print a paper:\n").append(getPrintCost());
				break;

			case "set-sub-price":
				if (command.getOptions().size() != 1) {
					build.append("set-sub-price requires 1 argument, the new price");
				}
				else {
					setSubPrice(Double.parseDouble(command.getOptions().get(0)));
					build.append("Price of a subscription updated");

				}
				break;
			case "set-ad-price":
				if (command.getOptions().size() != 1) {
					build.append("set-ad-price requires 1 argument, the new price");
				}
				else {
					setAdPrice(Double.parseDouble(command.getOptions().get(0)));
					build.append("Price of an ad updated");

				}
				break;
			case "set-paper-price":
				if (command.getOptions().size() != 1) {
					build.append("set-paper-price requires 1 argument, the new price");
				}
				else {
					setPaperPrice(Double.parseDouble(command.getOptions().get(0)));
					build.append("Price of a paper updated");

				}
				break;
			case "set-print-cost":
				if (command.getOptions().size() != 1) {
					build.append("set-print-price requires 1 argument, the new price");
				}
				else {
					setPrintCost(Double.parseDouble(command.getOptions().get(0)));
					build.append("Cost to print updated");

				}
				break;
		}
		return build.toString();
	}
}
