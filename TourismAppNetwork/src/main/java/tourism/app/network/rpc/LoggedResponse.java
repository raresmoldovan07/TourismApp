package tourism.app.network.rpc;

import tourism.app.network.dto.UserDTO;

public class LoggedResponse implements Response {

    private UserDTO userDTO;

    public LoggedResponse(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }
}
