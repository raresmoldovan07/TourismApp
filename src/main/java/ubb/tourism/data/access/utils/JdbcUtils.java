package ubb.tourism.data.access.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JdbcUtils {

    private Connection connection = null;
    private Properties properties;

    public JdbcUtils(Properties properties) {
        this.properties = properties;
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = getNewConnection();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    private Connection getNewConnection() {
        String url = properties.getProperty("tourism.jdbc.url");
        String user = properties.getProperty("tourism.jdbc.user");
        String password = properties.getProperty("tourism.jdbc.password");

        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
