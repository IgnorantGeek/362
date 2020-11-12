package newspaper.models;

public class Task {
    private String description;
    private int employeeID;

    public Task(String desc, int id) {
        description  = desc;
        employeeID = id;
    }
    /**
     * gets the employee id number
     * @return employeeID
     */
    public int getID() {
        return employeeID;
    }

    public String getDesc() {
        return description;
    }

    @Override
    public String toString() {
        String result = "";
        result += employeeID;
        result += ", " + description;
        return result;
    }

}
