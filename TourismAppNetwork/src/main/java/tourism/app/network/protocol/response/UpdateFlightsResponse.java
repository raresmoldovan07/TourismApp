package tourism.app.network.protocol.response;

import tourism.app.network.dto.FlightDTO;

public class UpdateFlightsResponse extends UpdateResponse {

    public FlightDTO[] flightDTOs;

    public UpdateFlightsResponse(FlightDTO[] flightDTOs) {
        super("UpdateFlightsResponse");
        this.flightDTOs = flightDTOs;
    }

    public FlightDTO[] getFlightDTOs() {
        return flightDTOs;
    }

    public void setFlightDTOs(FlightDTO[] flightDTOs) {
        this.flightDTOs = flightDTOs;
    }
}
