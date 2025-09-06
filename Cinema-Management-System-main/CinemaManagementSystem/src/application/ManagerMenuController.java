package application;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ManagerMenuController {

    @FXML
    private Button BackToLogin;

    @FXML
    private Button InventoryAction;

    @FXML
    private Button PersonnelAction;

    @FXML
    private Button PriceAction;

    @FXML
    private Button RevenueAction;

    @FXML
    void PersonnelActionsMenuButton(ActionEvent event) throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/PersonnelActions.fxml"));
    Parent root = loader.load();

    Scene scene = new Scene(root);
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    stage.setScene(scene);
    stage.show();
    }

    @FXML
    void PriceMenuButton(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/Price.fxml"));
        Parent root = loader.load();
    
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
        }

    @FXML
    void ReviewMenuButton(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/RevenueTaxes.fxml"));
        Parent root = loader.load();
    
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
        }

    @FXML
    void ViewInventoryButton(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/Inventory.fxml"));
        Parent root = loader.load();
    
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
        }

    @FXML
    void handleLogoutAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/Login.fxml"));
        Parent root = loader.load();
    
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
        }

    void handleBackToManagerMenuAction(ActionEvent event) {
        System.out.println("Back to Manager Menu action triggered.");
    }

}
