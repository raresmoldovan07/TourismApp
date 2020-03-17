package ubb.tourism;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ubb.tourism.data.access.entity.Flight;
import ubb.tourism.data.access.entity.Ticket;
import ubb.tourism.data.access.repository.FlightRepository;
import ubb.tourism.data.access.repository.UserRepository;
import ubb.tourism.data.access.repository.impl.FlightRepositoryImpl;
import ubb.tourism.data.access.repository.impl.TicketRepositoryImpl;
import ubb.tourism.data.access.repository.impl.UserRepositoryImpl;
import ubb.tourism.data.access.utils.JdbcUtils;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

public class Application {

    public static final Logger LOGGER = LogManager.getLogger();

    public static void main(String[] args) {
        testSize();
    }

    private static void testSize() {
        Properties properties = getProperties();
        JdbcUtils jdbcUtils = new JdbcUtils(properties);
        UserRepository userRepository = new UserRepositoryImpl(jdbcUtils);
        FlightRepository flightRepository = new FlightRepositoryImpl(jdbcUtils);
        List<Flight> flightList = (List<Flight>) flightRepository.findAll();
        TicketRepositoryImpl ticketRepository = new TicketRepositoryImpl(jdbcUtils);
        List<Ticket> ticketList = (List<Ticket>) ticketRepository.findAll();
    }

    private static Properties getProperties() {
        ClassLoader classLoader = Application.class.getClassLoader();
        Properties properties = new Properties();
        try {
            properties.load(classLoader.getResourceAsStream("config/mysql-config.properties"));
        } catch (IOException e) {
            LOGGER.error("Error loading mysql configuration file", e);
        }
        LOGGER.info("MySQL properties have been loaded successfully");
        return properties;
    }
}
