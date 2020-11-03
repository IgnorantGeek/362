package newspaper.models;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import newspaper.Global;

public class SalaryEmployee extends Employee {
	double salary;
	
	public SalaryEmployee(int Id, String FullName) {
		super(Id, FullName);
	}
	
	public SalaryEmployee(int Id, String password, int supervisorId, String FullName, double salary) {
		super(Id, password, supervisorId, FullName);
		this.salary = salary;
	}
	
	@Override
	public int write()
    {
        // Write the employe to the db file
        String fileName = this.Id() + ".txt";
        String build = "";
        build += this.FullName() + "\n" + this.supervisorId();
        build += 0 + "\n" + this.salary(); // 0 for salary and 1 for hourly

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
	
	public double salary() {
		return salary;
	}
	
	@Override
	public double getPaycheckValue() {
		return (salary/12);
	}
}
