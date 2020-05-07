package tourism.app.network.protocol.response;

import tourism.app.network.dto.FlightDTO;

public class FindAllFlightsResponse extends Response {

    private FlightDTO[] flightDTOs;

    public FindAllFlightsResponse(FlightDTO[] flightDTOs) {
        super("FindAllFlightsResponse");
        this.flightDTOs = flightDTOs;
    }

    public FlightDTO[] getFlightDTOs() {
        return flightDTOs;
    }

    public void setFlightDTOs(FlightDTO[] flightDTOs) {
        this.flightDTOs = flightDTOs;
    }
}
