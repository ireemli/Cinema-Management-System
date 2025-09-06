package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import database.DatabaseBill;
import database.DatabasePrice;
import database.DatabaseProduct;
import database.DatabaseSeats;
import database.DatabaseSession;
import javafx.util.StringConverter;


public class CustomerInfoController {

    private MainCashierController mainController;
    private Movie movieData;
    private Session sessionData;
    private List<Seat> selectedSeats;
    private double totalTicketPrice;
    private double total_amount;
    private Price prices;
    private int flag = 0;

    @FXML
    private Button backButton;

    @FXML
    private DatePicker birthDateField;

    @FXML
    private Button confirmPayButton;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField idNumberField;

    @FXML
    private TableColumn<OrderItem, Double> itemDiscountColumn;

    @FXML
    private TableColumn<OrderItem, String> itemNameColumn;

    @FXML
    private TableColumn<OrderItem, Double> itemPriceColumn;

    @FXML
    private TableColumn<OrderItem, Integer> itemQuantityColumn;

    @FXML
    private TableColumn<OrderItem, Double> itemTotalColumn;

    @FXML
    private TextField lastNameField;

    @FXML
    private Label movieInfoLabel;

    @FXML
    private TableColumn<Product, String> productActionColumn;

    @FXML
    private TableColumn<Product, String> productNameColumn;

    @FXML
    private TableColumn<Product, Double> productPriceColumn;

    @FXML
    private TableColumn<Product, Integer> productQuantityColumn;

    @FXML
    private TableView<Product> productsTable;

    @FXML
    private TableView<OrderItem> summaryTable;

    @FXML
    private Label selectedSeatsLabel;

    @FXML
    private Label totalPriceLabel;

    @FXML
    private Label vatLabel;

    @FXML
    private void initialize() {
        // Tablo sütunlarını ayarla
        setupTables();

        //İlk yüklendiğinde Label'ların boş gelmesi için
        movieInfoLabel.setText("");
        selectedSeatsLabel.setText("");

        // Event listeners
        backButton.setOnAction(event -> handleBack());
        confirmPayButton.setOnAction(event -> handleConfirmPay());

        // value change listener eklendi
        birthDateField.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if(validateAge() && flag==0){
                    applyAgeDiscount();
                    flag=1;
                }
                else if(!validateAge() && flag==1){
                    returnAgeDiscount();
                    flag=0;
                }
            }
        });

        // DatePicker format ve kısıtlama ayarları
        birthDateField.setPromptText("DD/MM/YYYY");
        birthDateField.setEditable(false); // Manuel yazı girişini engelle

        // Sadece geçerli tarih girişine izin ver
        birthDateField.getEditor().setOnKeyTyped(event -> event.consume());
        birthDateField.getEditor().setOnKeyPressed(event -> event.consume());

        // Tarih formatını ayarla
        StringConverter<LocalDate> converter = new StringConverter<>() {
            private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            @Override
            public String toString(LocalDate date) {
                return date != null ? dateFormatter.format(date) : "";
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    try {
                        return LocalDate.parse(string, dateFormatter);
                    } catch (Exception e) {
                        return null;
                    }
                }
                return null;
            }
        };
        birthDateField.setConverter(converter);
        LocalDate today = LocalDate.now();
        LocalDate yearbefore = today.minusYears(6);
        birthDateField.setValue(yearbefore);

        // Maksimum tarihi bugün olarak ayarla (gelecek tarihleri engelle)
        birthDateField.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.compareTo(yearbefore) > 0);
            }
        });


        // Ürünleri yükle
        DatabasePrice dataPr = new DatabasePrice();
        dataPr.connectDatabase();
        prices = dataPr.getPrices();
        dataPr.disconnectDatabase();
        loadProducts();
    }

    private void loadProducts() {
        // Veritabanından ürünleri yükle
        DatabaseProduct dataP = new DatabaseProduct();
        dataP.connectDatabase();
        List<Product> products = dataP.viewInventory();
        dataP.disconnectDatabase();
        productsTable.getItems().addAll(products);
    }

    private void applyAgeDiscount() {
        // %50 indirim uygula
        double discountRate = 1-(prices.getDiscountPercentage()/100);
        totalTicketPrice = totalTicketPrice * discountRate;
        updateTotalPrice();
    }

    private void returnAgeDiscount(){
        double discountRate = 1-(prices.getDiscountPercentage()/100);
        totalTicketPrice = totalTicketPrice / discountRate;
        updateTotalPrice();
    }

    private double calculateProductsTotal() {
        return summaryTable.getItems().stream()
                .mapToDouble(product -> product.getPrice() * product.getQuantity())
                .sum();
    }

    private void addToSummary(Product product) {
        if(product.getStock()>0){
            int flag = 0;
            for(OrderItem item : summaryTable.getItems()){
                if(item.getName().equals(product.getName())){
                    item.setQuantity(item.getQuantity()+1);
                    flag=1;
                    break;
                }
            }
            if(flag==0){
                OrderItem itemNew = new OrderItem(
                    0, // id
                    product.getName(),
                    1, // quantity
                    product.getPrice(),
                    0, // no discount for products
                    "product"
                    );
                summaryTable.getItems().add(itemNew);
            }
            product.setStock(product.getStock()-1);
            summaryTable.refresh();
            productsTable.refresh();
        }
    }

    private void removeFromSummary(Product product) {
        OrderItem itemToRemove = new OrderItem();
        for(OrderItem item : summaryTable.getItems()){
            if(item.getName().equals(product.getName())){
                product.setStock(product.getStock()+1);
                item.setQuantity(item.getQuantity()-1);
                if(item.getQuantity()==0)
                    itemToRemove=item;
                break;
            }
        }
        summaryTable.getItems().remove(itemToRemove);
        summaryTable.refresh();
        productsTable.refresh();
    }

    private void loadMovieSearch() {
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

    private void setupTables() {
        // Ürünler tablosu
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        productPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        productQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        setupActionColumn();

        // Özet tablosu
        itemNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        itemQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        itemPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        itemDiscountColumn.setCellValueFactory(new PropertyValueFactory<>("discount"));
        itemTotalColumn.setCellValueFactory(new PropertyValueFactory<>("total"));
    }

    private void setupActionColumn() {
        productActionColumn.setCellFactory(column -> new TableCell<>() {
            final Button addButton = new Button("+");
            final Button removeButton = new Button("-");
            final HBox buttonBox = new HBox(5, addButton, removeButton);

            {
                addButton.setOnAction(event -> handleAddProduct(getTableRow().getItem()));
                removeButton.setOnAction(event -> handleRemoveProduct(getTableRow().getItem()));
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(buttonBox);
                }
            }
        });
    }

    public void setMainController(MainCashierController controller) {
        this.mainController = controller;
    }

    public void setBookingData(Movie movie, Session session, List<Seat> seats) {
        this.movieData = movie;
        this.sessionData = session;
        this.selectedSeats = seats;
        updateBookingInfo();
    }

    private void updateBookingInfo() {
        movieInfoLabel.setText(String.format("Movie: %s - Hall: %s - Date: %s",
                movieData.getTitle(), sessionData.getHall().getName(), sessionData.getDateTime()));

        String seatNumbers = selectedSeats.stream()
                .map(Seat::getSeat)
                .collect(Collectors.joining(", "));
        selectedSeatsLabel.setText("Selected Seats: " + seatNumbers);

        calculateInitialTotal();
    }

    private void calculateInitialTotal() {

        totalTicketPrice = selectedSeats.size()*prices.getPrice();
        updateTotalPrice();
    }

    private boolean validateAge() {
        try {
            LocalDate birthDate = birthDateField.getValue();
            if (birthDate == null) {
                return false;
            }
            Period period = Period.between(birthDate, LocalDate.now());

            if (period.getYears() < 18 || period.getYears() > 60) {
                if(totalTicketPrice!=0)
                    return true;
            }            
        } catch (Exception e) {
            showError("Invalid Date", "Please select a valid date");
        }
        return false;
    }

    private void handleAddProduct(Product product) {
        addToSummary(product);
        updateTotalPrice();
    }

    private void handleRemoveProduct(Product product) {
        // Remove product from summary
        removeFromSummary(product);
        updateTotalPrice();
        // Update prices
    }

    private void updateTotalPrice() {
        double totalProducts = calculateProductsTotal();
        total_amount = totalTicketPrice + totalProducts;
        double vat = total_amount * prices.getTaxPercentage()/100; // Taxes

        vatLabel.setText(String.format("%.2f TL", vat));
        totalPriceLabel.setText(String.format("%.2f TL", total_amount));
    }

    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/SeatSelection.fxml"));
            Parent seatView = loader.load();

            SeatSelectionController seatController = loader.getController();
            seatController.setMainController(mainController);
            seatController.setSessionData(movieData, sessionData);

            mainController.getContentArea().getChildren().setAll(seatView);
        } catch (IOException e) {
            showError("Navigation Error", "Could not return to seat selection: " + e.getMessage());
        }
    }

    @FXML
    private void handleConfirmPay() {
        if (!validateFields()) {
            return;
        }
        if (customerNameError()){
            return;
        }

        try {
            updateTotalPrice();
            DatabaseProduct dataP = new DatabaseProduct();
            dataP.connectDatabase();
            deleteStocksFromDatabase(dataP);
            dataP.disconnectDatabase();
            saveBill(createCustomer());
            fillSeats();

            // Save customer info!!
            // Process payment
            // Generate tickets and invoice
            // Return to movie search

            loadMovieSearch();
        } catch (Exception e) {
            showError("Payment Error", "Could not process payment: " + e.getMessage());
        }
    }

    private void fillSeats(){
        DatabaseSeats dataS = new DatabaseSeats();
        dataS.connectDatabase();
        dataS.fillSeats(selectedSeats);
        dataS.disconnectDatabase();
        DatabaseSession dataSs = new DatabaseSession();
        dataSs.connectDatabase();
        dataSs.fillSeats(sessionData.getId(),selectedSeats.size());
        dataSs.disconnectDatabase();
    }

    private Customer createCustomer() {
        Customer customer = new Customer(
                0,
                firstNameField.getText(),
                lastNameField.getText(),
                birthDateField.getValue() // DatePicker değerini direkt kullanabilirz
        );
        return customer;
    }

    private void deleteStocksFromDatabase(DatabaseProduct dataP){

        for(OrderItem item : summaryTable.getItems()){
            Product product = new Product();
            product.setName(item.getName());
            product.setPrice(item.getPrice());
            product.setStock(item.getQuantity()*-1);
            dataP.updateStocks(product);
        }

    }

    private ArrayList<ItemBills> createItems(){

        ArrayList<ItemBills> itemBills = new ArrayList<>();

        String seatNames="";
        for(Seat seat : selectedSeats){
            seatNames = seatNames + "-" + seat.getSeat();
        }
        ItemBills itembill_tickets = new ItemBills(0,0,"ticket",seatNames,
                                    selectedSeats.size(),prices.getPrice()*selectedSeats.size(),(prices.getPrice()*prices.getTaxPercentage()/100));
        itemBills.add(itembill_tickets);

        for(OrderItem item : summaryTable.getItems()){
            ItemBills itembill = new ItemBills(0,0,"product",item.getName(),
                                item.getQuantity(),item.getPrice()*item.getQuantity(),(item.getPrice()*prices.getTaxPercentage()/100));
            itemBills.add(itembill);
        }
        return itemBills;

    }

    private void saveBill(Customer customer){
        String customerN = customer.getFirstName() + " " + customer.getLastName();
        Bills bill = new Bills(0,null,customerN,customer.getBirthDate(),sessionData.getId(),total_amount,(total_amount*prices.getTaxPercentage()/100));
        DatabaseBill dataB = new DatabaseBill();
        dataB.connectDatabase();
        dataB.setBill(createItems(), bill);
        dataB.disconnectDatabase();
    }

    private boolean validateFields() {
        if (firstNameField.getText().trim().isEmpty() ||
                lastNameField.getText().trim().isEmpty() ||
                idNumberField.getText().trim().isEmpty() ||
                birthDateField.getValue() == null) {  // Değiştirildi
            showError("Validation Error", "Please fill all customer information fields");
            return false;
        }
        return true;
    }

    private boolean customerNameError(){
        if (firstNameField.getText().matches("[a-zA-ZÇçĞğİıÖöŞşÜü]+")){
            if(lastNameField.getText().matches("[a-zA-ZÇçĞğİıÖöŞşÜü]+"))
                return false;
            else{
                showError("Validation Error", "Please fill customer last name with only alphabetical characters");
                return true;
            }
        } else{
            showError("Validation Error", "Please fill customer first name with only alphabetical characters");
            return true;
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
