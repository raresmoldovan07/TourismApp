package tourism.app.server;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import tourism.app.network.util.AbstractServer;
import tourism.app.network.util.ServerException;
import tourism.app.network.util.impl.RPCServer;
import tourism.app.server.impl.TourismAppServiceImpl;
import tourism.app.services.TourismAppService;

public class RPCServerStarter {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring-context.xml");
    }
}
