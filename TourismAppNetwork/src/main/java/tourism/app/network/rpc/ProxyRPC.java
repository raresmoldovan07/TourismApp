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
        sendRequest(new LoginRequest(userDTO));
        Response response = readResponse();
        if (response instanceof LoggedResponse) {
            this.client = client;
            return Converter.getUser(((LoggedResponse) response).getUserDTO());
        }
        if (response instanceof ErrorResponse) {
            closeConnection();
            throw new ServiceException(((ErrorResponse) response).getMessage());
        }
        return null;
    }

    @Override
    public Flight[] findAll() {
        sendRequest(new FindAllFlightsRequest());
        Response response = readResponse();
        if (response instanceof FindAllFlightsResponse) {
            return Converter.getFlightsList(((FindAllFlightsResponse) response).getFlightDTOs());
        }
        if (response instanceof ErrorResponse) {
            closeConnection();
            throw new ServiceException(((ErrorResponse) response).getMessage());
        }
        return null;
    }

    @Override
    public void save(Ticket ticket) {
        TicketDTO ticketDTO = Converter.getTicketDTO(ticket);
        sendRequest(new SaveTicketRequest(ticketDTO));
        Response response = readResponse();
        if (response instanceof ErrorResponse) {
            closeConnection();
            throw new ServiceException(((ErrorResponse) response).getMessage());
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

    private void handleUpdateFlightsResponse(UpdateFlights response) {
        Flight[] flightList = Converter.getFlightsList(response.getFlightDTOs());
        client.update(flightList);
    }

    private class ReaderThread implements Runnable {
        public void run() {
            while (!finished) {
                try {
                    Response response = (Response) input.readObject();
                    System.out.println("Response received " + response);
                    if (response instanceof UpdateResponse) {
                        if(response instanceof UpdateFlights) {
                            handleUpdateFlightsResponse((UpdateFlights)response);
                        }
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
