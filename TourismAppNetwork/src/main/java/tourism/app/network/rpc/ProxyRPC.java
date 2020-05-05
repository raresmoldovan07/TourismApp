package tourism.app.network.rpc;

import tourism.app.network.dto.Converter;
import tourism.app.network.dto.FlightDTO;
import tourism.app.network.dto.TicketDTO;
import tourism.app.network.dto.UserDTO;
import tourism.app.persistence.data.access.entity.Flight;
import tourism.app.persistence.data.access.entity.Ticket;
import tourism.app.persistence.data.access.entity.User;
import tourism.app.services.Observer;
import tourism.app.services.TourismAppService;
import tourism.app.services.exception.ServiceException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ProxyRPC implements TourismAppService {

    private String host;
    private int port;

    private Observer client;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket connection;

    private BlockingQueue<Response> qresponses;
    private volatile boolean finished;

    public ProxyRPC(String host, int port) {
        this.host = host;
        this.port = port;
        qresponses = new LinkedBlockingQueue<>();
    }

    @Override
    public User getUserByUsernameAndPassword(String username, String password, Observer client) throws ServiceException {
        initializeConnection();
        UserDTO userDTO = new UserDTO(username, password);
        Request req = new Request.Builder().type(RequestType.LOGIN).data(userDTO).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.getResponseType() == ResponseType.USER_LOGGED_IN) {
            this.client = client;
            return Converter.getUser((UserDTO) response.getData());
        }
        if (response.getResponseType() == ResponseType.ERROR) {
            String err = response.getData().toString();
            closeConnection();
            throw new ServiceException(err);
        }
        return null;
    }

    @Override
    public Flight[] findAll() {
        Request req = new Request.Builder().type(RequestType.GET_ALL_FLIGHTS).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.getResponseType() == ResponseType.GET_ALL_FLIGHTS) {
            return Converter.getFlightsList((FlightDTO[]) response.getData());
        }
        if (response.getResponseType() == ResponseType.ERROR) {
            String err = response.getData().toString();
            closeConnection();
            throw new ServiceException(err);
        }
        return null;
    }

    @Override
    public void save(Ticket ticket) {
        TicketDTO ticketDTO = Converter.getTicketDTO(ticket);
        Request req = new Request.Builder().type(RequestType.SAVE_TICKET).data(ticketDTO).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.getResponseType() == ResponseType.ERROR) {
            String err = response.getData().toString();
            closeConnection();
            throw new ServiceException(err);
        }
    }

    private void closeConnection() {
        finished = true;
        try {
            input.close();
            output.close();
            connection.close();
            client = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendRequest(Request request) throws ServiceException {
        try {
            output.writeObject(request);
            output.flush();
        } catch (IOException e) {
            throw new ServiceException("Error sending request " + e);
        }
    }

    private Response readResponse() throws ServiceException {
        Response response = null;
        try {
            response = qresponses.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }

    private void initializeConnection() throws ServiceException {
        try {
            connection = new Socket(host, port);
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            finished = false;
            startReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startReader() {
        Thread tw = new Thread(new ReaderThread());
        tw.start();
    }

    private void handleUpdate(Response response) throws RemoteException {
        Flight[] flightList = Converter.getFlightsList((FlightDTO[]) response.getData());
        client.update(flightList);
    }

    private boolean isUpdate(Response response) {
        return response.getResponseType() == ResponseType.OBSERVER_UPDATE;
    }

    private class ReaderThread implements Runnable {
        public void run() {
            while (!finished) {
                try {
                    Response response = (Response) input.readObject();
                    System.out.println("Response received " + response);
                    if (isUpdate(response)) {
                        handleUpdate(response);
                        continue;
                    }
                    try {
                        qresponses.put((Response) response);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } catch (IOException | ClassNotFoundException e) {
                    System.out.println("Reading error " + e);
                }
            }
        }
    }
}
