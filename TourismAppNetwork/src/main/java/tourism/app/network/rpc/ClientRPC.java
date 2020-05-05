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

public class ClientRPC implements Runnable, Observer {

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
    public void update(Flight[] flights) {
        System.out.println("Sending update request");
        try {
            sendResponse(new UpdateFlights(Converter.getFlightDTOsList(flights)));
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
        output.writeObject(response);
        output.flush();
    }
}
