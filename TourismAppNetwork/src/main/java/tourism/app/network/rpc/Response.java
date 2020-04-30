package tourism.app.network.rpc;

import java.io.Serializable;

public class Response implements Serializable {

    public ResponseType responseType;

    public Object data;

    private Response() {
    }

    public ResponseType getResponseType() {
        return responseType;
    }

    public void setResponseType(ResponseType responseType) {
        this.responseType = responseType;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Response{" +
                "responseType=" + responseType +
                ", data=" + data +
                '}';
    }

    public static class Builder {
        private Response response = new Response();

        public Builder type(ResponseType type) {
            response.setResponseType(type);
            return this;
        }

        public Builder data(Object data) {
            response.setData(data);
            return this;
        }

        public Response build() {
            return response;
        }
    }
}
