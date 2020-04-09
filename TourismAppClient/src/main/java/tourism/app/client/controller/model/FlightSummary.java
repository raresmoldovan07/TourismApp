package tourism.app.client.controller.model;

import java.time.LocalTime;

public class FlightSummary {

    private String airport;
    private LocalTime schedule;
    private Integer availableSpots;

    public FlightSummary(String airport, LocalTime schedule, Integer availableSpots) {
        this.airport = airport;
        this.schedule = schedule;
        this.availableSpots = availableSpots;
    }

    public String getAirport() {
        return airport;
    }

    public void setAirport(String airport) {
        this.airport = airport;
    }

    public LocalTime getSchedule() {
        return schedule;
    }

    public void setSchedule(LocalTime schedule) {
        this.schedule = schedule;
    }

    public Integer getAvailableSpots() {
        return availableSpots;
    }

    public void setAvailableSpots(Integer availableSpots) {
        this.availableSpots = availableSpots;
    }
}
