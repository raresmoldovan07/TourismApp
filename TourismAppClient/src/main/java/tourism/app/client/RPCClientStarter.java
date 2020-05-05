package tourism.app.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import tourism.app.client.controller.LoginController;
import tourism.app.services.TourismAppService;

public class RPCClientStarter extends Application {

    private static final int DEFAULT_PORT = 1099;
    private static final String DEFAULT_SERVER = "localhost";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println(String.format("Connecting client to %s:%s", DEFAULT_SERVER, DEFAULT_PORT));
        ApplicationContext factory = new ClassPathXmlApplicationContext("classpath:spring-context.xml");
        TourismAppService tourismAppService = (TourismAppService) factory.getBean("tourismAppService");

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/gui/login-stage.fxml"));
        fxmlLoader.setControllerFactory(c -> new LoginController(tourismAppService));
        AnchorPane anchorPane = fxmlLoader.load();

        primaryStage.setTitle("TourismApp");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(anchorPane));
        primaryStage.show();
    }
}
