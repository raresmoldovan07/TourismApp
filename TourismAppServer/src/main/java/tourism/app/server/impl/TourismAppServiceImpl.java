package tourism.app.server.impl;

import tourism.app.persistence.data.access.entity.Flight;
import tourism.app.persistence.data.access.entity.Ticket;
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
    public User getUserByUsernameAndPassword(String username, String password, Observer client) throws ServiceException {
        User user = userRepository.getUserByUsernameAndPassword(username, password);
        if (user == null) {
            throw new ServiceException("User not found");
        }
        loggedUsers.put(user.getId(), client);
        return user;
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
