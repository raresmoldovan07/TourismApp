package tourism.app.network.rpc;

import tourism.app.persistence.data.access.entity.Flight;
import tourism.app.persistence.data.access.entity.Ticket;
import tourism.app.persistence.data.access.entity.User;
import tourism.app.services.Observer;
import tourism.app.services.TourismAppService;
import tourism.app.services.exception.ServiceException;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ProxyRPC implements TourismAppService {

    private final int PORT;
    private final String HOST;

    private Observer client;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private Socket connection;

    private BlockingQueue<Response> responseBlockingQueue;

    public ProxyRPC(String host, int port) {
        this.HOST = host;
        this.PORT = port;
        responseBlockingQueue = new LinkedBlockingQueue<>();
    }

    @Override
    public void login(User user, Observer client) throws ServiceException {

    }

    @Override
    public void logout(User user, Observer client) throws ServiceException {

    }

    @Override
    public User[] getLoggedUsers(User user) throws ServiceException {
        return new User[0];
    }

    @Override
    public User getUserByUsernameAndPassword(String username, String password) {
        return null;
    }

    @Override
    public Iterable<Flight> findAll() {
        return null;
    }

    @Override
    public void save(Ticket ticket) {

    }

    @Override
    public void update(Integer flightId, Flight flight) {

    }
}
