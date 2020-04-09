package tourism.app.client.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import tourism.app.services.TourismAppService;
import tourism.app.client.controller.model.FlightSummary;
import tourism.app.persistence.data.access.entity.Flight;
import tourism.app.persistence.data.access.entity.Ticket;
import tourism.app.persistence.data.access.entity.User;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;
import java.util.stream.StreamSupport;

public class HomeController implements Initializable {

    @FXML
    public TableColumn<Flight, String> destinationColumn;
    @FXML
    public TableColumn<Flight, LocalDateTime> timeColumn;
    @FXML
    public TableColumn<Flight, String> airportColumn;
    @FXML
    public TableColumn<Flight, Integer> spotsColumn;
    @FXML
    public TableView<Flight> flightTableView;
    @FXML
    public TableView<FlightSummary> searchTableView;
    @FXML
    public TableColumn<FlightSummary, String> searchAirportColumn;
    @FXML
    public TableColumn<FlightSummary, LocalTime> searchTimeColumn;
    @FXML
    public TableColumn<FlightSummary, Integer> searchSpotsColumn;
    @FXML
    public TextField searchTextField;
    @FXML
    public DatePicker searchDatePicker;
    @FXML
    public TextField clientNameTextField;
    @FXML
    public TextField clientAddressTextField;
    @FXML
    public TextField quantityTextField;
    @FXML
    public Button confirmTicketsButton;
    @FXML
    public Label invalidQuantityLabel;
    @FXML
    public TextField destinationTextField;
    @FXML
    public TextField touristsTextField;
    @FXML
    public Button logoutButton;

    private ObservableList<Flight> tableViewModelGrade;
    private ObservableList<FlightSummary> searchTableViewModelGrade;

    private TourismAppService tourismAppService;

    private User authenticatedUser;

    public HomeController(TourismAppService tourismAppService, User authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
        this.tourismAppService = tourismAppService;
        tableViewModelGrade = FXCollections.observableArrayList();
        searchTableViewModelGrade = FXCollections.observableArrayList();
    }

    @Override
    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        update();
    }

    public void update() {
        loadFlightTable();
        clearTextFields();
        handleFlightTableSelection();
        handleSearchFlightTextField();
        handleQuantityTextField();
    }

    @FXML
    public void confirmTicketsButtonOnMouseClicked(MouseEvent mouseEvent) {
        String clientName = clientNameTextField.getText();
        String clientAddress = clientAddressTextField.getText();
        String tourists = touristsTextField.getText();
        Integer quantity = Integer.parseInt(quantityTextField.getText());
        Integer flightId = flightTableView.getSelectionModel().getSelectedItem().getId();
        Flight flight = flightTableView.getSelectionModel().getSelectedItem();
        flight.setAvailableSpots(flight.getAvailableSpots() - quantity);

        tourismAppService.save(new Ticket(0, flightId, quantity, clientName, clientAddress, tourists));
        tourismAppService.update(flightId, flight);
    }

    public void selectDateEvent(ActionEvent actionEvent) {
        loadSearchFlightTable();
    }

    private void loadFlightTable() {

        tableViewModelGrade.clear();
        flightTableView.getSelectionModel().clearSelection();

        StreamSupport.stream(tourismAppService.findAll().spliterator(), false)
                .filter(p -> p.getAvailableSpots() > 0)
                .forEach(tableViewModelGrade::add);

        destinationColumn.setCellValueFactory(new PropertyValueFactory<>("destination"));
        airportColumn.setCellValueFactory(new PropertyValueFactory<>("airport"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("flightDateTime"));
        spotsColumn.setCellValueFactory(new PropertyValueFactory<>("availableSpots"));

        flightTableView.setItems(tableViewModelGrade);
        flightTableView.getSortOrder().add(timeColumn);
        flightTableView.getSelectionModel().clearSelection();
    }

    private void handleFlightTableSelection() {
        flightTableView.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue == null) {
                return;
            }
            clientNameTextField.clear();
            clientAddressTextField.clear();
            touristsTextField.clear();
            quantityTextField.clear();
            invalidQuantityLabel.setVisible(false);
            confirmTicketsButton.setDisable(true);
            destinationTextField.setText(newValue.getDestination());
        }));
    }

    private void handleSearchFlightTextField() {
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            loadSearchFlightTable();
        });
    }

    private void loadSearchFlightTable() {

        searchTableViewModelGrade.clear();

        if (searchDatePicker.getValue() == null || searchTextField.getText().equals("")) {
            return;
        }

        StreamSupport.stream(tourismAppService.findAll().spliterator(), false)
                .filter(p -> {
                    LocalDate localDate = p.getFlightDateTime().toLocalDate();
                    return localDate.equals(searchDatePicker.getValue()) && p.getDestination().equals(searchTextField.getText());
                })
                .map(f -> new FlightSummary(f.getAirport(), f.getFlightDateTime().toLocalTime(), f.getAvailableSpots()))
                .forEach(searchTableViewModelGrade::add);

        searchAirportColumn.setCellValueFactory(new PropertyValueFactory<>("airport"));
        searchTimeColumn.setCellValueFactory(new PropertyValueFactory<>("schedule"));
        searchSpotsColumn.setCellValueFactory(new PropertyValueFactory<>("availableSpots"));
        searchTableView.getSortOrder().add(searchTimeColumn);
        searchTableView.setItems(searchTableViewModelGrade);
    }

    private void handleQuantityTextField() {
        quantityTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (destinationTextField.getText().equals("")) {
                return;
            }
            invalidQuantityLabel.setVisible(false);
            confirmTicketsButton.setDisable(true);
            try {
                int quantity = Integer.parseInt(newValue);
                if (quantity > flightTableView.getSelectionModel().getSelectedItem().getAvailableSpots()) {
                    invalidQuantityLabel.setVisible(true);
                } else {
                    confirmTicketsButton.setDisable(false);
                }
            } catch (NumberFormatException e) {
                invalidQuantityLabel.setVisible(true);
            }
        });
    }

    private void clearTextFields() {
        destinationTextField.setText("");
        touristsTextField.setText("");
        clientNameTextField.setText("");
        clientAddressTextField.setText("");
        quantityTextField.setText("");
        searchTextField.setText("");
        confirmTicketsButton.setDisable(true);
    }

    public void logoutButtonOnMouseClicked(MouseEvent mouseEvent) {
        Node node = (Node) mouseEvent.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }
}
