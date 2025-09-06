package application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import database.DatabaseEmployee;
import database.DatabaseMovies;

import java.io.IOException;

public class MovieSearchController {

    private MainCashierController mainController;
    private DatabaseMovies mov = new DatabaseMovies();


    //Seçilen film bilgilerini tutacak model sınıf
    private Movie selectedMovie;

    @FXML
    private Button cancelButton;

    @FXML
    private TableColumn<Movie, String> genreColumn;

    @FXML
    private TableColumn<Movie, Integer> yearColumn;

    @FXML
    private Label movieGenreLabel;

    @FXML
    private TableView<Movie> movieResultsTable;

    @FXML
    private VBox movieSearchRoot;

    @FXML
    private TextArea movieSummaryArea;

    @FXML
    private Label movieTitleLabel;

    @FXML
    private Button nextButton;

    @FXML
    private ImageView posterImageView;

    @FXML
    private Button searchButton;

    @FXML
    private TextField searchTextField;

    @FXML
    private ComboBox<String> searchTypeCombo; //Generic tipini String olarak değiştirdim

    private static final String[] SEARCH_TYPES = {
            "Search by Genre",
            "Search by Partial Name",
            "Search by Full Name"
    };

    @FXML
    private TableColumn<Movie, String> summaryColumn;

    @FXML
    private TableColumn<Movie, String> titleColumn;

    public void setMainController(MainCashierController controller) {
        this.mainController = controller;
    }

    @FXML
    private void initialize() {
        System.out.println("Initializing MovieSearchController...");

        //İlk yüklendiğinde Label'ların boş gelmesi için
        movieTitleLabel.setText("");
        movieGenreLabel.setText("");

        searchTypeCombo.getItems().addAll(SEARCH_TYPES);
        searchTypeCombo.getSelectionModel().selectFirst();

        //Başlangıçta filmleri listele
        movieResultsTable.getItems().setAll(showMovies());

        //Tablo sütunları ayarı:
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year")); // Yeni eklenen
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
        summaryColumn.setCellValueFactory(new PropertyValueFactory<>("summary")); // Bu eksik!

        //Tablo seçim event lisetener:
        movieResultsTable.getSelectionModel().selectedItemProperty().addListener((obs,oldVal,newWal) -> {
            if(newWal != null) {
                selectedMovie = newWal;
                updateMovieDetails();
                SessionSelectionController.movieData = selectedMovie;
            }
        });

        //next butonu:
        searchButton.setOnAction(event -> handleSearch());
        cancelButton.setOnAction(event -> handleCancel());
        nextButton.setOnAction(event -> loadSessionSelection());    
    }

    private void updateMovieDetails() {
        if (selectedMovie != null) {
            movieTitleLabel.setText(selectedMovie.getTitle());
            movieGenreLabel.setText(selectedMovie.getGenre());
            movieSummaryArea.setText(selectedMovie.getSummary());

            // Poster'ı güncelle
            byte[] posterBytes = selectedMovie.getPosterImage();
            if (posterBytes != null && posterBytes.length > 0) {
                try {
                    // Debug için boyut kontrolü
                    System.out.println("Loading image, size: " + posterBytes.length + " bytes");

                    Image image = new Image(new ByteArrayInputStream(posterBytes));
                    posterImageView.setImage(image);

                    // Debug için yükleme kontrolü
                    System.out.println("Image loaded successfully: " + !image.isError());
                } catch (Exception e) {
                    System.err.println("Error loading image: " + e.getMessage());
                    e.printStackTrace();
                    posterImageView.setImage(null);
                }
            } else {
                posterImageView.setImage(null);
            }
        }
    }

    @FXML
    private void handleSearch() {
        String searchType = searchTypeCombo.getValue();
        String searchTerm = searchTextField.getText().trim();

        if (searchTerm.isEmpty()) {
            movieResultsTable.getItems().setAll(showMovies());
            return;
        }

        List<Movie> results;
        try {
            switch(searchType) {
                case "Search by Genre":
                    results = searchByGenre(searchTerm);
                    break;
                case "Search by Partial Name":
                    results = searchByPartialName(searchTerm);
                    break;
                case "Search by Full Name":
                    results = searchByFullName(searchTerm);
                    break;
                default:
                    results = new ArrayList<>();
            }
            movieResultsTable.getItems().setAll(results);
        } catch (Exception e) {
            showError("Search Error", "Error while searching: " + e.getMessage());
        }
    }

    private List<Movie> showMovies() {
        // Veritabanından genre'a göre filmleri çek
        mov.connectDatabase();        
        ArrayList<Movie> movies = mov.getMovies();
        mov.disconnectDatabase();
        return movies; // TODO: implement
    }


    // Search metodları (veritabanı bağlantısı gerekecek)
    private List<Movie> searchByGenre(String genre) {
        // Veritabanından genre'a göre filmleri çek
        mov.connectDatabase();        
        ArrayList<Movie> movies = mov.getMovieGenre(genre);
        mov.disconnectDatabase();
        return movies; // TODO: implement
    }

    private List<Movie> searchByPartialName(String partialName) {
        // Veritabanından partial name'e göre filmleri çek
        mov.connectDatabase();        
        ArrayList<Movie> movies = mov.getMoviePartial(partialName);
        mov.disconnectDatabase();
        return movies; // TODO: implement
    }

    private List<Movie> searchByFullName(String fullName) {
        // Veritabanından tam isme göre filmleri çek
        mov.connectDatabase();        
        ArrayList<Movie> movies = mov.getMovieTitle(fullName);
        mov.disconnectDatabase();
        return movies; // TODO: implement
    }

    @FXML
    private void handleCancel() {
        // Seçimleri temizle
        searchTextField.clear();
        movieResultsTable.getSelectionModel().clearSelection();
        movieTitleLabel.setText("");
        movieGenreLabel.setText("");
        movieSummaryArea.clear();
        posterImageView.setImage(null);
        selectedMovie = null;
    }

    private void loadSessionSelection() {
        if (selectedMovie == null) {
            showError("Selection Error", "Please select a movie first");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/SessionSelection.fxml"));

            Parent sessionView = loader.load();

            SessionSelectionController sessionController = loader.getController();
            if (sessionController == null) {
                throw new RuntimeException("Could not load session controller");
            }

            sessionController.setMovieData(selectedMovie);
            sessionController.setMainController(mainController);

            mainController.getContentArea().getChildren().setAll(sessionView);

        } catch (IOException e) {
            showError("Loading Error", "Could not load session selection screen: " + e.getMessage());
        }
    }

    private void showError(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

}