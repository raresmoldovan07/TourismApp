package tourism.app.client.controller;

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
import tourism.app.services.TourismAppService;
import tourism.app.persistence.data.access.entity.User;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
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

    private TourismAppService tourismAppService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public LoginController(TourismAppService tourismAppService) {
        this.tourismAppService = tourismAppService;
    }

    public void signingButtonOnMouseClicked(MouseEvent mouseEvent) throws RemoteException {
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();
        HomeController homeController = new HomeController(tourismAppService);
        User user = null;
        try {
            user = tourismAppService.getUserByUsernameAndPassword(username, password, homeController);
        } catch (RuntimeException e) {
            failedAuthenticationLabel.setVisible(true);
            return;
        }
        usernameTextField.clear();
        passwordTextField.clear();
        openHomePage(homeController);
    }

    public void usernameTextFieldOnMouseClicked(MouseEvent mouseEvent) {
        failedAuthenticationLabel.setVisible(false);
    }


    public void passwordTextFieldOnMouseClicked(MouseEvent mouseEvent) {
        failedAuthenticationLabel.setVisible(false);
    }

    private void openHomePage(HomeController homeController) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/gui/home-stage.fxml"));
        fxmlLoader.setControllerFactory(c -> homeController);
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
