package tourism.app.server.impl;

import tourism.app.persistence.data.access.entity.User;
import tourism.app.persistence.data.access.repository.FlightRepository;
import tourism.app.persistence.data.access.repository.TicketRepository;
import tourism.app.persistence.data.access.repository.UserRepository;
import tourism.app.services.Observer;
import tourism.app.services.TourismAppService;
import tourism.app.services.exception.ServiceException;

public class TourismAppServiceImpl implements TourismAppService {

    private FlightRepository flightRepository;
    private TicketRepository ticketRepository;
    private UserRepository userRepository;

    public TourismAppServiceImpl(FlightRepository flightRepository, TicketRepository ticketRepository, UserRepository userRepository) {
        this.flightRepository = flightRepository;
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
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
}
