package newspaper.managers;

import java.util.Collection;
import java.util.HashMap;

import newspaper.models.Employee;

public class FinancialManager {
	HashMap<Integer, Employee> employeeRegistry;
	
	public FinancialManager(EmployeeManager eman) { 
		employeeRegistry = eman.allEmployees();
	}
	
	public String payRoll() {
		String result = "Payroll should be payed as follows:";
		Collection<Employee> vals = employeeRegistry.values();
		for(Employee e: vals) {
			result += "\n";
			result += e.FullName() + ": "  + e.getPaycheckValue();
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
		// TODO Auto-generated method stub
		return null;
	}

	private String getExpences() {
		// TODO Auto-generated method stub
		return null;
	}

	private String getRevenue() {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
