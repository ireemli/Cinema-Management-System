package application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.fxml.Initializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import database.DatabaseHalls;
import database.DatabaseSession;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class SessionSelectionController {

    private MainCashierController mainController;
    public static Movie movieData;
    private Session selectedSession;

    private static ArrayList<String> HALLS = new ArrayList<>();
    private static ArrayList<String> SESSIONS = new ArrayList<>();

    @FXML
    private Label availableSeatsLabel;

    @FXML
    private Button backButton;

    @FXML
    private DatePicker datePicker;

    @FXML
    private ComboBox<String> hallCombo;

    @FXML
    private ImageView moviePosterView;

    @FXML
    private Button nextButton;

    @FXML
    private Label selectedGenreLabel;

    @FXML
    private Label selectedMovieLabel;

    @FXML
    private GridPane selectionGrid;

    @FXML
    private ComboBox<String> sessionCombo;

    @FXML
    private VBox sessionSelectionRoot;

    @FXML
    private void initialize() {

        DatabaseSession dataS = new DatabaseSession();
        dataS.connectDatabase();
        SESSIONS=dataS.getSessionHours(movieData.getId());
        HALLS = dataS.getHallNames(movieData.getId());

        //Combobox'ları doldurmak için
        hallCombo.getItems().addAll(HALLS);
        sessionCombo.getItems().addAll(SESSIONS);

        List<LocalDate> allowedDates = new ArrayList<>();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // sadece filmin olduğu seans günlerini veri olarak alır
            List<String> dates = dataS.getSessionDates(movieData.getId());

            for (String date : dates) {   // Changes strings to local dates
                LocalDate localDate = LocalDate.parse(date, formatter);
                allowedDates.add(localDate);

            }

        //DatePicker'ı bugünden başlatıp 1 ay ile sınırlayalım
        datePicker.setValue(allowedDates.getFirst());
        datePicker.setDayCellFactory(datePicker1 -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();
                LocalDate oneMonthLater = today.plusMonths(1);
                setDisable(empty || date.compareTo(today) < 0 || date.compareTo(oneMonthLater) > 0 || !allowedDates.contains(date));
            }
        });

        //Event Listeners:
        datePicker.valueProperty().addListener((obs, oldVal, newVal) -> updateAvailableSeats(dataS));
        hallCombo.valueProperty().addListener((obs, oldVal, newVal) -> updateAvailableSeats(dataS));
        sessionCombo.valueProperty().addListener((obs, oldVal, newVal) -> updateAvailableSeats(dataS));

        backButton.setOnAction(event -> handleBack());
        nextButton.setOnAction(event -> handleNext());

        // Next butonu başlangıçta devre dışı
        nextButton.setDisable(true);
    }

    public void setMainController(MainCashierController controller) {
        this.mainController = controller;
    }

    public void setMovieData(Movie movie) {
        this.movieData = movie;
        updateMovieInfo();
    }

    private void updateMovieInfo() {
        if (movieData != null) {
            selectedMovieLabel.setText(movieData.getTitle());
            selectedGenreLabel.setText(movieData.getGenre());
            if (movieData.getPosterImage() != null) {
                Image image = new Image(new ByteArrayInputStream(movieData.getPosterImage()));
                moviePosterView.setImage(image);
            }
        }
    }

    private void updateAvailableSeats(DatabaseSession dataS){
        if (datePicker.getValue() != null && hallCombo.getValue() != null && sessionCombo.getValue() != null) {
            try {

                ArrayList<Session> sessions = dataS.getSessions();

                for(Session session : sessions){
                    if(session.getHall().getName().equals(hallCombo.getValue())){

                        String dateTime = datePicker.getValue().toString() + "T" + sessionCombo.getValue().toString();
                        String time = session.getDateTime().toString().substring(0, 16);

                        if(time.equals(dateTime))
                            selectedSession = session;
                    }
                }
                
                // Veritabanından boş koltuk sayısını al
                int availableSeats = selectedSession.getAvailableSeats();
                availableSeatsLabel.setText(String.valueOf(availableSeats));

                nextButton.setDisable(availableSeats <= 0);

            } catch (Exception e) {
                showError("Database Error", "Could not fetch available seats: " + e.getMessage());
            }
        } else {
            availableSeatsLabel.setText("--");
            nextButton.setDisable(true);
        }
    }

    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/MovieSearch.fxml"));
            Parent movieSearchView = loader.load();

            MovieSearchController movieController = loader.getController();
            movieController.setMainController(mainController);

            mainController.getContentArea().getChildren().setAll(movieSearchView);
        } catch (IOException e) {
            showError("Navigation Error", "Could not return to movie search: " + e.getMessage());
        }
    }

    @FXML
    private void handleNext() {
        if (selectedSession == null) {
            showError("Selection Error", "Please select date, hall and session");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/SeatSelection.fxml"));
            Parent seatView = loader.load();

            SeatSelectionController seatController = loader.getController();
            if (seatController == null) {
                throw new RuntimeException("Could not load seat selection controller");
            }

            seatController.setMainController(mainController);
            seatController.setSessionData(movieData, selectedSession);

            mainController.getContentArea().getChildren().setAll(seatView);
        } catch (IOException e) {
            showError("Loading Error", "Could not load seat selection screen: " + e.getMessage());
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