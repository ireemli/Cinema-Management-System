

import database.DatabaseMovies;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        // Resimleri yükle (sadece bir kez çalıştırın)
        DatabaseMovies db = new DatabaseMovies();
        db.loadAllMovieImages();

        // Login ekranını yükle
        Parent root = FXMLLoader.load(getClass().getResource("ui/Login.fxml"));

        // Sahneyi oluştur
        Scene scene = new Scene(root, 600, 600);

        // Stage ayarları
        primaryStage.setTitle("Group01 CinemaCenter");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public static void main(String[] args){
        launch(args);
    }
}