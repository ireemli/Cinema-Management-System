package database;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;

import application.Employee;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseEmployee implements DatabaseSource{

    private Connection connection; 

    //Database'e bağlan
    public void connectDatabase(){

        try{
            connection = DriverManager.getConnection(url, username, password);
        }
        catch(SQLException sqlException){
            sqlException.printStackTrace();
        }

    }

    //Database query hazırlama
    public ResultSet executeQuery(String query){

        try{
            Statement statement = connection.createStatement();
            return statement.executeQuery(query);
        }
        catch(SQLException sqlException){
            sqlException.printStackTrace();
            return null;
        }
    }

    //Database query execute
    public int executeUpdate(String query){

        try(Statement statement = connection.createStatement()){

            return statement.executeUpdate(query);
        }
        catch(SQLException sqlException){
            sqlException.printStackTrace();
            return -1;
        }
    }

    public ArrayList<Employee> getEmployees(){

        ArrayList<Employee> employees = new ArrayList<Employee>();
        String query = "SELECT * FROM employee";

        try{

            ResultSet rs = executeQuery(query);

            while(rs != null && rs.next()){

                Employee employee = new Employee();

                employee.setEmployeeID(rs.getInt("idEmployee"));
                employee.setUsername(rs.getString("username"));
                employee.setPassword(rs.getString("password"));
                employee.setName(rs.getString("name"));
                employee.setSurname(rs.getString("surname"));
                employee.setPhoneNo(rs.getString("phone_no"));
                employee.setEmail(rs.getString("email"));
                employee.setDateOfBirth(rs.getDate("date_of_birth"));
                employee.setDateOfStart(rs.getDate("date_of_start"));
                employee.setRole(rs.getString("role"));
                employees.add(employee);
            }
        }
        catch(SQLException sqlException){
            sqlException.printStackTrace();
        }

        return employees;
    }

    public ArrayList<Employee> getEmployeesRole(String role){

        ArrayList<Employee> employees = new ArrayList<Employee>();
        String query = "SELECT * FROM employee";

        try{

            ResultSet rs = executeQuery(query);

            while(rs != null && rs.next()){

                Employee employee = new Employee();

                if(role.equalsIgnoreCase(rs.getString("role"))){
                    employee.setRole(rs.getString("role"));
                    employee.setEmployeeID(rs.getInt("employee_ID"));
                    employee.setUsername(rs.getString("username"));
                    employee.setPassword(rs.getString("password"));
                    employee.setName(rs.getString("name"));
                    employee.setSurname(rs.getString("surname"));
                    employee.setPhoneNo(rs.getString("phone_no"));
                    employee.setEmail(rs.getString("email"));
                    employee.setDateOfBirth(rs.getDate("date_of_birth"));
                    employee.setDateOfStart(rs.getDate("date_of_start"));
                    employees.add(employee);
                }

            }
        }
        catch(SQLException sqlException){
            sqlException.printStackTrace();
        }

        return employees;
    }

    public ArrayList<Employee> getEmployeeUsername(String username){

        username = "'" + username + "'";
        String query = "SELECT * FROM employee WHERE username LIKE " + username;

        ArrayList<Employee> employees = new ArrayList<>();

        try{

            ResultSet rs = executeQuery(query);

            if(rs.next()){
                   
                Employee employee = new Employee();
    
                employee.setRole(rs.getString("role"));
                employee.setEmployeeID(rs.getInt("idEmployee"));
                employee.setUsername(rs.getString("username"));
                employee.setPassword(rs.getString("password"));
                employee.setName(rs.getString("name"));
                employee.setSurname(rs.getString("surname"));
                employee.setPhoneNo(rs.getString("phone_no"));
                employee.setEmail(rs.getString("email"));
                employee.setDateOfBirth(rs.getDate("date_of_birth"));
                employee.setDateOfStart(rs.getDate("date_of_start"));
                
                employees.add(employee);
            }

        }
        catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
        return employees;

    }

    public Employee authEmployee(String username){

        username = "'" + username + "'";
        String query = "SELECT * FROM employee WHERE username = " + username;

        try{

            ResultSet rs = executeQuery(query);

            if(rs.next()){
                   
                Employee employee = new Employee();
    
                employee.setRole(rs.getString("role"));
                employee.setEmployeeID(rs.getInt("idEmployee"));
                employee.setUsername(rs.getString("username"));
                employee.setPassword(rs.getString("password"));
                employee.setName(rs.getString("name"));
                employee.setSurname(rs.getString("surname"));
                employee.setPhoneNo(rs.getString("phone_no"));
                employee.setEmail(rs.getString("email"));
                employee.setDateOfBirth(rs.getDate("date_of_birth"));
                employee.setDateOfStart(rs.getDate("date_of_start"));
                    
                return employee;
            }
            else
                return null;

        }
        catch(SQLException sqlException){
            sqlException.printStackTrace();
            return null;
        }

    }

    public void insertEmployee(Employee employee){

        String query = "INSERT INTO employee (username, password, name, surname, phone_no, email, date_of_birth" + 
        ", date_of_start, role) VALUES ( ? , ? , ? , ? , ? , ? , ? , ? , ?)";

        try(PreparedStatement pStatement = connection.prepareStatement(query)){
            
            pStatement.setString(1, employee.getUsername());
            pStatement.setString(2,employee.getPassword());
            pStatement.setString(3,employee.getName());
            pStatement.setString(4,employee.getSurname());
            pStatement.setString(5,employee.getPhoneNo());
            pStatement.setString(6,employee.getEmail());
            pStatement.setDate(7,employee.getDateOfBirth());
            pStatement.setDate(8,employee.getDateOfStart());
            pStatement.setString(9,employee.getRole());

            if (pStatement.executeUpdate() > 0)
                System.out.println("Employee inserted successfully!");
            else
                System.out.println("Insert failed!");
        }
        catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
    }

    public void updateEmployee(Employee employee, String column, String value){

        int ID = employee.getEmployeeID();
        String query = "UPDATE employee SET " + column + " = ? WHERE idEmployee = ? ";
        try(PreparedStatement pStatement = connection.prepareStatement(query);){

            pStatement.setString(1, value);
            pStatement.setInt(2, ID);

            if (pStatement.executeUpdate() > 0)
                System.out.println("Employee updated successfully!");
            else
                System.out.println("Update failed.");

        }
        catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
    }

    public boolean uniqueness(String column, String value){

        String query = "SELECT EXISTS (SELECT 1 FROM employee WHERE " + column +  " = ? )";
        try(PreparedStatement pStatement = connection.prepareStatement(query);){

            pStatement.setString(1, value);

            try(ResultSet rs = pStatement.executeQuery()){

            if (rs.next())
                return !rs.getBoolean(1);
            }

        }
        catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
        return false;
    }

    public void deleteEmployee(Employee employee){

        int ID = employee.getEmployeeID();
        String query = "DELETE FROM employee WHERE idEmployee = ?";
        try(PreparedStatement pStatement = connection.prepareStatement(query)){
            pStatement.setInt(1, ID);
            if (pStatement.executeUpdate() > 0)
                System.out.println("Employee deleted successfully!");
            else
                System.out.println("Delete failed!");
        }
        catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
    }

    public void disconnectDatabase(){

        try{
            if(connection!=null && !connection.isClosed())
                connection.close();
            else
                System.out.println("Disconnection error!");
        }
        catch(SQLException sqlException){
        }
    }
}
