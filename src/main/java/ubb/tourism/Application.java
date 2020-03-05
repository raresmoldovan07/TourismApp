package ubb.tourism;

import ubb.tourism.data.access.utils.JdbcUtils;

import java.io.IOException;
import java.sql.Connection;
import java.util.Properties;

public class Application {

    public static void main(String[] args) {
        ClassLoader classLoader = Application.class.getClassLoader();
        Properties properties = new Properties();
        try {
            properties.load(classLoader.getResourceAsStream("config/mysql-config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(properties);
        JdbcUtils jdbcUtils = new JdbcUtils(properties);
        Connection connection = jdbcUtils.getConnection();
    }
}
