package application;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import database.DatabaseEmployee;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Labeled;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
public class PersonnelActionsController {

    @FXML
    private TextField EditEmail;

    @FXML
    private TableColumn<Employee, String> EditEmailCol;
    
    @FXML
    private TextField HireEmail;
    
    @FXML
    private TableColumn<Employee, String> email;

    @FXML
    private TableColumn<Employee, String> fireemail;

    @FXML
    private TextField EditPhoneNo;

    @FXML
    private TableColumn<Employee, String> EditPhoneNoCol;

    @FXML
    private TextField HirePhoneNo;

    @FXML
    private TableColumn<Employee, String> phoneno;

    @FXML
    private TableColumn<Employee, String> firephoneno;

    @FXML
    private Button ApplyHire;

    @FXML
    private DatePicker Birthdate;

    @FXML
    private Button ClickFire;

    @FXML
    private DatePicker EditBirthDate;

    @FXML
    private TableColumn<Employee, Date> EditBirthDateCol;

    @FXML
    private TextField EditFirstName;

    @FXML
    private TableColumn<Employee, String> EditFirstNameCol;

    @FXML
    private TableColumn<Employee, Integer> EditIdCol;

    @FXML
    private TextField EditLastName;

    @FXML
    private TextField EditPassword;

    @FXML
    private TableColumn<Employee, String> EditPasswordCol;

    @FXML
    private ChoiceBox<String> EditRole;

    @FXML
    private TableColumn<Employee, String> EditRoleCol;

    @FXML
    private DatePicker EditStartDate;

    @FXML
    private TableColumn<Employee, Date> EditStartDateCol;

    @FXML
    private TableColumn<Employee, String> EditSurnameCol;

    @FXML
    private TextField EditUsername;

    @FXML
    private TableColumn<Employee, String> EditUsernameCol;

    @FXML
    private TextField HireFirstName;

    @FXML
    private TextField HireLastName;

    @FXML
    private TextField HirePassword;

    @FXML
    private TextField HireUsername;

    @FXML
    private Button Logout;

    @FXML
    private Button Logout1;

    @FXML
    private Button Logout2;

    @FXML
    private Button Logout3;

    @FXML
    private Button PersonnelEditApply;

    @FXML
    private ChoiceBox<String> HireRoleChoice;

    @FXML
    private Button SearchEditClick;

    @FXML
    private Button SearchFireClick;

    @FXML
    private TextField SearchPersonnelEdit;

    @FXML
    private TextField SearchPersonnelFire;

    @FXML
    private DatePicker StartDate;

    @FXML
    private Button backToManagerMenu;

    @FXML
    private Button backToManagerMenu1;

    @FXML
    private Button backToManagerMenu2;

    @FXML
    private Button backToManagerMenu21;

    @FXML
    private TableColumn<Employee, Date> birthdate;

    @FXML
    private TableColumn<Employee, Integer> employeeid;

    @FXML
    private TableColumn<Employee, Date> firebirthdate;

    @FXML
    private TableColumn<Employee, Integer> fireid;

    @FXML
    private TableColumn<Employee, String> firename;

    @FXML
    private TableColumn<Employee, String> firepassword;

    @FXML
    private TableColumn<Employee, String> firerole;

    @FXML
    private TableColumn<Employee, Date> firestartdate;

    @FXML
    private TableColumn<Employee, String> firesurname;

    @FXML
    private TableColumn<Employee, String> fireusername;

    @FXML
    private TableColumn<Employee, String> firstName;

    @FXML
    private TableColumn<Employee, String> lastName;

    @FXML
    private TableColumn<Employee, String> password;

    @FXML
    private TableView<Employee> personnelAction;

    @FXML
    private TableView<Employee> personnelAction1;

    @FXML
    private TableView<Employee> personnelAction2;

    @FXML
    private TableColumn<Employee, String> role;

    @FXML
    private TableColumn<Employee, Date> startdate;

    @FXML
    private TableColumn<Employee, String> username;

    private Employee currentEmployee;
    private Employee currentEmployee1;
    private Employee currentEmployee2;


    private DatabaseEmployee dataE = new DatabaseEmployee();


    @FXML 
    public void initialize(){
        
        HireRoleChoice.setItems(FXCollections.observableArrayList("Admin", "Manager", "Cashier"));
        EditRole.setItems(FXCollections.observableArrayList("Admin", "Manager", "Cashier"));
        configureTableColumns(personnelAction);
        configureTableColumns(personnelAction1);
        configureTableColumns(personnelAction2);
        dataE.connectDatabase();

        personnelAction.getSelectionModel().selectedItemProperty().addListener((obs,oldVal,newWal) -> {
            if(newWal != null)
                currentEmployee = newWal;});

        personnelAction1.getSelectionModel().selectedItemProperty().addListener((obs,oldVal,newWal) -> {
            if(newWal != null)
                currentEmployee1 = newWal;});

        personnelAction2.getSelectionModel().selectedItemProperty().addListener((obs,oldVal,newWal) -> {
            if(newWal != null)
                currentEmployee2 = newWal;});

        loadPersonnelTable();

    }

    private void configureTableColumns(TableView<Employee> tableView){
        employeeid.setCellValueFactory(new PropertyValueFactory<>("employeeID"));
        firstName.setCellValueFactory(new PropertyValueFactory<>("name"));
        lastName.setCellValueFactory(new PropertyValueFactory<>("surname"));
        username.setCellValueFactory(new PropertyValueFactory<>("username"));
        password.setCellValueFactory(new PropertyValueFactory<>("password"));
        email.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneno.setCellValueFactory(new PropertyValueFactory<>("phoneNo"));
        role.setCellValueFactory(new PropertyValueFactory<>("role"));
        birthdate.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        startdate.setCellValueFactory(new PropertyValueFactory<>("dateOfStart"));

        fireid.setCellValueFactory(new PropertyValueFactory<>("employeeID"));
        firename.setCellValueFactory(new PropertyValueFactory<>("name"));
        firesurname.setCellValueFactory(new PropertyValueFactory<>("surname"));
        fireusername.setCellValueFactory(new PropertyValueFactory<>("username"));
        firepassword.setCellValueFactory(new PropertyValueFactory<>("password"));
        fireemail.setCellValueFactory(new PropertyValueFactory<>("email"));
        firephoneno.setCellValueFactory(new PropertyValueFactory<>("phoneNo"));
        firerole.setCellValueFactory(new PropertyValueFactory<>("role"));
        firebirthdate.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        firestartdate.setCellValueFactory(new PropertyValueFactory<>("dateOfStart"));

        EditIdCol.setCellValueFactory(new PropertyValueFactory<>("employeeID"));
        EditFirstNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        EditSurnameCol.setCellValueFactory(new PropertyValueFactory<>("surname"));
        EditUsernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        EditPasswordCol.setCellValueFactory(new PropertyValueFactory<>("password"));
        EditEmailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        EditPhoneNoCol.setCellValueFactory(new PropertyValueFactory<>("phoneNo"));
        EditRoleCol.setCellValueFactory(new PropertyValueFactory<>("role"));
        EditBirthDateCol.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        EditStartDateCol.setCellValueFactory(new PropertyValueFactory<>("dateOfStart"));
    }

    private void loadPersonnelTable(){

        ArrayList<Employee> employeeList = dataE.getEmployees();

        ObservableList<Employee> employee = FXCollections.observableArrayList(employeeList);
        ObservableList<Employee> employee2 = FXCollections.observableArrayList(employeeList);
        ObservableList<Employee> employee3 = FXCollections.observableArrayList(employeeList);

        personnelAction.setItems(employee);
        personnelAction1.setItems(employee2);
        personnelAction2.setItems(employee3);

    }


    @FXML
    @SuppressWarnings("unused")
    private void firesearchemployee(){

        String searchText = SearchPersonnelFire.getText();

        if(searchText.isEmpty()){
            System.err.println("Please enter a value to search!");
        }
        //databaseden employee çekme

        ArrayList<Employee> employee = dataE.getEmployeeUsername(searchText);

        if (employee == null) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Employee Not Found!");
            alert.setContentText("No employee found with the username: " + searchText);
            alert.showAndWait();
        return;
        }

        firesearchloadtable(employee);
        
    }


    //bi üstteki fonksiyonda kullanıldı

    private void firesearchloadtable(ArrayList<Employee> employeeList){

        ObservableList<Employee> fireemployee = FXCollections.observableArrayList(employeeList);
        personnelAction1.setItems(fireemployee);
    }


    //onaction ile tuşa eklenicek
    @FXML 
    @SuppressWarnings("unused")
    private void fireemployee(){
        dataE.deleteEmployee(currentEmployee1);
    }
    

    @FXML
    @SuppressWarnings("unused")
    private void editsearchemployee(){

        String searchText = SearchPersonnelEdit.getText();

        if(searchText.isEmpty()){
            System.err.println("Please enter a value to search!");
        }
        //databaseden employee çekme
        ArrayList<Employee> employees = dataE.getEmployeeUsername(searchText);

        if (employees == null) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Employee Not Found!");
            alert.setContentText("No employee found with the username: " + searchText);
            alert.showAndWait();
        return;
        }

        editsearchloadtable(employees);
        
    }

    
    private void editsearchloadtable(ArrayList<Employee> employeeList){

        ObservableList<Employee> editemployee = FXCollections.observableArrayList(employeeList);
        personnelAction2.setItems(editemployee);
    }

    private boolean nameError(String str){
        if (str.matches("[a-zA-ZÇçĞğİıÖöŞşÜü]+")){
            return false;
        } else{
            return true;
        }
    }
    
    @FXML 
    @SuppressWarnings("unused")
    private void editemployee(){

        Employee employee = currentEmployee2;

        String name = EditFirstName.getText();

        if(!name.isEmpty()){
            if(nameError(name)){
                showError("Please fill customer name with only alphabetical characters");
                return;
            }
            if(name.length()>50){
                showError("Customer name too long. Maximum 50 character.");
                return;
            }
            dataE.updateEmployee(employee, "name", name);
        }

        String surname = EditLastName.getText();

        if(!surname.isEmpty()){
            if(nameError(name)){
                showError("Please fill customer surname with only alphabetical characters");
                return;
            }
            if(surname.length()>50){
                showError("Customer name too long. Maximum 50 character.");
                return;
            }
            dataE.updateEmployee(employee, "surname",surname);
        }

        String username = EditUsername.getText();

        if(!username.isEmpty()){
            if(username.matches("^[a-zA-Z0-9]+$")){
                if(username.length()>50){
                    showError("Customer name too long. Maximum 50 character.");
                    return;
                }
                dataE.updateEmployee(employee, "username", username);
            }
            else{
                showError("Please fill customer username with only alphanumerical characters");
                return;
            }
        }

        String password = EditPassword.getText();

        if(!password.isEmpty()){
            if(password.matches("^[a-zA-Z0-9]+$")){
                if(password.length()>70){
                    showError("Password name too long. Maximum 70 character.");
                    return;
                }
                dataE.updateEmployee(employee, "password", password);
            }
            else{
                showError("Please fill customer password with only alphanumerical characters");
                return;
            }
        }

        String email = EditEmail.getText();

        if(!email.isEmpty()){
            if(email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")){
                if(email.length()>45){
                    showError("Password name too long. Maximum 45 character.");
                    return;
                }
                dataE.updateEmployee(employee, "email", email);
            }
            else{
                showError("Please fill vaild email address.");
                return;
            }
        }

        String phoneNo = EditPhoneNo.getText();

        if(!phoneNo.isEmpty()){
            if(phoneNo.matches("^(\\\\+90|0)?5\\\\d{2} \\\\d{3} \\\\d{2} \\\\d{2}$"))
                dataE.updateEmployee(employee, "phone_no", phoneNo);
            else{
                showError("Please fill valid phone number.");
                return;
            }
        }
        String role = EditRole.getValue();

        if(role != null) dataE.updateEmployee(employee, "role", EditRole.getValue());

        LocalDate startDate = EditStartDate.getValue();
    
        LocalDate birthdate = EditBirthDate.getValue();

        if(birthdate != null){
            if (Period.between(birthdate, LocalDate.now()).getYears() < 18) {
                showError("Employee must be at least 18 years old!");
                return;
            }
            dataE.updateEmployee(employee, "date_of_birth", birthdate.toString());
        }

        if(startDate!=null){ 
            if (Period.between(employee.getDateOfBirth().toLocalDate(), startDate).getYears() < 18) {
                showError("Start date must be at least 18 years after the birthdate!");
                return;
            }
            dataE.updateEmployee(employee, "date_of_start", startDate.toString());
        }
        loadPersonnelTable();
    }


     @FXML
    @SuppressWarnings("unused")
    private void handleLogoutAction(ActionEvent event) throws IOException {
        dataE.disconnectDatabase();
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/Login.fxml"));
    Parent root = loader.load();

    Scene scene = new Scene(root);
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    stage.setScene(scene);
    stage.show();
    }

    @FXML
    @SuppressWarnings("unused")
    private void handleBackToManagerMenuAction(ActionEvent event) throws IOException {
        dataE.disconnectDatabase();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/ManagerMenu.fxml"));
        Parent root = loader.load();
    
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
        }
    
    @FXML
    @SuppressWarnings("unused")
    private void hireemployee(){

        Employee employee = new Employee();

        if (HireFirstName.getText().isEmpty() || 
        HireLastName.getText().isEmpty() || 
        HireUsername.getText().isEmpty() || 
        HirePassword.getText().isEmpty() || 
        HireEmail.getText().isEmpty() || 
        HirePhoneNo.getText().isEmpty() || 
        HireRoleChoice.getValue().isEmpty() || 
        Birthdate.getValue() == null || 
        StartDate.getValue() == null) {
        
            showError("Please fill all the blanks!");
        return;
    }

        LocalDate birthdate = Birthdate.getValue();
        LocalDate startDate = StartDate.getValue();

        if (Period.between(birthdate, LocalDate.now()).getYears() < 18) {
            showError("Employee must be at least 18 years old!");
        return;
    }
        if (Period.between(birthdate, startDate).getYears() < 18) {
            showError("Start date must be at least 18 years after the birthdate!");
            return;
    }



        String name = HireFirstName.getText();

        if(nameError(name)){
            showError("Please fill customer name with only alphabetical characters");
            return;
        }
        else if(name.length()>50){
            showError("Customer name too long. Maximum 50 character.");
            return;
        }
        else
            employee.setName(name);

        String surname = HireLastName.getText();

        if(nameError(surname)){
            showError("Please fill customer surname with only alphabetical characters");
            return;
        }
        else if(surname.length()>50){
            showError("Customer name too long. Maximum 50 character.");
            return;
        }
        else employee.setSurname(surname);

        String username = HireUsername.getText();
        
        if(username.matches("^[a-zA-Z0-9]+$")){
            if(username.length()>50){
            showError("Customer name too long. Maximum 50 character.");
                return;
            }
            employee.setUsername(username);
        }
        else{
            showError("Please fill customer username with only alphanumerical characters");
            return;
        }
        
        String password = HirePassword.getText();

        if(password.matches("^[a-zA-Z0-9]+$")){
            if(password.length()>70){
                showError("Password name too long. Maximum 70 character.");
                return;
            }
            employee.setPassword(password);
        }
        else{
            showError("Please fill customer password with only alphanumerical characters");
            return;
        }

        String email = HireEmail.getText();

        if(email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")){
            if(email.length()>45){
                showError("Password name too long. Maximum 45 character.");
                return;
            }
            employee.setEmail(email);
        }
        else{
            showError("Please fill vaild email address.");
            return;
        }

        String phoneNo = HirePhoneNo.getText();

        System.out.println(phoneNo);
        if (phoneNo.matches("^(\\+90|0)?5\\d{2} \\d{3} \\d{2} \\d{2}$"))
            employee.setPhoneNo(phoneNo);
        else{
            showError("Please fill valid phone number.");
        return;
        }


        employee.setRole(HireRoleChoice.getValue());
        employee.setDateOfBirth(Date.valueOf(birthdate));
        employee.setDateOfStart(Date.valueOf(startDate));

        dataE.insertEmployee(employee);
        showDone("Employee hired succesfully!");
        loadPersonnelTable();
    }

    private void showError(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private  void showDone(String message){
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Operation Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
