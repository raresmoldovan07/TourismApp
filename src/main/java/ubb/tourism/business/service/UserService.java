package ubb.tourism.business.service;

import tourism.app.persistence.data.access.entity.User;

public interface UserService {

    User getUserByUsernameAndPassword(String username, String password);
}
