package ubb.tourism.business.service;

import ubb.tourism.data.access.entity.User;

public interface UserService {

    User getUserByUsernameAndPassword(String username, String password);
}
