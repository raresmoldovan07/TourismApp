package ubb.tourism.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import ubb.tourism.business.exception.UserNotFoundException;
import ubb.tourism.business.service.impl.FlightServiceImpl;
import ubb.tourism.business.service.impl.TicketServiceImpl;
import tourism.app.persistence.data.access.entity.User;
import ubb.tourism.business.service.impl.UserServiceImpl;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    public TextField usernameTextField;
    @FXML
    public PasswordField passwordTextField;
    @FXML
    public Button signingButton;
    @FXML
    public Label failedAuthenticationLabel;

    private FlightServiceImpl flightService;
    private TicketServiceImpl ticketService;
    private UserServiceImpl userService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public LoginController(FlightServiceImpl flightService, TicketServiceImpl ticketService, UserServiceImpl userService) {
        this.flightService = flightService;
        this.ticketService = ticketService;
        this.userService = userService;
    }

    public void signingButtonOnMouseClicked(MouseEvent mouseEvent) {
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();
        User user = null;
        try {
            user = userService.getUserByUsernameAndPassword(username, password);
        } catch (UserNotFoundException e) {
            failedAuthenticationLabel.setVisible(true);
            return;
        }
        usernameTextField.clear();
        passwordTextField.clear();
        openHomePage(user);

    }

    public void usernameTextFieldOnMouseClicked(MouseEvent mouseEvent) {
        failedAuthenticationLabel.setVisible(false);
    }


    public void passwordTextFieldOnMouseClicked(MouseEvent mouseEvent) {
        failedAuthenticationLabel.setVisible(false);
    }

    private void openHomePage(User user) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/gui/home-stage.fxml"));
        fxmlLoader.setControllerFactory(c -> new HomeController(flightService, ticketService, userService, user));
        AnchorPane anchorPane = null;
        try {
            anchorPane = fxmlLoader.load();
            Stage primaryStage = new Stage();
            primaryStage.setTitle("TourismApp");
            primaryStage.setResizable(false);
            primaryStage.setScene(new Scene(anchorPane));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
