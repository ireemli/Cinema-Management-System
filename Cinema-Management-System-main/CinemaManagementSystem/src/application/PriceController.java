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
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class PriceController {

    @FXML
    private Button ApplyDiscount18;

    @FXML
    private Button ApplyDiscount60;

    @FXML
    private Button BackToManagerMenu;

    @FXML
    private Button BackToManagerMenu1;

    @FXML
    private Label DiscountRate1;

    @FXML
    private Label DiscountRate2;

    @FXML
    private Label EditPrice;

    @FXML
    private Button Logout;

    @FXML
    private Button Logout1;

    @FXML
    private Tab Prices;

    @FXML
    private Tab Prices1;

    @FXML
    private Button SetPricesButton;

    @FXML
    private TableColumn<Product, String> category;

    @FXML
    private TableColumn<Product, Double> categoryPrice;

    @FXML
    private TextField newDiscountRate18;

    @FXML
    private TextField newDiscountRate60;

    @FXML
    private TableView<Product> personnelAction1;

    @FXML
    private TextField setNewPrice1;

    @FXML
    private TextField setNewPrice2;

    @FXML
    private TextField setNewPrice3;

    @FXML
    private TextField setNewPrice4;

    public void initialize(){
        loadPriceList();
        


        }


    private void loadPriceList(){

        category.setCellValueFactory(new PropertyValueFactory<>("name"));
        categoryPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        //database'den verileri al
        ArrayList<Product> list = new ArrayList<>();
        list.add(new Product(1,"ticket price", 1.20,0));
        list.add(new Product(2,"beverage", 0.80,0));
        list.add(new Product(3,"biscuit", 2.50,0));
        list.add(new Product(4,"toy", 2.50,0));
        ObservableList<Product> products = FXCollections.observableArrayList(list);
        //verileri table a yükle
        personnelAction1.setItems(products);

    }

    @FXML
    @SuppressWarnings("unused")
    private void editPriceList(){
        try{
        //inputları kullanıcıdan al
        double newPrice1 = Double.parseDouble(setNewPrice1.getText());
        double newPrice2 = Double.parseDouble(setNewPrice2.getText());
        double newPrice3 = Double.parseDouble(setNewPrice3.getText());
        double newPrice4 = Double.parseDouble(setNewPrice4.getText());

        //database'e üstteki değerleri doldur
        
        //input exception
        if (newPrice1 < 0 || newPrice2 < 0 || newPrice3 < 0 || newPrice4 < 0){
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText("Price Minus!");
                alert.setContentText("Stock cannot be a minus value");
                alert.showAndWait();
            return;
            }

        loadPriceList();
        } catch (NumberFormatException e) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Wrong Value!");
            alert.setContentText("Please Fill All Values");
            alert.showAndWait();
        }

    }

    @FXML
    @SuppressWarnings("unused")
    private void handleLogoutAction(ActionEvent event) throws IOException {
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/ManagerMenu.fxml"));
        Parent root = loader.load();
    
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
        }

}
