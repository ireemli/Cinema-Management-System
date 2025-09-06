package application;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import database.DatabaseBill;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class RefundsController {

    @FXML
    private Button backFromRefunds;

    @FXML
    private TableColumn<Bills, ?> birthReceiptCol;

    @FXML
    private Button logoutFromRefund;

    @FXML
    private TableColumn<Bills, ?> nameReceiptCol;

    @FXML
    private TextField noOfReceiptText;

    @FXML
    private TableColumn<Bills, ?> noReceiptCol;

    @FXML
    private TableView<Bills> receiptTable;

    @FXML
    private Button refundButton;

    @FXML
    private Button searchByNameRefund;

    @FXML
    private TextField searchByNameText;

    @FXML
    private TableColumn<Bills, ?> taxReceiptCol;

    @FXML
    private TableColumn<Bills, ?> timeReceiptCol;

    @FXML
    private TableColumn<Bills, ?> totalReceiptCol;

    private DatabaseBill dataB = new DatabaseBill();
    private Bills selectedBill = new Bills();

    public void initialize(){
        dataB.connectDatabase();
        noReceiptCol.setCellValueFactory(new PropertyValueFactory<>("bill_id"));
        timeReceiptCol.setCellValueFactory(new PropertyValueFactory<>("time"));
        nameReceiptCol.setCellValueFactory(new PropertyValueFactory<>("customer"));
        birthReceiptCol.setCellValueFactory(new PropertyValueFactory<>("customerBirthDate"));
        totalReceiptCol.setCellValueFactory(new PropertyValueFactory<>("total_amount"));
        taxReceiptCol.setCellValueFactory(new PropertyValueFactory<>("tax_amount"));

        receiptTable.getSelectionModel().selectedItemProperty().addListener((obs,oldVal,newWal) -> {
            if(newWal != null) {
                selectedBill = newWal;
            }
        });
        loadBills();
        }
    
    private void loadBills(){

        //database'den verileri çek
        ArrayList<Bills> list = dataB.getBills();

        //observableliste çevir
        ObservableList<Bills> bills = FXCollections.observableArrayList(list);

        //tabloya geçir
        receiptTable.setItems(bills);
    }

    private void showInfo(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    @FXML
    void handleBackToAdminMenu(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/Admin.fxml"));
        Parent root = loader.load();
    
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
        }

    @FXML
    void handleLogout(ActionEvent event) throws IOException {
        dataB.disconnectDatabase();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/Login.fxml"));
        Parent root = loader.load();
    
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
        }

    void returnProducts(){};

    @SuppressWarnings("unused")
    @FXML
    void handleRefund(ActionEvent event) {

        if(selectedBill==null){
            showError("Please choose a bill");
            return;
        }
        dataB.returnBill(selectedBill);            
        boolean success = dataB.deleteBill(selectedBill.getBill_id());
        if(success==true){
            loadBills();
            showInfo("Bill Refunded Succesfully");
        }
        else
            showInfo("Delete failed! There is no bill with this id.");

    }

    @FXML
    void handleSearchByNameRefund(ActionEvent event) throws IOException{
        String customerName = searchByNameText.getText(); 

        if (customerName.isEmpty()){
            showError("Please enter a name ");//gets the name 
            return;
        }

        Bills bill = dataB.getBillCustomer(customerName);

        if(bill != null){
            ArrayList<Bills> list = new ArrayList<>();
            list.add(bill);
            ObservableList<Bills> bills = FXCollections.observableArrayList(list);
            receiptTable.setItems(bills);
        }
        else
            showError("Bill for Customer name not Found!");
    }

}
