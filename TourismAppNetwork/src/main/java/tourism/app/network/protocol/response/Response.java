package tourism.app.network.protocol.response;

import java.io.Serializable;

public class Response implements Serializable {

    private String type;

    public Response(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

