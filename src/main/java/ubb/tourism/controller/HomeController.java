package ubb.tourism.controller;

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
import ubb.tourism.business.service.impl.FlightServiceImpl;
import ubb.tourism.business.service.impl.TicketServiceImpl;
import ubb.tourism.business.service.impl.UserServiceImpl;
import ubb.tourism.controller.dto.FlightDTO;
import ubb.tourism.data.access.entity.Flight;
import ubb.tourism.data.access.entity.Ticket;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;
import java.util.stream.StreamSupport;

public class HomeController implements Initializable, Observer {

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
    public TableView<FlightDTO> searchTableView;
    @FXML
    public TableColumn<FlightDTO, String> searchAirportColumn;
    @FXML
    public TableColumn<FlightDTO, LocalTime> searchTimeColumn;
    @FXML
    public TableColumn<FlightDTO, Integer> searchSpotsColumn;
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
    private ObservableList<FlightDTO> searchTableViewModelGrade;

    private FlightServiceImpl flightService;
    private TicketServiceImpl ticketService;
    private UserServiceImpl userService;

    public HomeController(FlightServiceImpl flightService, TicketServiceImpl ticketService, UserServiceImpl userService) {
        this.flightService = flightService;
        this.ticketService = ticketService;
        this.userService = userService;
        tableViewModelGrade = FXCollections.observableArrayList();
        searchTableViewModelGrade = FXCollections.observableArrayList();
        this.flightService.addObserver(this);
        this.ticketService.addObserver(this);
        this.userService.addObserver(this);
    }

    @Override
    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        update();
    }

    @Override
    public void update() {
        loadFlightTable();
        clearTextFields();
        handleFlightTableSelection();
        handleSearchFlightTextField();
        handleQuantityTextField();
    }

    public void confirmTicketsButtonOnMouseClicked(MouseEvent mouseEvent) {
        String clientName = clientNameTextField.getText();
        String clientAddress = clientAddressTextField.getText();
        String tourists = touristsTextField.getText();
        Integer quantity = Integer.parseInt(quantityTextField.getText());
        Integer flightId = flightTableView.getSelectionModel().getSelectedItem().getId();
        Flight flight = flightTableView.getSelectionModel().getSelectedItem();
        flight.setAvailableSpots(flight.getAvailableSpots() - quantity);

        ticketService.save(new Ticket(0, flightId, quantity, clientName, clientAddress, tourists));
        flightService.update(flightId, flight);
    }

    public void selectDateEvent(ActionEvent actionEvent) {
        loadSearchFlightTable();
    }

    private void loadFlightTable() {

        tableViewModelGrade.clear();
        flightTableView.getSelectionModel().clearSelection();

        StreamSupport.stream(flightService.findAll().spliterator(), false)
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

        StreamSupport.stream(flightService.findAll().spliterator(), false)
                .filter(p -> {
                    LocalDate localDate = p.getFlightDateTime().toLocalDate();
                    return localDate.equals(searchDatePicker.getValue()) && p.getDestination().equals(searchTextField.getText());
                })
                .map(f -> new FlightDTO(f.getAirport(), f.getFlightDateTime().toLocalTime(), f.getAvailableSpots()))
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
