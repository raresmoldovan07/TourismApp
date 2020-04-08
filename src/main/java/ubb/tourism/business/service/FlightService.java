package ubb.tourism.business.service;

import tourism.app.persistence.data.access.entity.Flight;

public interface FlightService {

    void update(Integer id, Flight flight);

    Iterable<Flight> findAll();
}
