package ubb.tourism.data.access.repository.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ubb.tourism.data.access.entity.Ticket;
import ubb.tourism.data.access.repository.CrudRepository;
import ubb.tourism.data.access.utils.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TicketRepositoryImpl implements CrudRepository<Integer, Ticket> {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final String FIND_ALL_QUERY = "select * from ticket;";
    private static final String FIND_ONE_QUERY = "select * from ticket where ticket_id = ?;";
    private static final String UPDATE_QUERY = "update ticket set available_spots=?, client_name=?, client_address=?, tourists=? where ticket_id = ?;";
    private static final String DELETE_QUERY = "delete from ticket where ticket_id = ?;";
    private static final String SAVE_QUERY = "insert into ticket (available_spots, client_name, client_address, tourists) values (?, ?, ?, ?);";
    private static final String SIZE_QUERY = "select count(*) as size from ticket;";

    private JdbcUtils jdbcUtils;

    public TicketRepositoryImpl(JdbcUtils jdbcUtils) {
        this.jdbcUtils = jdbcUtils;
    }

    @Override
    public int size() {
        Connection connection = jdbcUtils.getConnection();
        LOGGER.info("Getting size of ticket repository");
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SIZE_QUERY);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("size");
            }
        } catch (SQLException e) {
            LOGGER.error("Error getting size of ticket repository", e);
        }
        return 0;
    }

    @Override
    public void save(Ticket ticket) {
        Connection connection = jdbcUtils.getConnection();
        LOGGER.info("Saving new ticket");
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SAVE_QUERY);
            preparedStatement.setInt(1, ticket.getAvailableSpots());
            preparedStatement.setString(2, ticket.getClientName());
            preparedStatement.setString(3, ticket.getClientAddress());
            preparedStatement.setString(4, ticket.getTourists());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Error saving new ticket", e);
        }
    }

    @Override
    public void update(Integer ticketId, Ticket ticket) {
        Connection connection = jdbcUtils.getConnection();
        LOGGER.info("Updating ticket with ticketId {}", ticketId);
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_QUERY);
            preparedStatement.setInt(1, ticket.getAvailableSpots());
            preparedStatement.setString(2, ticket.getClientName());
            preparedStatement.setString(3, ticket.getClientAddress());
            preparedStatement.setString(4, ticket.getTourists());
            preparedStatement.setInt(5, ticketId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Error updating ticket with ticketId {}", ticketId, e);
        }
    }

    @Override
    public void delete(Integer ticketId) {
        Connection connection = jdbcUtils.getConnection();
        LOGGER.info("Deleting ticket with ticketId {}", ticketId);
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_QUERY);
            preparedStatement.setInt(1, ticketId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Error deleting ticket with ticketId {}", ticketId, e);
        }
    }

    @Override
    public Ticket findOne(Integer ticketId) {
        Connection connection = jdbcUtils.getConnection();
        LOGGER.info("Getting ticket with ticketId {}", ticketId);
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_ONE_QUERY);
            preparedStatement.setInt(1, ticketId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Integer availableSpots = resultSet.getInt("available_spots");
                String clientName = resultSet.getString("client_name");
                String clientAddress = resultSet.getString("client_address");
                String tourists = resultSet.getString("tourists");
                return new Ticket(ticketId, availableSpots, clientName, clientAddress, tourists);
            }
        } catch (SQLException e) {
            LOGGER.error("Error getting ticket with ticketId {}", ticketId, e);
        }
        return null;
    }

    @Override
    public Iterable<Ticket> findAll() {
        Connection connection = jdbcUtils.getConnection();
        List<Ticket> tickets = new ArrayList<>();
        LOGGER.info("Getting all tickets");
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_QUERY);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Integer ticketId = resultSet.getInt("ticket_id");
                Integer availableSpots = resultSet.getInt("available_spots");
                String clientName = resultSet.getString("client_name");
                String clientAddress = resultSet.getString("client_address");
                String tourists = resultSet.getString("tourists");
                tickets.add(new Ticket(ticketId, availableSpots, clientName, clientAddress, tourists));
            }
        } catch (SQLException e) {
            LOGGER.error("Error getting all tickets", e);
        }
        return tickets;
    }
}