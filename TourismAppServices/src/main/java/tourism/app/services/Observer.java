package tourism.app.services;

import tourism.app.persistence.data.access.entity.User;
import tourism.app.services.exception.ServiceException;

public interface Observer {

    void notificationReceived();

    void userLoggedIn(User user) throws ServiceException;

    void userLoggedOut(User user) throws ServiceException;
}
