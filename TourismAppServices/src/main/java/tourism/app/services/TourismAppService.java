package tourism.app.services;

import tourism.app.persistence.data.access.entity.Flight;
import tourism.app.persistence.data.access.entity.Ticket;
import tourism.app.persistence.data.access.entity.User;
import tourism.app.services.exception.ServiceException;

public interface TourismAppService {

    void login(User user, Observer client) throws ServiceException;

    void logout(User user, Observer client) throws ServiceException;

    User[] getLoggedUsers(User user) throws ServiceException;

    User getUserByUsernameAndPassword(String username, String password);

    Iterable<Flight> findAll();

    void save(Ticket ticket);

    void update(Integer flightId, Flight flight);
}
