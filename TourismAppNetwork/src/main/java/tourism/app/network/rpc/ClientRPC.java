package tourism.app.network.rpc;

import tourism.app.network.dto.Converter;
import tourism.app.network.dto.UserDTO;
import tourism.app.persistence.data.access.entity.User;
import tourism.app.services.Observer;
import tourism.app.services.TourismAppService;
import tourism.app.services.exception.ServiceException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientRPC implements Runnable, Observer {

    private static Response okResponse = new Response.Builder().type(ResponseType.OK).build();
    private TourismAppService tourismAppService;
    private Socket connection;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;

    public ClientRPC(TourismAppService tourismAppService, Socket socket) {
        this.tourismAppService = tourismAppService;
        this.connection = socket;
        try {
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            connected = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (connected) {
            try {
                Object request = input.readObject();
                Response response = handleRequest((Request) request);
                if (response != null) {
                    sendResponse(response);
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            System.out.println("Error " + e);
        }
    }

    @Override
    public void update() {

    }

    private Response handleRequest(Request request) {
        Response response = null;
        if (request.getRequestType() == RequestType.LOGIN) {
            return handleLoginRequest(request);
        }
        return null;
    }

    private Response handleLoginRequest(Request request) {
        System.out.println("Handling login request");
        UserDTO userDTO = (UserDTO) request.getData();
        try {
            User user = tourismAppService.getUserByUsernameAndPassword(userDTO.getUsername(), userDTO.getPassword(), this);
            return new Response.Builder().type(ResponseType.USER_LOGGED_IN).data(Converter.getUserDTO(user)).build();
        } catch (ServiceException e) {
            connected = false;
            System.out.println(String.format("Error handling login request %s", e));
        }
        return new Response.Builder().type(ResponseType.ERROR).build();
    }

    private void sendResponse(Response response) throws IOException {
        System.out.println("Sending response " + response);
        output.writeObject(response);
        output.flush();
    }
}
