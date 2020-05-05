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
//        TourismAppService tourismAppService = applicationContext.getBean(TourismAppServiceImpl.class);
//
//        System.out.println("Starting server on port " + DEFAULT_PORT);
//
//        AbstractServer server = new RPCServer(DEFAULT_PORT, tourismAppService);
//        try {
//            server.start();
//        } catch (ServerException e) {
//            System.out.println(String.format("Error starting the server %s", e));
//        } finally {
//            try {
//                server.stop();
//            } catch (ServerException e) {
//                System.out.println(String.format("Error stopping the server %s", e));
//            }
//        }
    }
}
