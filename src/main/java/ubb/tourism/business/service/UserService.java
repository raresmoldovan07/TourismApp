package ubb.tourism.business.service;

import ubb.tourism.business.exception.UserNotFoundException;
import ubb.tourism.data.access.entity.User;
import ubb.tourism.data.access.repository.UserRepository;

public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
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
