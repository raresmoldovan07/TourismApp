package ubb.tourism.data.access.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ubb.tourism.Application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JdbcUtils {

    private static final Logger LOGGER = LogManager.getLogger(JdbcUtils.class);

    private Connection connection = null;
    private Properties properties;

    private String propertiesFileName;

    public JdbcUtils(Properties properties) {
        this.properties = properties;
    }

    public void setPropertiesFileName(String propertiesFileName) {
        this.propertiesFileName = propertiesFileName;
        loadProperties();
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

    private void loadProperties() {
        ClassLoader classLoader = Application.class.getClassLoader();
        try {
            properties.load(classLoader.getResourceAsStream(propertiesFileName));
        } catch (IOException e) {
            LOGGER.error("Error loading mysql configuration file", e);
        }
        LOGGER.info("MySQL properties have been loaded successfully");
    }
}
