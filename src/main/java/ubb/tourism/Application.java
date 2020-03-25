package ubb.tourism;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ubb.tourism.controller.LoginController;
import ubb.tourism.data.access.repository.FlightRepository;
import ubb.tourism.data.access.repository.UserRepository;
import ubb.tourism.data.access.repository.impl.FlightRepositoryImpl;
import ubb.tourism.data.access.repository.impl.TicketRepositoryImpl;
import ubb.tourism.data.access.repository.impl.UserRepositoryImpl;
import ubb.tourism.business.service.UserService;

public class Application extends javafx.application.Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring-context.xml");

        UserRepository userRepository = applicationContext.getBean(UserRepositoryImpl.class);
        TicketRepositoryImpl ticketRepository = applicationContext.getBean(TicketRepositoryImpl.class);
        FlightRepository flightRepository = applicationContext.getBean(FlightRepositoryImpl.class);

        UserService userService = applicationContext.getBean(UserService.class);

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/gui/login-stage.fxml"));
        fxmlLoader.setControllerFactory(c -> {
            return applicationContext.getBean(LoginController.class);
        });
        AnchorPane anchorPane = fxmlLoader.load();

        primaryStage.setTitle("TourismApp");
        primaryStage.setScene(new Scene(anchorPane));
        primaryStage.show();
    }
}
