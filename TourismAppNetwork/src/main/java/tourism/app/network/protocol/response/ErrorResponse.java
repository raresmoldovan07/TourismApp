package tourism.app.network.protocol.response;

public class ErrorResponse extends Response {

    private String message;

    public ErrorResponse(String message) {
        super("ErrorResponse");
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
