package application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class MainCashierController {

    private String loginedUser;

    @FXML
    private StackPane contentArea;

    @FXML
    private Button logoutButton;

    @FXML
    private HBox topBar;

    @FXML
    private Label userInfoLabel;

    public void setLoginedUser(String username) {
        this.loginedUser = username;
        userInfoLabel.setText("Cashier: " + username);
    }

    @FXML
    private void handleLogout() {
        try {
            // Login ekranına geri dön
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/Login.fxml"));
            Parent loginView = loader.load();

            // Mevcut pencereyi al ve içeriğini değiştir
            Scene scene = logoutButton.getScene();
            scene.setRoot(loginView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        if (userInfoLabel == null || contentArea == null) {
            throw new RuntimeException("FXML elements were not properly injected!");
        }

        // Logout butonu için event handler
        logoutButton.setOnAction(event -> handleLogout());

        // Login olan kullanıcı bilgilerini göster
        if (loginedUser != null) {
            userInfoLabel.setText("Cashier: " + loginedUser);
        }

        // İlk açılışta MovieSearch ekranını yükle
        loadMovieSearchScreen();
    }

    private void loadMovieSearchScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/MovieSearch.fxml"));
            Parent movieSearch = loader.load();

            MovieSearchController movieController = loader.getController();
            if (movieController == null) {
                throw new RuntimeException("Controller could not be loaded!");
            }
            movieController.setMainController(this);

            contentArea.getChildren().setAll(movieSearch);
        } catch (IOException e) {
            // Hata mesajını kullanıcıya göster
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Loading Error");
            alert.setContentText("Could not load movie search screen: " + e.getMessage());
            alert.showAndWait();
            e.printStackTrace();
        }
    }

    //ContentArea'ya erişim için getter
    public StackPane getContentArea() {
        return contentArea;
    }

    // Diğer ekranları yüklemek için genel metod
    public void loadScreen(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent view = loader.load();
            contentArea.getChildren().setAll(view);
        } catch (IOException e) {
            showError("Loading Error", "Could not load screen: " + e.getMessage());
        }
    }

    // Hata mesajları için yardımcı metod
    private void showError(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
