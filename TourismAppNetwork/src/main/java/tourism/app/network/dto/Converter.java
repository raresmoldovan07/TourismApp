package tourism.app.network.dto;

import tourism.app.persistence.data.access.entity.Flight;
import tourism.app.persistence.data.access.entity.User;

public class Converter {

    public static UserDTO getUserDTO(User user) {
        return new UserDTO(user.getId(), user.getUsername(), user.getPassword(), user.getName());
    }

    public static User getUser(UserDTO userDTO) {
        return new User(userDTO.getId(), userDTO.getUsername(), userDTO.getPassword(), userDTO.getName());
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
