package newspaper.models;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import newspaper.Global;

public class HourlyEmployee extends Employee{
	
	double hourlyRate;
	double hoursWorked;
	
	public HourlyEmployee(int Id, String password, int supervisorId, String FullName, double hourlyRate, double hoursWorked) {
		super(Id, password, supervisorId, FullName);
		this.hourlyRate = hourlyRate;
		this.hoursWorked = hoursWorked;

	}
	
	public HourlyEmployee(int Id, String password, int supervisorId, String FullName, double hourlyRate) {
		super(Id, password, supervisorId, FullName);
		this.hourlyRate = hourlyRate;
		this.hoursWorked = 0;
	}
	
	public HourlyEmployee(int Id, String FullName) {
		super(Id, FullName);
	}
	
	@Override
	public int write()
    {
        // Write the employe to the db file
        String fileName = this.Id() + ".txt";
        String build = "";
        build += this.FullName() + "\n" + this.supervisorId() + "\n" + this.Password() + "\n";
        build += 1 + "\n" + this.hourlyRate() + "\n" + this.hoursWorked(); // 0 for salary and 1 for hourly

        try
        {
            BufferedWriter writer = new BufferedWriter(new FileWriter(Global.EMPLOYEE_DB_PATH + fileName));

            writer.write(build);

            writer.close();
        }
        catch (IOException e)
        {
            System.out.println("ERROR WRITING EMPLOYEE TO DB: " + e.getMessage());
            return -1;
        }
        return 0;
    }
	
	public double hoursWorked() {
		return hoursWorked;
	}
	
	public double hourlyRate() {
		return hourlyRate;
	}
	
	public void addHours(double hours) {
		hoursWorked += hours;
	}

	@Override
	public double getPaycheckValue() {
		return hoursWorked * hourlyRate;
	}
	
	public void resetHours() {
		hoursWorked = 0;
	}
}
