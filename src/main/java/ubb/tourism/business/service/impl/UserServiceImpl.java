package ubb.tourism.business.service.impl;

import ubb.tourism.business.exception.UserNotFoundException;
import ubb.tourism.business.service.Observable;
import ubb.tourism.business.service.UserService;
import tourism.app.persistence.data.access.entity.User;
import tourism.app.persistence.data.access.repository.UserRepository;

public class UserServiceImpl extends Observable implements UserService {

    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserByUsernameAndPassword(String username, String password) {
        User user = userRepository.getUserByUsernameAndPassword(username, password);
        if(user == null) {
            throw new UserNotFoundException();
        }
        return user;
    }
}
