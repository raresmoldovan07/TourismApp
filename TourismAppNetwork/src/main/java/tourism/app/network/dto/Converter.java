package tourism.app.network.dto;

import tourism.app.model.entity.Flight;
import tourism.app.model.entity.Ticket;
import tourism.app.model.entity.User;

public class Converter {

    public static UserDTO getUserDTO(User user) {
        return new UserDTO(user.getId(), user.getUsername(), user.getPassword(), user.getName());
    }

    public static User getUser(UserDTO userDTO) {
        return new User(userDTO.getId(), userDTO.getUsername(), userDTO.getPassword(), userDTO.getName());
    }

    public static TicketDTO getTicketDTO(Ticket ticket){
        return new TicketDTO(ticket.getId(), ticket.getFlightId(), ticket.getSpots(), ticket.getClientName(), ticket.getClientAddress(),
                ticket.getTourists());
    }

    public static Ticket getTicket(TicketDTO ticket){
        return new Ticket(ticket.getId(), ticket.getFlightId(), ticket.getSpots(), ticket.getClientName(), ticket.getClientAddress(),
                ticket.getTourists());
    }

    public static FlightDTO getFlightDTO(Flight flight) {
        return new FlightDTO(flight.getId(), flight.getDestination(), flight.getAirport(), flight.getFlightDateTime(), flight.getAvailableSpots());
    }

    public static Flight getFlight(FlightDTO flight) {
        return new Flight(flight.getId(), flight.getDestination(), flight.getAirport(), flight.getFlightDateTime(), flight.getAvailableSpots());
    }

    public static Flight[] getFlightsList(FlightDTO[] flightDTOS) {
        Flight[] flights = new Flight[flightDTOS.length];
        for (int i = 0; i < flightDTOS.length; ++i) {
            flights[i] = new Flight(flightDTOS[i].getId(), flightDTOS[i].getDestination(), flightDTOS[i].getAirport(),
                    flightDTOS[i].getFlightDateTime(), flightDTOS[i].getAvailableSpots());
        }
        return flights;
    }

    public static FlightDTO[] getFlightDTOsList(Flight[] flights) {
        FlightDTO[] flightDTOS = new FlightDTO[flights.length];
        for (int i = 0; i < flights.length; ++i) {
            flightDTOS[i] = new FlightDTO(flights[i].getId(), flights[i].getDestination(), flights[i].getAirport(),
                    flights[i].getFlightDateTime(), flights[i].getAvailableSpots());
        }
        return flightDTOS;
    }
}
