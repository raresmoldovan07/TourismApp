package tourism.app.network.protocol;

import com.google.gson.Gson;
import tourism.app.network.dto.Converter;
import tourism.app.network.dto.TicketDTO;
import tourism.app.network.dto.UserDTO;
import tourism.app.network.protocol.request.FindAllFlightsRequest;
import tourism.app.network.protocol.request.LoginRequest;
import tourism.app.network.protocol.request.Request;
import tourism.app.network.protocol.request.SaveTicketRequest;
import tourism.app.network.protocol.response.*;
import tourism.app.persistence.data.access.entity.Flight;
import tourism.app.persistence.data.access.entity.Ticket;
import tourism.app.persistence.data.access.entity.User;
import tourism.app.services.Observer;
import tourism.app.services.TourismAppService;
import tourism.app.services.exception.ServiceException;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ProxyServer implements TourismAppService {

    private String host;
    private int port;

    private Observer client;

    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private Socket connection;

    private BlockingQueue<Response> qresponses;
    private volatile boolean finished;
    private Gson gson = new Gson();

    public ProxyServer(String host, int port) {
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
            inputStream.close();
            outputStream.close();
            connection.close();
            client = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendRequest(Request request) throws ServiceException {
        try {
            String requestJSON = gson.toJson(request);
            outputStream.writeInt(requestJSON.getBytes().length);
            outputStream.flush();
            outputStream.write(requestJSON.getBytes(), 0, requestJSON.getBytes().length);
            outputStream.flush();
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
            outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.flush();
            inputStream = new DataInputStream(connection.getInputStream());
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

    private void handleUpdateFlightsResponse(UpdateFlightsResponse response) {
        Flight[] flightList = Converter.getFlightsList(response.getFlightDTOs());
        client.update(flightList);
    }

    private class ReaderThread implements Runnable {
        public void run() {
            while (!finished) {
                try {
                    int inputSize = 10000;
                    byte[] inputByte = new byte[inputSize];
                    inputStream.read(inputByte, 0, inputSize);
                    inputByte = shrink(inputByte);
                    String responseJSON = new String(inputByte);
                    Response response = gson.fromJson(responseJSON, Response.class);
                    System.out.println("Response received " + response);
                    switch (response.getType()) {
                        case "LoggedResponse":
                            response = gson.fromJson(responseJSON, LoggedResponse.class);
                            break;
                        case "FindAllFlightsResponse":
                            response = gson.fromJson(responseJSON, FindAllFlightsResponse.class);
                            break;
                        case "OkResponse":
                            response = gson.fromJson(responseJSON, OkResponse.class);
                            break;
                        case "ErrorResponse":
                            response = gson.fromJson(responseJSON, ErrorResponse.class);
                            break;
                        case "UpdateFlightsResponse":
                            response = gson.fromJson(responseJSON, UpdateFlightsResponse.class);
                            break;
                        default:
                            continue;
                    }
                    if (response instanceof UpdateResponse) {
                        handleUpdateFlightsResponse((UpdateFlightsResponse) response);
                        continue;
                    }
                    try {
                        qresponses.put(response);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    System.out.println("Reading error " + e);
                }
            }
        }
    }

    private byte[] shrink(byte[] array) {
        int i = 0;
        while(array[i++] != '\0');
        i -= 1;
        byte[] result = new byte[i];
        System.arraycopy(array, 0, result, 0, i);
        return result;
    }
}
