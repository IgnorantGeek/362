package newspaper;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

public class EmployeeTest
{
     @Test
     public void CheckWrite()
     {
         // TODO
         // Test write a new employee to the database
         Employee test1 = new Employee(1, "Test User");

         test1.write();

         // Assert the file is in the database, and remove it.
         File testFile = new File("../Database/Employees/1.txt");
         assertTrue(testFile.exists());

         testFile.delete();
     }

     @Test
     public void CheckDelete()
     {
        // Test write a new employee to the database
        Employee test1 = new Employee(1, "Test User");

        test1.write();

        // Assert the file is in the database, and remove it.
        File testFile = new File("../Database/Employees/1.txt");
        assertTrue(testFile.exists());

        test1.delete();

        assertFalse(testFile.exists());
     }
}
