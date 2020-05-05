package tourism.app.network.protocol.response;

import tourism.app.network.dto.FlightDTO;

public class FindAllFlightsResponse implements Response {

    private FlightDTO[] flightDTOs;

    public FindAllFlightsResponse(FlightDTO[] flightDTOs) {
        this.flightDTOs = flightDTOs;
    }

    public FlightDTO[] getFlightDTOs() {
        return flightDTOs;
    }

    public void setFlightDTOs(FlightDTO[] flightDTOs) {
        this.flightDTOs = flightDTOs;
    }
}
