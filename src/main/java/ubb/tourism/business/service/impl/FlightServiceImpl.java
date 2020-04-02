package ubb.tourism.business.service.impl;

import ubb.tourism.business.service.FlightService;
import ubb.tourism.business.service.Observable;
import ubb.tourism.data.access.entity.Flight;
import ubb.tourism.data.access.repository.FlightRepository;

public class FlightServiceImpl extends Observable implements FlightService {

    private FlightRepository flightRepository;

    public FlightServiceImpl(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    public void update(Integer id, Flight flight) {
        flightRepository.update(id, flight);
        notifyAllObservers();
    }

    public Iterable<Flight> findAll() {
        return flightRepository.findAll();
    }
}
