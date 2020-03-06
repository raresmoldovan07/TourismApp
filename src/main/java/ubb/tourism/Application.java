package ubb.tourism;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ubb.tourism.data.access.entity.User;
import ubb.tourism.data.access.repository.UserRepository;
import ubb.tourism.data.access.repository.impl.UserRepositoryImpl;
import ubb.tourism.data.access.utils.JdbcUtils;
import ubb.tourism.data.validator.impl.UserValidator;

import java.io.IOException;
import java.util.Properties;

public class Application {

    public static final Logger LOGGER = LogManager.getLogger();

    public static void main(String[] args) {
        testSize();
    }

    private static void testSize() {
        Properties properties = getProperties();
        JdbcUtils jdbcUtils = new JdbcUtils(properties);
        UserValidator userValidator = new UserValidator();
        UserRepository userRepository = new UserRepositoryImpl(jdbcUtils, userValidator);
        System.out.println(String.format("Size of user repository is %s", userRepository.size()));
        int id = 12;

        User user = new User("username", "password", "name");

        userRepository.save(user);
        System.out.println(String.format("Size of user repository is %s", userRepository.size()));

        user = userRepository.findOne(id);
        System.out.println("Saved user: " + user);

        user.setName("NewName");
        userRepository.update(id, user);

        user = userRepository.findOne(id);
        System.out.println("New user: " + user);

        userRepository.delete(id);
        System.out.println(String.format("Size of user repository is %s", userRepository.size()));
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
