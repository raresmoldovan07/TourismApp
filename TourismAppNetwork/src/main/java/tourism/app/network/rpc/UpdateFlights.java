package tourism.app.network.rpc;

import tourism.app.network.dto.FlightDTO;

public class UpdateFlights  implements UpdateResponse {

    public FlightDTO[] flightDTOs;

    public UpdateFlights(FlightDTO[] flightDTOs) {
        this.flightDTOs = flightDTOs;
    }

    public FlightDTO[] getFlightDTOs() {
        return flightDTOs;
    }

    public void setFlightDTOs(FlightDTO[] flightDTOs) {
        this.flightDTOs = flightDTOs;
    }
}
