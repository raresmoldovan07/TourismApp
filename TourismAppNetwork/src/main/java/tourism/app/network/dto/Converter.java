package tourism.app.network.dto;

import tourism.app.persistence.data.access.entity.User;

public class Converter {

    public static UserDTO getUserDTO(User user) {
        return new UserDTO(user.getId(), user.getUsername(), user.getPassword(), user.getName());
    }

    public static User getUser(UserDTO userDTO) {
        return new User(userDTO.getId(), userDTO.getUsername(), userDTO.getPassword(), userDTO.getName());
    }
}
