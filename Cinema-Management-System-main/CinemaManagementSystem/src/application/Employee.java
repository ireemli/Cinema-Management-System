package application;
import java.sql.Date;

public class Employee {
    private int employeeID;

    private String username;
    private String password;
    private String role;
    private String name;
    private String surname;
    private String phoneNo;
    private String email;

    private Date dateOfBirth;
    private Date dateOfStart;

    public Employee(){
        this.employeeID = 0;
        this.username = null;
        this.password = null;
        this.role = null;
        this.name = null;
        this.surname = null;
        this.phoneNo = null;
        this.dateOfBirth = null;
        this.dateOfStart = null;
        this.email = null;
    }
    public Employee( String name, String surname ,String username,String role){
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.role = role;
    }

    public Employee(int employeeID, String username, String password, String role, String name, String surname,
            String phoneNo, String email, Date dateOfBirth, Date dateOfStart) {
        this.employeeID = employeeID;
        this.username = username;
        this.password = password;
        this.role = role;
        this.name = name;
        this.surname = surname;
        this.phoneNo = phoneNo;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.dateOfStart = dateOfStart;
    }
    
    public int getEmployeeID() {
        return employeeID;
    }
    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getSurname() {
        return surname;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }
    public String getPhoneNo() {
        return phoneNo;
    }
    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public Date getDateOfBirth() {
        return dateOfBirth;
    }
    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    public Date getDateOfStart() {
        return dateOfStart;
    }
    public void setDateOfStart(Date dateOfStart) {
        this.dateOfStart = dateOfStart;
    }

}