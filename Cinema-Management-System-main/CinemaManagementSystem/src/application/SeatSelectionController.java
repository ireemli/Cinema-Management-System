package application;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.geometry.Pos;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import database.DatabasePrice;
import database.DatabaseSeats;
import javafx.scene.control.cell.PropertyValueFactory;

public class SeatSelectionController {

    private MainCashierController mainController;
    private Movie movieData;
    private Session sessionData;
    private List<Seat> allSeats;
    private List<Seat> selectedSeats;

    // Salon kapasiteleri
    private static final int HALL_A_ROWS = 4;
    private static final int HALL_A_COLS = 4;
    private static final int HALL_B_ROWS = 8;
    private static final int HALL_B_COLS = 6;

    @FXML
    private Button backButton;

    @FXML
    private Label dateTimeLabel;

    @FXML
    private Label hallLabel;

    @FXML
    private Label movieTitleLabel;

    @FXML
    private Button nextButton;

    @FXML
    private TableColumn<Price, Double> priceColumn;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private TableColumn<Seat, String> seatNoColumn;

    @FXML
    private GridPane seatsGrid;

    @FXML
    private TableView<Seat> selectedSeatsTable;

    @FXML
    private Label totalPriceLabel;

    @FXML
    private void initialize() {
        selectedSeats = new ArrayList<>();

        // Tablo sütunlarını ayarla
        seatNoColumn.setCellValueFactory(new PropertyValueFactory<>("seat"));
        // priceColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.format("%.2f", cellData.getValue().getPrice())));


        // Buton event handler'ları
        backButton.setOnAction(event -> handleBack());
        nextButton.setOnAction(event -> handleNext());

        // Next butonu başlangıçta devre dışı
        nextButton.setDisable(true);

        // Toplam fiyat başlangıçta 0
        updateTotalPrice();
    }

    public void setMainController(MainCashierController controller) {
        this.mainController = controller;
    }

    public void setSessionData(Movie movie, Session session) {
        this.movieData = movie;
        this.sessionData = session;
        updateSessionInfo();
        createSeatButtons();
    }

    private void updateSessionInfo() {
        movieTitleLabel.setText(movieData.getTitle());
        dateTimeLabel.setText(sessionData.getDateTime().toString());
        hallLabel.setText(sessionData.getHall().getName());
    }

    private void createSeatButtons() {
        seatsGrid.getChildren().clear();
        int rows = sessionData.getHall().getName().equals("Hall_A") ? HALL_A_ROWS : HALL_B_ROWS;
        int cols = sessionData.getHall().getName().equals("Hall_A") ? HALL_A_COLS : HALL_B_COLS;
        getAllSeats();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                String seatNo = String.format("%c%d", (char)('A' + row), col + 1);
                Button seatButton = createSeatButton(seatNo);
                seatsGrid.add(seatButton, col, row);
            }
        }
    }

    private Button createSeatButton(String seatNo) {
        Button button = new Button(seatNo);
        button.setPrefSize(50, 50);
        button.setAlignment(Pos.CENTER);

        // Koltuğun durumunu kontrol et
        boolean isOccupied = checkIfSeatOccupied(seatNo);
        if (isOccupied) {
            button.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white;");
            button.setDisable(true);
        } else {
            button.setStyle("-fx-background-color: #28a745; -fx-text-fill: white;");
            button.setOnAction(e -> handleSeatSelection(button, seatNo));
        }

        return button;
    }

    private void getAllSeats(){
        DatabaseSeats dataS = new DatabaseSeats();
        dataS.connectDatabase();
        allSeats=dataS.getSeats(sessionData.getId());
    }

    private void handleSeatSelection(Button seatButton, String seatNo) {
        Seat seat = new Seat();
        for(Seat s : allSeats){
            if(s.getSeat().equals(seatNo))
                seat=s;
        }

        if (seatButton.getStyle().contains("#28a745")) { // Yeşil - seçilmemiş
            seatButton.setStyle("-fx-background-color: #007bff; -fx-text-fill: white;");
            selectedSeats.add(seat);
        } else { // Mavi - seçilmiş
            seatButton.setStyle("-fx-background-color: #28a745; -fx-text-fill: white;");
            selectedSeats.removeIf(s -> s.getSeat().equals(seatNo));
        }

        selectedSeatsTable.getItems().setAll(selectedSeats);
        updateTotalPrice();
        nextButton.setDisable(selectedSeats.isEmpty());
    }

    private boolean checkIfSeatOccupied(String seatNo) {

        int sessionId = sessionData.getId();
        DatabaseSeats dataS = new DatabaseSeats();
        dataS.connectDatabase();
        Boolean taken = dataS.checkSeat(sessionId, seatNo);

        return taken;
    }

    private void updateTotalPrice() {
        DatabasePrice dataP = new DatabasePrice();
        dataP.connectDatabase();
        double price = dataP.getPrices().getPrice();
        double seatNumber = selectedSeats.size();
        double total = price*seatNumber;
        totalPriceLabel.setText(String.format("%.2f TL", total));
    }

    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/SessionSelection.fxml"));
            Parent sessionView = loader.load();

            SessionSelectionController sessionController = loader.getController();
            sessionController.setMainController(mainController);
            sessionController.setMovieData(movieData);

            mainController.getContentArea().getChildren().setAll(sessionView);
        } catch (IOException e) {
            showError("Navigation Error", "Could not return to session selection: " + e.getMessage());
        }
    }

    @FXML
    private void handleNext() {
        if (selectedSeats.isEmpty()) {
            showError("Selection Error", "Please select at least one seat");
            return;
        }

        try {
            System.out.println("asss");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/CustomerInfo.fxml"));
            System.out.println("0");

            Parent customerView = loader.load();
            System.out.println("a");
            CustomerInfoController customerController = loader.getController();
            System.out.println("aa");

            customerController.setMainController(mainController);
            System.out.println("aaaa");

            customerController.setBookingData(movieData, sessionData, selectedSeats);
            System.out.println("asss");


            mainController.getContentArea().getChildren().setAll(customerView);
            System.out.println("avvv");

        } catch (IOException e) {
            e.printStackTrace();
            showError("Loading Error", "Could not load customer information screen: " + e.getMessage());
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
