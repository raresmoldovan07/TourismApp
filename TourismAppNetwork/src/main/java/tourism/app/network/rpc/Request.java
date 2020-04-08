package tourism.app.network.rpc;

public class Request {

    private RequestType requestType;
    private Object data;

    public RequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Request{" +
                "requestType=" + requestType +
                ", data=" + data +
                '}';
    }

    public static class Builder {

        private Request request = new Request();

        public Builder type(RequestType type) {
            request.setRequestType(type);
            return this;
        }

        public Builder data(Object data) {
            request.setData(data);
            return this;
        }

        public Request build() {
            return request;
        }
    }
}
