package tourism.app.persistence.data.access.repository.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.hql.spi.id.cte.CteValuesListBulkIdStrategy;
import tourism.app.persistence.data.access.entity.Ticket;
import tourism.app.persistence.data.access.entity.User;
import tourism.app.persistence.data.access.repository.TicketRepository;
import tourism.app.persistence.data.access.utils.HibernateUtils;
import tourism.app.persistence.data.access.utils.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TicketRepositoryImpl implements TicketRepository {

    private static final Logger LOGGER = LogManager.getLogger();

    private SessionFactory sessionFactory;

    private static final String FIND_ALL_QUERY = "select * from ticket;";
    private static final String FIND_ONE_QUERY = "select * from ticket where ticket_id = ?;";
    private static final String UPDATE_QUERY = "update ticket set flight_id=?, spots=?, client_name=?, client_address=?, tourists=? where ticket_id = ?;";
    private static final String DELETE_QUERY = "delete from ticket where ticket_id = ?;";
    private static final String SAVE_QUERY = "insert into ticket (flight_id, spots, client_name, client_address, tourists) values (?, ?, ?, ?, ?);";
    private static final String SIZE_QUERY = "select count(*) as size from ticket;";

    private JdbcUtils jdbcUtils;

    public TicketRepositoryImpl(JdbcUtils jdbcUtils) {
        this.jdbcUtils = jdbcUtils;
        sessionFactory = HibernateUtils.initializeSessionFactory();
    }

    @Override
    public int size() {
        LOGGER.info("Getting size of ticket repository");
        try (Connection connection = jdbcUtils.getConnection()) {
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
        LOGGER.info("Saving new ticket");
        try(Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                session.save(ticket);
                transaction.commit();
            } catch(RuntimeException e) {
                LOGGER.error("Error saving ticket", e);
                if (transaction != null)
                    transaction.rollback();
            }
        }
    }

    @Override
    public void update(Integer ticketId, Ticket ticket) {
        LOGGER.info("Updating ticket with ticketId {}", ticketId);
        try (Connection connection = jdbcUtils.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_QUERY);
            setTicketFieldsInPreparedStatement(preparedStatement, ticket);
            preparedStatement.setInt(6, ticketId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Error updating ticket with ticketId {}", ticketId, e);
        }
    }

    @Override
    public void delete(Integer ticketId) {
        LOGGER.info("Deleting ticket with ticketId {}", ticketId);
        try (Connection connection = jdbcUtils.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_QUERY);
            preparedStatement.setInt(1, ticketId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Error deleting ticket with ticketId {}", ticketId, e);
        }
    }

    @Override
    public Ticket findOne(Integer ticketId) {
        LOGGER.info("Getting ticket with ticketId {}", ticketId);
        try (Connection connection = jdbcUtils.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_ONE_QUERY);
            preparedStatement.setInt(1, ticketId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return getTicketFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            LOGGER.error("Error getting ticket with ticketId {}", ticketId, e);
        }
        return null;
    }

    @Override
    public Iterable<Ticket> findAll() {
        List<Ticket> tickets = new ArrayList<>();
        LOGGER.info("Getting all tickets");
        try (Connection connection = jdbcUtils.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_QUERY);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                tickets.add(getTicketFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            LOGGER.error("Error getting all tickets", e);
        }
        return tickets;
    }

    private Ticket getTicketFromResultSet(ResultSet resultSet) {
        try {
            Integer ticketId = resultSet.getInt("ticket_id");
            Integer flightId = resultSet.getInt("flight_id");
            Integer spots = resultSet.getInt("spots");
            String clientName = resultSet.getString("client_name");
            String clientAddress = resultSet.getString("client_address");
            String tourists = resultSet.getString("tourists");
            return new Ticket(ticketId, flightId, spots, clientName, clientAddress, tourists);
        } catch (SQLException e) {
            LOGGER.error("Error getting ticket from resultSet");
        }
        return null;
    }

    private void setTicketFieldsInPreparedStatement(PreparedStatement preparedStatement, Ticket ticket) {
        try {
            preparedStatement.setInt(1, ticket.getFlightId());
            preparedStatement.setInt(2, ticket.getSpots());
            preparedStatement.setString(3, ticket.getClientName());
            preparedStatement.setString(4, ticket.getClientAddress());
            preparedStatement.setString(5, ticket.getTourists());
        } catch (SQLException e) {
            LOGGER.error("Error setting ticket fields in prepared statement");
        }
    }
}