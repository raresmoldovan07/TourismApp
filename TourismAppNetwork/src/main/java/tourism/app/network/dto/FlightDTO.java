package tourism.app.network.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public class FlightDTO implements Serializable {

    private Integer id;
    private String destination;
    private String airport;
    private LocalDateTime flightDateTime;
    private Integer availableSpots;

    public FlightDTO(Integer id, String destination, String airport, LocalDateTime flightDateTime, Integer availableSpots) {
        this.id = id;
        this.destination = destination;
        this.airport = airport;
        this.flightDateTime = flightDateTime;
        this.availableSpots = availableSpots;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getAirport() {
        return airport;
    }

    public void setAirport(String airport) {
        this.airport = airport;
    }

    public LocalDateTime getFlightDateTime() {
        return flightDateTime;
    }

    public void setFlightDateTime(LocalDateTime flightDateTime) {
        this.flightDateTime = flightDateTime;
    }

    public Integer getAvailableSpots() {
        return availableSpots;
    }

    public void setAvailableSpots(Integer availableSpots) {
        this.availableSpots = availableSpots;
    }
}
