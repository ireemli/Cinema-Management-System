package application;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import database.DatabaseHalls;
import database.DatabaseSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class MonthlySchedulesController {

    private final String[] HOURS = {"10:00", "12:00", "14:00", "16:00", "18:00", "20:00"};
    private ArrayList<String> HALLS = new ArrayList<>();
    private ArrayList<String> MOVIES = new ArrayList<>();


    @FXML
    private DatePicker ScheduleDateChoice;

    @FXML
    private ChoiceBox<String> ScheduleMovieChoice;

    @FXML
    private ChoiceBox<String> ScheduleHallChoice;

    @FXML
    private ChoiceBox<String> ScheduleSessionChoice;

    @FXML
    private Button backFromSchedules;

    @FXML
    private Button createSchedulesButton;

    @FXML
    private TableColumn<Session, LocalDate> dateScheduleTable;

    @FXML
    private TableColumn<Session, String> hallScheduleTable;

    @FXML
    private TableColumn<Session, String> movieScheduleTable;

    @FXML
    private TableView<Session> schedulesTable;

    @FXML
    private TableColumn<Session, String> sessionScheduleTable;

    @FXML
    private Button updateSchedulesButton;

    private Map<String, Movie> movieMap = new HashMap<>();
    private Map<String, Halls> hallMap = new HashMap<>();

    private ObservableList<Session> scheduleList = FXCollections.observableArrayList();
    private DatabaseSession dataS = new DatabaseSession();
    private static Session selectedSession;

    @FXML
    private Button logoutButton;

    @FXML
    private HBox topBar;

    @FXML
    private Label userInfoLabel;

    @FXML
    public void initialize() {

        dateScheduleTable.setCellValueFactory(cellData -> 
            new SimpleObjectProperty<>(cellData.getValue().getDateTime().toLocalDate()));
        hallScheduleTable.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getHall().getName()));
        movieScheduleTable.setCellValueFactory(cellData -> {
            Movie movie = cellData.getValue().getMovie();
            return new SimpleStringProperty(movie.getTitle() + " (" + movie.getYear() + ")");
        });
        sessionScheduleTable.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getDateTime().toLocalTime().toString()));

            ScheduleDateChoice.setDayCellFactory(datePicker1 -> new DateCell() {
                @Override
                public void updateItem(LocalDate date, boolean empty) {
                    super.updateItem(date, empty);
                    LocalDate today = LocalDate.now();
                    LocalDate oneMonthLater = today.plusMonths(1);
                    setDisable(empty || date.compareTo(today) < 0 || date.compareTo(oneMonthLater) > 0);
                }
            });
            
        scheduleList.setAll(getSchedules());
        schedulesTable.setItems(scheduleList);
        ScheduleDateChoice.setValue(LocalDate.now());

        getMovies(scheduleList);
        getHalls();
        ScheduleHallChoice.setItems(FXCollections.observableArrayList(HALLS));
        ScheduleSessionChoice.setItems(FXCollections.observableArrayList(HOURS));
        ScheduleMovieChoice.setItems(FXCollections.observableArrayList(MOVIES));

        schedulesTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) { // newVal, seçilen Session nesnesidir
                selectedSession = newVal;
            }
        });

    }

    private void getMovies(ObservableList<Session> schedulelist){
        for(Session schedule : schedulelist){
            String movieName = schedule.getMovie().getTitle() + " (" + schedule.getMovie().getYear() + ")";
            if(!MOVIES.contains(movieName))
                MOVIES.add(movieName);
                movieMap.put(movieName, schedule.getMovie());
        }

    }

    private void getHalls(){
        DatabaseHalls dataH = new DatabaseHalls();
        dataH.connectDatabase();
        ArrayList<Halls> hallsT = dataH.viewHalls();
        for(Halls hall : hallsT) {
            HALLS.add(hall.getName());
            if (hall != null)
                hallMap.put(hall.getName(), hall);
        }
        dataH.disconnectDatabase();
    }

    private ArrayList<Session> getSchedules(){
        dataS.connectDatabase();
        ArrayList<Session> sessions = dataS.getSessions();
        dataS.disconnectDatabase();
        return sessions;
    }

    @FXML
    void handleBackButton(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/Admin.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void handleCreateButton(ActionEvent event) {

        LocalDate date = ScheduleDateChoice.getValue();
        String hall = ScheduleHallChoice.getValue();
        Halls selectedHall = hallMap.get(hall);
        String session = ScheduleSessionChoice.getValue();
        String movie = ScheduleMovieChoice.getValue();
        Movie sMovie = movieMap.get(movie);

        if (date != null && hall != null && session != null && movie != null) {

            LocalDateTime sessionTime = LocalDateTime.of(date, LocalTime.parse(session));
            Session newSchedule = new Session(scheduleList.size() + 1, sMovie, sessionTime, selectedHall, selectedHall.getCapacity());

            scheduleList.add(newSchedule);
            addDatabase(newSchedule);
            schedulesTable.setItems(scheduleList);
            clearFields();

        } else {
            showAlert(Alert.AlertType.WARNING, "Warning", "All fields are required!");
        }
    }

    private void addDatabase(Session schedule){
        dataS.connectDatabase();
        dataS.insertSession(schedule);
        dataS.disconnectDatabase();
    }

    private void updateDatabase(Session schedule){
        dataS.connectDatabase();
        dataS.updateSession(schedule);
        dataS.disconnectDatabase();
    }

    @FXML
    void handleUpdateButton(ActionEvent event) {

        Session selectedSchedule = schedulesTable.getSelectionModel().getSelectedItem();

        if(selectedSchedule.getHall().getCapacity()==selectedSchedule.getAvailableSeats()){

            if (selectedSchedule != null) {

                String hall = ScheduleHallChoice.getValue();
                System.out.println(hall);
                Halls selectedHall = hallMap.get(hall);
                LocalDate date = ScheduleDateChoice.getValue();
                String session = ScheduleSessionChoice.getValue();
                String movie = ScheduleMovieChoice.getValue();
                Movie sMovie = movieMap.get(movie);

                if(session==null){
                    session=selectedSchedule.getDateTime().toLocalTime().toString();
                }
                if(date!=null){
                String temp = date.toString() + " " + session;
                DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                LocalDateTime DateTime = LocalDateTime.parse(temp, format);
                selectedSchedule.setDateTime(DateTime);
                }

                if (hall != null) selectedSchedule.setHall(selectedHall);

                if (hall != null) selectedSchedule.setAvailableSeats(selectedHall.getCapacity());

                if (sMovie != null) selectedSchedule.setMovie(sMovie);

                updateDatabase(selectedSchedule);
                schedulesTable.refresh();
                clearFields();

                }
        }
        else
            showAlert(Alert.AlertType.ERROR, "Error", "Tickets sold from this session");

    }

    @FXML
    void handleLogoutButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/Login.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while loading the Login page.");
        }
    }

    private void clearFields() {
        ScheduleDateChoice.setValue(null);
        ScheduleHallChoice.setValue(null);
        ScheduleSessionChoice.setValue(null);
        ScheduleMovieChoice.setValue(null);
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}