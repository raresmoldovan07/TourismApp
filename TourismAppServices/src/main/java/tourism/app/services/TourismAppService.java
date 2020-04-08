package tourism.app.services;

import tourism.app.persistence.data.access.entity.User;
import tourism.app.services.exception.ServiceException;

public interface TourismAppService {

    void login(User user, Observer client) throws ServiceException;

    void logout(User user, Observer client) throws ServiceException;

    User[] getLoggedUsers(User user) throws ServiceException;
}
