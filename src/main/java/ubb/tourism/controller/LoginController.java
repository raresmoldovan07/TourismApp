package ubb.tourism.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import ubb.tourism.business.exception.UserNotFoundException;
import ubb.tourism.business.service.FlightService;
import ubb.tourism.business.service.TicketService;
import ubb.tourism.data.access.entity.User;
import ubb.tourism.business.service.UserService;

public class LoginController {

    @FXML
    public TextField usernameTextField;
    @FXML
    public PasswordField passwordTextField;
    @FXML
    public Button signingButton;
    @FXML
    public Label failedAuthenticationLabel;

    private FlightService flightService;
    private TicketService ticketService;
    private UserService userService;

    public LoginController(FlightService flightService, TicketService ticketService, UserService userService) {
        this.flightService = flightService;
        this.ticketService = ticketService;
        this.userService = userService;
    }

    public void signingButtonOnMouseClicked(MouseEvent mouseEvent) {
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();
        try {
            User user = userService.getUserByUsernameAndPassword(username, password);
        } catch (UserNotFoundException e) {
            failedAuthenticationLabel.setVisible(true);
        }
    }

    public void usernameTextFieldOnMouseClicked(MouseEvent mouseEvent) {
        failedAuthenticationLabel.setVisible(false);
    }


    public void passwordTextFieldOnMouseClicked(MouseEvent mouseEvent) {
        failedAuthenticationLabel.setVisible(false);
    }
}
