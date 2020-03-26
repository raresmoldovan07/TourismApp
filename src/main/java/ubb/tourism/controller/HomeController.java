package ubb.tourism.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import ubb.tourism.business.service.FlightService;
import ubb.tourism.business.service.TicketService;
import ubb.tourism.business.service.UserService;
import ubb.tourism.controller.model.FlightSummary;
import ubb.tourism.data.access.entity.Flight;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
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
    public TextField clientNameTextField2;
    @FXML
    public TextField quantityTextField;
    @FXML
    public Button confirmTicketsButton;
    @FXML
    public Label invalidQuantityLabel;

    private ObservableList<Flight> tableViewModelGrade;
    private ObservableList<FlightSummary> searchTableViewModelGrade;

    private FlightService flightService;
    private TicketService ticketService;
    private UserService userService;

    public HomeController(FlightService flightService, TicketService ticketService, UserService userService) {
        this.flightService = flightService;
        this.ticketService = ticketService;
        this.userService = userService;
        tableViewModelGrade = FXCollections.observableArrayList();
        searchTableViewModelGrade = FXCollections.observableArrayList();
    }

    @Override
    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        loadFlightTable();
        handleSearchFlightTextField();
    }

    private void loadFlightTable() {
        tableViewModelGrade.setAll((Collection<? extends Flight>) flightService.findAll());
        destinationColumn.setCellValueFactory(new PropertyValueFactory<>("destination"));
        airportColumn.setCellValueFactory(new PropertyValueFactory<>("airport"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("flightDateTime"));
        spotsColumn.setCellValueFactory(new PropertyValueFactory<>("availableSpots"));
        flightTableView.setItems(tableViewModelGrade);
        flightTableView.getSortOrder().add(timeColumn);
    }

    private void handleSearchFlightTextField() {
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            loadSearchFlightTable();
        });
    }

    public void selectDateEvent(ActionEvent actionEvent) {
        loadSearchFlightTable();
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
                .map(f -> new FlightSummary(f.getAirport(), f.getFlightDateTime().toLocalTime(), f.getAvailableSpots()))
                .forEach(searchTableViewModelGrade::add);

        searchAirportColumn.setCellValueFactory(new PropertyValueFactory<>("airport"));
        searchTimeColumn.setCellValueFactory(new PropertyValueFactory<>("schedule"));
        searchSpotsColumn.setCellValueFactory(new PropertyValueFactory<>("availableSpots"));
        searchTableView.getSortOrder().add(searchTimeColumn);
        searchTableView.setItems(searchTableViewModelGrade);
    }

    public void confirmTicketsButtonOnMouseClicked(MouseEvent mouseEvent) {
    }
}
