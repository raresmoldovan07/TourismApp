package tourism.app.services;

import tourism.app.persistence.data.access.entity.Flight;
import tourism.app.persistence.data.access.entity.Ticket;
import tourism.app.persistence.data.access.entity.User;
import tourism.app.services.exception.ServiceException;

public interface TourismAppService {

    User getUserByUsernameAndPassword(String username, String password, Observer client) throws ServiceException;

    Iterable<Flight> findAll();

    void save(Ticket ticket);

    void update(Integer flightId, Flight flight);
}
