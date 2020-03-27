package ubb.tourism;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ubb.tourism.controller.LoginController;

public class Application extends javafx.application.Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring-context.xml");

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/gui/login-stage.fxml"));
        fxmlLoader.setControllerFactory(c -> applicationContext.getBean(LoginController.class));
        AnchorPane anchorPane = fxmlLoader.load();

        primaryStage.setTitle("TourismApp");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(anchorPane));
        primaryStage.show();
    }
}
