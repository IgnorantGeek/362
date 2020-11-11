package newspaper.managers;

import java.util.Collection;
import java.util.HashMap;

import newspaper.models.Ad;
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
	HashMap<String, Distributor> distributers;
	
	double subPrice;
	double paperPrice;
	double adPrice;
	double printCost;
	double revenue;
	double expenses;
	
	public FinancialManager(EmployeeManager eman, AdManager adman, SubscriptionManager sman, DistributionManager dman) { 
		employeeRegistry = eman.getRegistry();
		subs = sman.getAll();
		ads = adman.getAll();
		distributers = dman.getAll();
		
		subPrice = 12.00;
		paperPrice = 3.00;
		adPrice = 600.00;
		printCost = 0.50;
	}
	
	public String payRoll() {
		String result = "Payroll should be payed as follows:";
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
		String result = "This month's Financial Report\n\nRevenue:\n" + getRevenue() + "\n";
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
		
		Collection<Distributor> dVals = distributers.values();
		for(Distributor d:dVals) {
			int papers = d.paperCount();
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
		
		Collection<Distributor> dVals = distributers.values();
		for(Distributor d:dVals) {
			int papers = d.paperCount();
			double val =  papers * paperPrice;
			dstRev += val;
		}
		dstRev = round(dstRev);
		
		result += "income from ads: " + adRev + "\n";
		result += "income from subscriptions: " + subRev + "\n";
		result += "income from distributers: " + dstRev + "\n";
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
		// TODO
		return null;
	}
}
