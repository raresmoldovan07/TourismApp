package tourism.app.network.protocol.request;

import java.io.Serializable;

public class Request implements Serializable{

    private String type;

    public Request(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

