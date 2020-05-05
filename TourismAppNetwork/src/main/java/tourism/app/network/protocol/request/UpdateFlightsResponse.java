package tourism.app.network.protocol.request;

import tourism.app.network.dto.FlightDTO;

public class UpdateFlightsResponse implements UpdateResponse {

    public FlightDTO[] flightDTOs;

    public UpdateFlightsResponse(FlightDTO[] flightDTOs) {
        this.flightDTOs = flightDTOs;
    }

    public FlightDTO[] getFlightDTOs() {
        return flightDTOs;
    }

    public void setFlightDTOs(FlightDTO[] flightDTOs) {
        this.flightDTOs = flightDTOs;
    }
}
