package tourism.app.persistence.data.access.repository.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tourism.app.persistence.data.access.entity.Flight;
import tourism.app.persistence.data.access.repository.FlightRepository;
import tourism.app.persistence.data.access.utils.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FlightRepositoryImpl implements FlightRepository {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final String FIND_ALL_QUERY = "select * from flight;";
    private static final String FIND_ONE_QUERY = "select * from flight where flight_id = ?;";
    private static final String UPDATE_QUERY = "update flight set destination=?, airport=?, flight_date_time=?, available_spots=? where flight_id = ?;";
    private static final String DELETE_QUERY = "delete from flight where flight_id = ?;";
    private static final String SAVE_QUERY = "insert into flight (destination, airport, flight_date_time, available_spots) values (?, ?, ?, ?);";
    private static final String SIZE_QUERY = "select count(*) as size from flight;";

    private JdbcUtils jdbcUtils;

    public FlightRepositoryImpl(JdbcUtils jdbcUtils) {
        this.jdbcUtils = jdbcUtils;
    }

    @Override
    public int size() {
        LOGGER.info("Getting size of flight repository");
        try (Connection connection = jdbcUtils.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SIZE_QUERY);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("size");
            }
        } catch (SQLException e) {
            LOGGER.error("Error getting size of flight repository", e);
        }
        return 0;
    }

    @Override
    public void save(Flight flight) {
        LOGGER.info("Saving new flight");
        try (Connection connection = jdbcUtils.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SAVE_QUERY);
            preparedStatement.setString(1, flight.getDestination());
            preparedStatement.setString(2, flight.getAirport());
            preparedStatement.setString(3, flight.getFlightDateTime().toString());
            preparedStatement.setInt(4, flight.getAvailableSpots());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Error saving new flight", e);
        }
    }

    @Override
    public void update(Integer flightId, Flight flight) {
        LOGGER.info("Updating flight with flightId {}", flightId);
        try (Connection connection = jdbcUtils.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_QUERY);
            preparedStatement.setString(1, flight.getDestination());
            preparedStatement.setString(2, flight.getAirport());
            preparedStatement.setString(3, flight.getFlightDateTime().toString());
            preparedStatement.setInt(4, flight.getAvailableSpots());
            preparedStatement.setInt(5, flightId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Error updating flight with flightId {}", flightId, e);
        }
    }

    @Override
    public void delete(Integer flightId) {
        LOGGER.info("Deleting flight with flightId {}", flightId);
        try (Connection connection = jdbcUtils.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_QUERY);
            preparedStatement.setInt(1, flightId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Error deleting flight with flightId {}", flightId, e);
        }
    }

    @Override
    public Flight findOne(Integer flightId) {
        LOGGER.info("Getting flight with flightId {}", flightId);
        try (Connection connection = jdbcUtils.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_ONE_QUERY);
            preparedStatement.setInt(1, flightId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String destination = resultSet.getString("destination");
                String airport = resultSet.getString("airport");
                LocalDateTime flightDateTime = resultSet.getTimestamp("flight_date_time").toLocalDateTime();
                Integer availableSpots = resultSet.getInt("available_spots");
                return new Flight(flightId, destination, airport, flightDateTime, availableSpots);
            }
        } catch (SQLException e) {
            LOGGER.error("Error getting flight with flightId {}", flightId, e);
        }
        return null;
    }

    @Override
    public Iterable<Flight> findAll() {
        List<Flight> flights = new ArrayList<>();
        LOGGER.info("Getting all flights");
        try (Connection connection = jdbcUtils.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_QUERY);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Integer flightId = resultSet.getInt("flight_id");
                String destination = resultSet.getString("destination");
                String airport = resultSet.getString("airport");
                LocalDateTime flightDateTime = resultSet.getTimestamp("flight_date_time").toLocalDateTime();
                Integer availableSpots = resultSet.getInt("available_spots");
                flights.add(new Flight(flightId, destination, airport, flightDateTime, availableSpots));
            }
        } catch (SQLException e) {
            LOGGER.error("Error getting all tickets", e);
        }
        return flights;
    }

    @Override
    public void getFlightByDestAndDate(String destination, LocalDateTime flightDateTime) {

    }
}
