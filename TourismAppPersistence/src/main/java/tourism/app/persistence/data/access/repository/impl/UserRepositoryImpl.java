package tourism.app.persistence.data.access.repository.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tourism.app.persistence.data.access.entity.User;
import tourism.app.persistence.data.access.repository.UserRepository;
import tourism.app.persistence.data.access.utils.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserRepositoryImpl implements UserRepository {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final String FIND_ALL_QUERY = "select * from user;";
    private static final String FIND_ONE_QUERY = "select * from user where user_id = ?;";
    private static final String UPDATE_QUERY = "update user set username=?, name=?, password=? where user_id = ?;";
    private static final String DELETE_QUERY = "delete from user where user_id = ?;";
    private static final String SAVE_QUERY = "insert into user (username, name, password) values (?, ?, ?);";
    private static final String SIZE_QUERY = "select count(*) as SIZE from user;";
    private static final String GET_USER_BY_USERNAME_AND_PASSWORD_QUERY = "select * from user where username=? and password=?";

    private JdbcUtils jdbcUtils;

    public UserRepositoryImpl(JdbcUtils jdbcUtils) {
        this.jdbcUtils = jdbcUtils;
    }

    @Override
    public int size() {
        LOGGER.info("Getting size of user repository");
        try (Connection connection = jdbcUtils.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SIZE_QUERY);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("SIZE");
            }
        } catch (SQLException e) {
            LOGGER.error("Error getting size of user repository", e);
        }
        return 0;
    }

    @Override
    public void save(User user) {
        LOGGER.info("Saving new user with username {}", user.getUsername());
        try (Connection connection = jdbcUtils.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SAVE_QUERY);
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getName());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Error saving new user with username {}", user.getUsername(), e);
        }
    }

    @Override
    public void update(Integer userId, User user) {
        LOGGER.info("Updating user with userId {}", userId);
        try (Connection connection = jdbcUtils.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_QUERY);
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getName());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setInt(4, userId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Error updating user with userId {}", userId, e);
        }
    }

    @Override
    public void delete(Integer userId) {
        LOGGER.info("Deleting user with userId {}", userId);
        try (Connection connection = jdbcUtils.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_QUERY);
            preparedStatement.setInt(1, userId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Error deleting user with userId {}", userId, e);
        }
    }

    @Override
    public User findOne(Integer userId) {
        LOGGER.info("Getting user with userId {}", userId);
        try (Connection connection = jdbcUtils.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_ONE_QUERY);
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return getUserFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            LOGGER.error("Error getting user with userId {}", userId, e);
        }
        return null;
    }

    @Override
    public Iterable<User> findAll() {
        List<User> users = new ArrayList<>();
        LOGGER.info("Getting all users");
        try (Connection connection = jdbcUtils.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_QUERY);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                users.add(getUserFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            LOGGER.error("Error getting all users", e);
        }
        return users;
    }

    @Override
    public User getUserByUsernameAndPassword(String username, String password) {
        LOGGER.info("Getting user with username {}", username);
        try (Connection connection = jdbcUtils.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_USER_BY_USERNAME_AND_PASSWORD_QUERY);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return getUserFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            LOGGER.error("Error getting user with username {}", username);
        }
        return null;
    }

    private User getUserFromResultSet(ResultSet resultSet) {
        try {
            Integer userId = resultSet.getInt("user_id");
            String name = resultSet.getString("name");
            String username = resultSet.getString("username");
            String password = resultSet.getString("password");
            return new User(userId, username, password, name);
        } catch (SQLException e) {
            LOGGER.error("Error mapping resultSet to user");
        }
        return null;
    }
}
