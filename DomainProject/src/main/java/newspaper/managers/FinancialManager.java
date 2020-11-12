package newspaper.managers;

import java.util.Collection;
import java.util.HashMap;

import newspaper.models.*;
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

	public FinancialManager(EmployeeManager eman, AdManager adman, SubscriptionManager sman, DistributionManager dman){

	}
	
	public FinancialManager(EmployeeManager eman, AdManager adman, SubscriptionManager sman, CustomerManager cman) {
		employeeRegistry = eman.getRegistry();
		subs = sman.getAll();
		ads = adman.getAll();
		customers = cman.getRegistry();
		
		subPrice = 12.00;
		paperPrice = 3.00;
		adPrice = 600.00;
		printCost = 0.50;
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
		}
		return build.toString();
	}
}
