package newspaper.models;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import newspaper.Global;

public class HourlyEmployee extends Employee{
	
	double hourlyRate;
	double hoursWorked;
	
	public HourlyEmployee(int Id, String password, int supervisorId, String FullName, int clearance, double hourlyRate, double hoursWorked) {
		super(Id, FullName, password, supervisorId, clearance);
		this.hourlyRate = hourlyRate;
		this.hoursWorked = hoursWorked;

	}
	
	public HourlyEmployee(int Id, String password, int supervisorId, String FullName, int clearance, double hourlyRate) {
		super(Id, FullName, password, supervisorId, clearance);
		this.hourlyRate = hourlyRate;
		this.hoursWorked = 0;
	}
	
	@Override
	public int write()
    {
        // Write the employe to the db file
        String fileName = this.Id() + ".txt";
        String build = "";
        build += this.FullName() + "\n" + this.supervisorId() + "\n" + this.Password() + "\n";
        build += this.Clearance() + '\n' + 1 + "\n" + this.hourlyRate() + "\n" + this.hoursWorked();

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
