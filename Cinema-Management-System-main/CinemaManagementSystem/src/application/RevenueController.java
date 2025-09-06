package application;

import java.io.IOException;

import database.DatabaseProduct;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class RevenueController {

    @FXML
    private Button BackToManagerFromRevenue;

    @FXML
    private Button LogoutFromRevenue;

    @FXML
    private AnchorPane RefreshButton;

    @FXML
    private Label TotalRevenue;

    @FXML
    private Label TotalTaxes;

    public void initialize() {
        //bunu kullanıp databaseden double array çekilcek, sonra 0 ve 1 yazdırılcak.
        DatabaseProduct dataP = new DatabaseProduct();
        dataP.connectDatabase();
        double[] revenueData = dataP.viewRevenue();
        dataP.disconnectDatabase();
        TotalRevenue.setText("Total Revenue: " + revenueData[0]);       
        TotalTaxes.setText("Total Taxes: " + revenueData[1]);
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
