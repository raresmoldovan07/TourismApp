package ubb.tourism;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ubb.tourism.data.access.repository.FlightRepository;
import ubb.tourism.data.access.repository.UserRepository;
import ubb.tourism.data.access.repository.impl.FlightRepositoryImpl;
import ubb.tourism.data.access.repository.impl.TicketRepositoryImpl;
import ubb.tourism.data.access.repository.impl.UserRepositoryImpl;

public class Application {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring-context.xml");

        UserRepository userRepository = applicationContext.getBean(UserRepositoryImpl.class);
        TicketRepositoryImpl ticketRepository = applicationContext.getBean(TicketRepositoryImpl.class);
        FlightRepository flightRepository = applicationContext.getBean(FlightRepositoryImpl.class);

        userRepository.findAll().forEach(System.out::println);
        ticketRepository.findAll().forEach(System.out::println);
        flightRepository.findAll().forEach(System.out::println);
    }
}
