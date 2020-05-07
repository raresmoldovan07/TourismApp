package tourism.app.network.protocol.request;

import tourism.app.network.dto.UserDTO;

public class LoginRequest extends Request {

    private UserDTO userDTO;

    public LoginRequest(UserDTO userDTO) {
        super("LoginRequest");
        this.userDTO = userDTO;
    }

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }
}

