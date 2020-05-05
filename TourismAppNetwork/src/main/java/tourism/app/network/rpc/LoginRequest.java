package tourism.app.network.rpc;

import tourism.app.network.dto.UserDTO;

public class LoginRequest implements Request {

    private UserDTO userDTO;

    public LoginRequest(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }
}

