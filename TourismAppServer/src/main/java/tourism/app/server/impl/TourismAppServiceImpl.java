package tourism.app.server.impl;

import tourism.app.persistence.data.access.entity.User;
import tourism.app.persistence.data.access.repository.FlightRepository;
import tourism.app.persistence.data.access.repository.TicketRepository;
import tourism.app.persistence.data.access.repository.UserRepository;
import tourism.app.services.Observer;
import tourism.app.services.TourismAppService;
import tourism.app.services.exception.ServiceException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TourismAppServiceImpl implements TourismAppService {

    private FlightRepository flightRepository;
    private TicketRepository ticketRepository;
    private UserRepository userRepository;

    private Map<Integer, Observer> loggedUsers;

    public TourismAppServiceImpl(FlightRepository flightRepository, TicketRepository ticketRepository, UserRepository userRepository) {
        this.flightRepository = flightRepository;
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
        loggedUsers = new ConcurrentHashMap<>();
    }

    @Override
    public synchronized void login(User user, Observer client) throws ServiceException {
        User loggedUser = userRepository.getUserByUsernameAndPassword(user.getUsername(), user.getPassword());
        if (loggedUser == null) {
            throw new ServiceException("Error authenticating user");
        }
        if (loggedUsers.get(loggedUser.getId()) != null) {
            throw new ServiceException("User is already authenticated");
        }
        loggedUsers.put(loggedUser.getId(), client);
        //todo notify logged in user
    }

    @Override
    public synchronized void logout(User user, Observer client) throws ServiceException {
        Observer localClient = loggedUsers.remove(user.getId());
        if (localClient == null) {
            throw new ServiceException(String.format("User with userId %s is not logged in", user.getId()));
        }
        //todo notify logged out user
    }

    @Override
    public synchronized User[] getLoggedUsers(User user) throws ServiceException {
        return new User[0];
    }
}
