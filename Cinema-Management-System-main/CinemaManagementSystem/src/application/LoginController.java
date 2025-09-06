package application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Label errorLabel;

    @FXML
    private void initialize() {
        loginButton.setOnAction(event -> handleLogin());

        // Enter tuşuna basıldığında da login işlemi gerçekleşsin
        passwordField.setOnAction(event -> handleLogin());

        //Butona tıklandnığı zaman:
        loginButton.setOnAction(event -> handleLogin());
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showError("Please enter username and password");
            return;
        }

        try {
            // Kullanıcı bilgilerini kontrol et
            String userRole = authenticateUser(username, password);
            if (userRole != null) {
                loadAppropriateScreen(username, userRole);
            } else {
                showError("Invalid username or password");
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            showError("Login error: " + e.getMessage());
        }
    }

    private String authenticateUser(String username, String password) {
//KUllanıcıyı kontrol eder
        Authentication aut = new Authentication();
        Employee employee = aut.authenticatePassword(username, password);
        if(employee==null)
            return null;
        return employee.getRole();
    }

    private void loadAppropriateScreen(String username, String role) throws IOException {
        String fxmlFile;
        switch (role) {
            case "cashier":
                fxmlFile = "/ui/MainCashier.fxml";
                break;
            case "admin":
                fxmlFile = "/ui/Admin.fxml";
                break;
            case "manager":
                fxmlFile = "/ui/ManagerMenu.fxml";
                break;
            default:
                throw new IllegalStateException("Unknown role: " + role);
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        Parent root = loader.load();

        // Role göre controller'a kullanıcı bilgisini gönder
        switch (role) {
            case "cashier":
                MainCashierController cashierController = loader.getController();
                cashierController.setLoginedUser(username);
                break;
            case "admin":
                // Admin controller setup
                break;
            case "manager":
                // Manager controller setup
                break;
        }

        // Yeni sahneyi yükle
        Stage stage = (Stage) loginButton.getScene().getWindow();
        Scene scene = new Scene(root, 600, 600);
        stage.setScene(scene);
    }

    private void showError(String message) {
        errorLabel.setText(message);
    }
}