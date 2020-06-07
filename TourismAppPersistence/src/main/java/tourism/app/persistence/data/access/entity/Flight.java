package tourism.app.persistence.data.access.entity;

import java.time.LocalDateTime;

public class Flight implements Entity<Integer> {

    private Integer id;
    private String destination;
    private String airport;
    private LocalDateTime flightDateTime;
    private Integer availableSpots;

    public Flight() {
    }

    public Flight(Integer id, String destination, String airport, LocalDateTime flightDateTime, Integer availableSpots) {
        this.id = id;
        this.destination = destination;
        this.airport = airport;
        this.flightDateTime = flightDateTime;
        this.availableSpots = availableSpots;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAvailableSpots() {
        return availableSpots;
    }

    public void setAvailableSpots(Integer availableSpots) {
        this.availableSpots = availableSpots;
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

    @Override
    public String toString() {
        return "Flight{" +
                "id=" + id +
                ", destination='" + destination + '\'' +
                ", airport='" + airport + '\'' +
                ", flightDateTime=" + flightDateTime +
                ", availableSpots=" + availableSpots +
                '}';
    }
}
