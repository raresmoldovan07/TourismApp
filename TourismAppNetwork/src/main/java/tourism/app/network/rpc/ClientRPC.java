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
        if (request.getRequestType() == RequestType.LOGIN) {
            return handleLoginRequest(request);
        } else if (request.getRequestType() == RequestType.GET_ALL_FLIGHTS) {
            return handleFlightsRequest(request);
        } else if (request.getRequestType() == RequestType.SAVE_TICKET) {
            return handleSaveTicketRequest(request);
        } else if (request.getRequestType() == RequestType.UPDATE_FLIGHT) {
            return handleUpdateFlightRequest(request);
        }
        return null;
    }

    private Response handleUpdateFlightRequest(Request request) {
        System.out.println("Handling update flight request");
        try {
            FlightDTO flightDTO = (FlightDTO) request.getData();
            Flight flight = Converter.getFlight(flightDTO);
            tourismAppService.update(flight.getId(), flight);
            return new Response.Builder().type(ResponseType.UPDATED_TICKET).build();
        } catch (ServiceException e) {
            connected = false;
            System.out.println(String.format("Error handling update ticket request %s", e));
        }
        return new Response.Builder().type(ResponseType.ERROR).build();
    }

    private Response handleSaveTicketRequest(Request request) {
        System.out.println("Handling save ticket request");
        try {
            TicketDTO ticketDTO = (TicketDTO) request.getData();
            Ticket ticket = Converter.getTicket(ticketDTO);
            tourismAppService.save(ticket);
            return new Response.Builder().type(ResponseType.SAVED_TICKET).build();
        } catch (ServiceException e) {
            connected = false;
            System.out.println(String.format("Error handling save ticket request %s", e));
        }
        return new Response.Builder().type(ResponseType.ERROR).build();
    }

    private Response handleFlightsRequest(Request request) {
        System.out.println("Handling flights request");
        try {
            Flight[] flights = tourismAppService.findAll();
            FlightDTO[] flightDTOS = Converter.getFlightDTOsList(flights);
            return new Response.Builder().type(ResponseType.GET_ALL_FLIGHTS).data(flightDTOS).build();
        } catch (ServiceException e) {
            connected = false;
            System.out.println(String.format("Error handling flights request %s", e));
        }
        return new Response.Builder().type(ResponseType.ERROR).build();
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
