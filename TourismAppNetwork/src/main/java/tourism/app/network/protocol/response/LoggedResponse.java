package tourism.app.network.protocol.response;

import tourism.app.network.dto.UserDTO;

public class LoggedResponse extends Response {

    private UserDTO userDTO;

    public LoggedResponse(UserDTO userDTO) {
        super("LoggedResponse");
        this.userDTO = userDTO;
    }

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }
}
