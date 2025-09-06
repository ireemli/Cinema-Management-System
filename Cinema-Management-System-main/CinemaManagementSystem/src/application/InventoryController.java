package application;

import java.io.IOException;
import java.util.ArrayList;

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
import database.DatabaseProduct;

public class InventoryController {

    private DatabaseProduct databaseProduct;

    @FXML
    private Button backToManagerMenu;
    
    @FXML
    private Button Logout;

    @FXML
    private TableColumn<Product, String> productName;

    @FXML
    private TableView<Product> productTable;

    @FXML
    private TableColumn<Product, Integer> stockStatus;
    

    @FXML
    private Button updateStockOK;

    @FXML
    private TextField updatedQuantity;

    @FXML

    public void initialize(){

        databaseProduct = new DatabaseProduct();
        databaseProduct.connectDatabase();

        productName.setCellValueFactory(new PropertyValueFactory<>("name"));
        stockStatus.setCellValueFactory(new PropertyValueFactory<>("stock"));
        loadInventory();
    }

    private void clearFields() {
        updatedQuantity.clear();
    }
    
    
    
    public void loadInventory(){
        //get the products from database
        ArrayList<Product> products = databaseProduct.viewInventory();

        //arraylist to observable list to set data on table
        ObservableList<Product> listofproducts = FXCollections.observableArrayList(products);
        productTable.setItems(listofproducts);
    }

    public void updateStock() {
        try {
            Product selectedProduct = productTable.getSelectionModel().getSelectedItem(); //selected product from table
            if (selectedProduct == null) {
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText("No Product Selected!");
                alert.setContentText("Select a product to update.");
                alert.showAndWait();
                return;
            }
    
            int newQuantity = Integer.parseInt(updatedQuantity.getText());
    
            if (newQuantity < 0) {
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText("Minus Quantity!");
                alert.setContentText("Stock cannot be a negative value.");
                alert.showAndWait();
                return;
            }
    
        
            databaseProduct.updateProduct(selectedProduct, "stock", newQuantity);//update the stock of the selected product in database
    
            selectedProduct.setStock(newQuantity); // Update the stock of the selected product on table
            clearFields(); // Clear the textfield
    
            // refresh the table
            productTable.refresh();

        } catch (NumberFormatException e) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Invalid Input!");
            alert.setContentText("Please enter a valid number.");
            alert.showAndWait();
        }
    }
    

    @FXML
    private void handleLogoutAction(ActionEvent event) throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/Login.fxml"));
    Parent root = loader.load();

    Scene scene = new Scene(root);
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    stage.setScene(scene);
    stage.show();
    }

    
    @FXML
    private void handleBackToManagerMenuAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/ManagerMenu.fxml"));
        Parent root = loader.load();
    
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
        }

}

