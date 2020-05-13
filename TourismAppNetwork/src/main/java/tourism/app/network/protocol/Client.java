package tourism.app.network.protocol;

import com.google.gson.Gson;
import tourism.app.network.dto.Converter;
import tourism.app.network.dto.FlightDTO;
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

import java.io.*;
import java.net.Socket;

public class Client implements Runnable, Observer {

    private TourismAppService tourismAppService;
    private Socket connection;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private volatile boolean connected;
    private Gson gson = new Gson();

    public Client(TourismAppService tourismAppService, Socket socket) {
        this.tourismAppService = tourismAppService;
        this.connection = socket;
        try {
            outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.flush();
            inputStream = new DataInputStream(connection.getInputStream());
            connected = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (connected) {
            try {
                int inputSize = inputStream.readInt();
                byte[] inputByte = new byte[inputSize];
                inputStream.read(inputByte, 0, inputSize);
                String requestJSON = new String(inputByte);
                Request request = gson.fromJson(requestJSON, Request.class);
                Response response = null;
                switch (request.getType()) {
                    case "LoginRequest":
                        response = handleRequest(gson.fromJson(requestJSON, LoginRequest.class));
                        break;
                    case "FindAllFlightsRequest":
                        response = handleRequest(gson.fromJson(requestJSON, FindAllFlightsRequest.class));
                        break;
                    case "SaveTicketRequest":
                        response = handleRequest(gson.fromJson(requestJSON, SaveTicketRequest.class));
                        break;
                    default:
                        continue;
                }
                if (response != null) {
                    sendResponse(response);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            inputStream.close();
            outputStream.close();
            connection.close();
        } catch (IOException e) {
            System.out.println("Error " + e);
        }
    }

    @Override
    public void update(Flight[] flights) {
        System.out.println("Sending update request");
        try {
            sendResponse(new UpdateFlightsResponse(Converter.getFlightDTOsList(flights)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Response handleRequest(Request request) {
        if (request instanceof LoginRequest) {
            return handleLoginRequest((LoginRequest) request);
        } else if (request instanceof FindAllFlightsRequest) {
            return handleFlightsRequest((FindAllFlightsRequest) request);
        } else if (request instanceof SaveTicketRequest) {
            return handleSaveTicketRequest((SaveTicketRequest) request);
        }
        return null;
    }

    private Response handleSaveTicketRequest(SaveTicketRequest request) {
        System.out.println("Handling save ticket request");
        try {
            TicketDTO ticketDTO = request.getTicketDTO();
            Ticket ticket = Converter.getTicket(ticketDTO);
            tourismAppService.save(ticket);
            return new OkResponse();
        } catch (ServiceException e) {
            connected = false;
            System.out.println(String.format("Error handling save ticket request %s", e));
            return new ErrorResponse(e.getMessage());
        }
    }

    private Response handleFlightsRequest(FindAllFlightsRequest request) {
        System.out.println("Handling flights request");
        try {
            Flight[] flights = tourismAppService.findAll();
            FlightDTO[] flightDTOS = Converter.getFlightDTOsList(flights);
            return new FindAllFlightsResponse(flightDTOS);
        } catch (ServiceException e) {
            connected = false;
            System.out.println(String.format("Error handling flights request %s", e));
            return new ErrorResponse(e.getMessage());
        }
    }

    private Response handleLoginRequest(LoginRequest request) {
        System.out.println("Handling login request");
        UserDTO userDTO = request.getUserDTO();
        try {
            User user = tourismAppService.getUserByUsernameAndPassword(userDTO.getUsername(), userDTO.getPassword(), this);
            return new LoggedResponse(Converter.getUserDTO(user));
        } catch (ServiceException e) {
            connected = false;
            System.out.println(String.format("Error handling login request %s", e));
            return new ErrorResponse(e.getMessage());
        }
    }

    private void sendResponse(Response response) throws IOException {
        System.out.println("Sending response " + response);
        String responseJSON = gson.toJson(response);
        int responseSize = responseJSON.getBytes().length;
        outputStream.write(responseJSON.getBytes(), 0, responseSize);
        outputStream.flush();
    }
}
