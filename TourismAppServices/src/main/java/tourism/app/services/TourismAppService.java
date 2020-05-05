package tourism.app.services;

import tourism.app.model.entity.Flight;
import tourism.app.model.entity.Ticket;
import tourism.app.model.entity.User;
import tourism.app.services.exception.ServiceException;

public interface TourismAppService {

    User getUserByUsernameAndPassword(String username, String password, Observer client) throws ServiceException;

    Flight[] findAll();

    void save(Ticket ticket);
}
