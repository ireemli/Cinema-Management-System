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

public class AdminController {

    @FXML
    private Button backFromAdminMenu;

    @FXML
    private Button movieManagementButton;

    @FXML
    private Button refundButton;

    @FXML
    private Button updateMovieButton;

    @FXML
    private Button backToLoginFromAdminButton;

    @FXML
    private Button logoutButton;

    @FXML
    void handleMovieManagementButton(ActionEvent event) throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/MovieManagement.fxml"));//loading new fxml
    Parent root = loader.load();

    Scene scene = new Scene(root);
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    stage.setScene(scene);
    stage.show();
    }

    @FXML
    void handleRefundButton(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/Refunds.fxml"));
        Parent root = loader.load();
    
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
        }
    
    @FXML
    void handleUpdateMovieButton(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/MonthlySchedules.fxml"));
        Parent root = loader.load();
    
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
        }
    
    @FXML
    void handleBackToLogin(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/Login.fxml"));
        Parent root = loader.load();
    
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
        }

    @FXML
    void handleLogoutButton(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/Login.fxml")); // Login.fxml dosyasını yükler
        Parent root = loader.load();

        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

}
