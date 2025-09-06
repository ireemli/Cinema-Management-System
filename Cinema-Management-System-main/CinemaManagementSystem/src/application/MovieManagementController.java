package application;

import javafx.embed.swing.SwingFXUtils;
import javafx.stage.FileChooser;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import javafx.scene.image.Image;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.embed.swing.SwingFXUtils;
import javax.imageio.ImageIO;

import database.DatabaseMovies;

import java.io.ByteArrayOutputStream;

public class MovieManagementController {

    @FXML
    private TableView<Movie> TableMovieManagement;

    @FXML
    private TableColumn<Movie, String> tableMovieTitle;

    @FXML
    private TableColumn<Movie, String> tableMovieGenre;

    @FXML
    private TableColumn<Movie, String> tableMovieSummary;

    @FXML
    private TableColumn<Movie, String> tableMoviePoster;

    @FXML
    private TableColumn<Movie, Integer> tableMovieYear;

    @FXML
    private TextField TitleAddMovie;

    @FXML
    private TextField GenreAddMovie;

    @FXML
    private TextField SummaryAddMovie;

    @FXML
    private ImageView PosterAddMovieImageView;

    @FXML
    private TextField YearAddMovie;

    private ObservableList<Movie> movieList;
    private DatabaseMovies mov = new DatabaseMovies();
    //Seçilen film bilgilerini tutacak model sınıf

    @FXML
    public void initialize() {
        movieList = FXCollections.observableArrayList();

        // Tablodaki sütunlarla Movie sınıfının özelliklerini eşleştir
        tableMovieTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        tableMovieYear.setCellValueFactory(new PropertyValueFactory<>("year"));
        tableMovieGenre.setCellValueFactory(new PropertyValueFactory<>("genre"));
        tableMovieSummary.setCellValueFactory(new PropertyValueFactory<>("summary"));
        tableMoviePoster.setCellValueFactory(new PropertyValueFactory<>("poster"));

        // Tablonun veri setini ayarla
        TableMovieManagement.setItems(movieList);
        movieList.setAll(showMovies());
        // Veritabanından filmleri yükle
    }

    @FXML
    void handleAddButton(ActionEvent event) {
        String title = TitleAddMovie.getText().trim();
        String genre = GenreAddMovie.getText().trim();
        String summary = SummaryAddMovie.getText().trim();
        String year = YearAddMovie.getText().trim();
        int yearval = 0;
        Image posterImage = PosterAddMovieImageView.getImage();


        if (title.isEmpty() || genre.isEmpty() || summary.isEmpty() || year.isEmpty() || posterImage == null) {
            showAlert(Alert.AlertType.WARNING, "Missing Information", "Please fill in all fields.");
            return;
        }

        if (title.length() > 100) {
            showAlert(Alert.AlertType.WARNING, "Title Too Long", "The title cannot be longer than 100 characters.");
            return;
        }

        if (genre.length() > 100) {
            showAlert(Alert.AlertType.WARNING, "Genre Too Long", "The title cannot be longer than 100 characters.");
            return;
        }

        if (summary.length() > 700) {
            showAlert(Alert.AlertType.WARNING, "Summary Too Long", "The summary cannot be longer than 700 characters.");
            return;
        }
    
        try {
            int yearValue = Integer.parseInt(year);
            if (yearValue < 1850 || yearValue > 2025) {
                showAlert(Alert.AlertType.WARNING, "Invalid Year", "Please enter a valid year (1850-2025).");
                return;
            }
            yearval=yearValue;
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Invalid Year", "Year must be a number.");
            return;
        }


        try {
            // Image'i byte array'e çevir
            byte[] posterBytes = convertImageToByteArray(posterImage);

            // Movie nesnesini oluştur
            Movie newMovie = new Movie(0, title, yearval, genre, summary, posterBytes);

            // Veritabanına ekle
            addMovieToDatabase(newMovie);
            movieList.add(newMovie);
            clearInputFields();

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while adding the movie to the database.");
        }
    }

    private byte[] convertImageToByteArray(Image image) {
        try {
            BufferedImage bufferImage = SwingFXUtils.fromFXImage(image, null);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            // jpg yerine png kullan
            ImageIO.write(bufferImage, "png", outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Error converting image to byte array", e);
        }
    }

    @FXML
    void handleDeleteButton(ActionEvent event) {
        Movie selectedMovie = TableMovieManagement.getSelectionModel().getSelectedItem();

        if (selectedMovie == null) {
            showAlert(Alert.AlertType.INFORMATION, "Row Not Selected", "Please select a row to delete.");
            return;
        }

        try {
            deleteMovieFromDatabase(selectedMovie);
            movieList.remove(selectedMovie);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while deleting the movie.");
        }
    }

    @FXML
    void handleUpdateButton(ActionEvent event) {
        Movie selectedMovie = TableMovieManagement.getSelectionModel().getSelectedItem();

        if (selectedMovie == null) {
            showAlert(Alert.AlertType.INFORMATION, "Row Not Selected", "Please select a row to update.");
            return;
        }

        String newTitle = TitleAddMovie.getText().trim();
        String newGenre = GenreAddMovie.getText().trim();
        String newSummary = SummaryAddMovie.getText().trim();
        String newYear = YearAddMovie.getText().trim();
        Image newPosterImage = PosterAddMovieImageView.getImage();

        boolean isUpdated = false;

        if (newTitle.length() > 100) {
            showAlert(Alert.AlertType.WARNING, "Title Too Long", "The title cannot be longer than 100 characters.");
            return;
        }

        if (newGenre.length() > 100) {
            showAlert(Alert.AlertType.WARNING, "Genre Too Long", "The title cannot be longer than 100 characters.");
            return;
        }

        if (newSummary.length() > 700) {
            showAlert(Alert.AlertType.WARNING, "Summary Too Long", "The summary cannot be longer than 700 characters.");
            return;
        }

        if (!newTitle.isEmpty() && !newTitle.equals(selectedMovie.getTitle())) {
            selectedMovie.setTitle(newTitle);
            isUpdated = true;
        }

        if (!newGenre.isEmpty() && !newGenre.equals(selectedMovie.getGenre())) {
            selectedMovie.setGenre(newGenre);
            isUpdated = true;
        }

        if (!newSummary.isEmpty() && !newSummary.equals(selectedMovie.getSummary())) {
            selectedMovie.setSummary(newSummary);
            isUpdated = true;
        }

        if (!newYear.isEmpty()) {
            try {
                int yearValue = Integer.parseInt(newYear);
                if (yearValue < 1850 || yearValue > 2025){
                    showAlert(Alert.AlertType.WARNING, "Invalid Year", "Please enter a valid year (1850-2025).");
                    return;
                }
                if (yearValue != selectedMovie.getYear()) {
                    selectedMovie.setYear(yearValue);
                    isUpdated = true;
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.WARNING, "Invalid Year", "Year must be a number.");
                return;
            }
        }

        if (newPosterImage != null) {
            try {
                byte[] newPosterBytes = convertImageToByteArray(newPosterImage);
                selectedMovie.setPosterImage(newPosterBytes);
                isUpdated = true;
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Could not process the new poster image.");
                return;
            }
        }

        if (isUpdated) {
            try {
                updateMovieInDatabase(selectedMovie);
                TableMovieManagement.refresh();
                clearInputFields();
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", "There was an error updating the movie.");
            }
        } else {
            showAlert(Alert.AlertType.INFORMATION, "No Update Performed", "No field has been changed.");
        }
    }

    @FXML
    private void handleChoosePosterButton(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Poster");
        // Önce png destekleyen filtreler
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PNG Images", "*.png"),
                new FileChooser.ExtensionFilter("All Images", "*.png", "*.jpg", "*.jpeg"));

        File selectedFile = fileChooser.showOpenDialog(PosterAddMovieImageView.getScene().getWindow());
        if (selectedFile != null) {
            try {
                // Dosyayı byte array'e çevir
                byte[] imageBytes = Files.readAllBytes(selectedFile.toPath());

                // Debug için boyut kontrolü
                System.out.println("Image size: " + imageBytes.length + " bytes");

                // ImageView'da göstermek için Image nesnesine çevir
                Image image = new Image(new ByteArrayInputStream(imageBytes));
                PosterAddMovieImageView.setImage(image);

            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Image could not be uploaded: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void clearInputFields() {
        TitleAddMovie.clear();
        GenreAddMovie.clear();
        SummaryAddMovie.clear();
        PosterAddMovieImageView.setImage(null);
        YearAddMovie.clear();
    }

    private List<Movie> showMovies() {
        mov.connectDatabase();
        ArrayList<Movie> movies = mov.getMovies();
        mov.disconnectDatabase();
        return movies;
    }

    private void addMovieToDatabase(Movie movie) {
        mov.connectDatabase();
        mov.insertMovie(movie);
        mov.disconnectDatabase();
    }

    private void deleteMovieFromDatabase(Movie movie) {
        mov.connectDatabase();
        mov.deleteMovie(movie);
        mov.disconnectDatabase();
    }

    private void updateMovieInDatabase(Movie movie) {
        mov.connectDatabase();
        mov.updateMovie(movie);
        mov.disconnectDatabase();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void titleEnterPressed(ActionEvent event) {
        // This method is triggered when the Enter key is pressed in the Title TextField
        GenreAddMovie.requestFocus();
    }

    @FXML
    void genreEnterPressed(ActionEvent event) {
        // This method is triggered when the Enter key is pressed in the Genre TextField
        SummaryAddMovie.requestFocus();
    }

    @FXML
    void summaryEnterPressed(ActionEvent event) {
        // This method is triggered when the Enter key is pressed in the Summary TextField
        YearAddMovie.requestFocus();
    }

    @FXML
    void yearEnterPressed(ActionEvent event) {
        // This method is triggered when the Enter key is pressed in the Year TextField
        TitleAddMovie.requestFocus();
    }

    @FXML
    void handleBackButton(ActionEvent event) {
        try {
            // Load the Admin.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/Admin.fxml"));
            Parent root = loader.load();

            // Get the current window (Stage) and set the new scene
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while loading the Admin page.");
        }
    }

    @FXML
    void handleLogoutButton(ActionEvent event) {
        try {
            // Load the Login.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
            Parent root = loader.load();

            // Get the current window (Stage) and set the new scene
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while loading the Login page.");
        }
    }

}