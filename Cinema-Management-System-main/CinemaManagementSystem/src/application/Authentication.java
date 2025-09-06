package application;

import database.DatabaseEmployee;

public class Authentication {
    private DatabaseEmployee database;
    private Employee employee;

    public DatabaseEmployee getdatabase() {
        return database;
    }

    public void setdatabase(DatabaseEmployee database) {
        this.database = database;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Authentication() {
        this.database = new DatabaseEmployee();
        this.database.connectDatabase();
        this.employee = null;
    }


    public Employee authenticatePassword(String username, String password) {
        employee = database.authEmployee(username);
        if ((employee.getPassword()).equals(password)) {
            // if (employee.getPassword().equals("password123")) {
                 // employee.setPasswordFirstTime();
            // }
            return employee;
        } else {
            System.out.println("Invalid password");
            return null;
        }
    }

    public void disconnect() {
        database.disconnectDatabase();
    }
}
