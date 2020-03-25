package ubb.tourism.business.service;

import ubb.tourism.data.access.entity.Flight;
import ubb.tourism.data.access.repository.FlightRepository;

public class FlightService {

    private FlightRepository flightRepository;

    public FlightService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    public Iterable<Flight> findAll() {
        return flightRepository.findAll();
    }
}
