package ubb.tourism.data.access.repository;

import ubb.tourism.data.access.entity.Flight;

import java.time.LocalDateTime;

public interface FlightRepository extends CrudRepository<Integer, Flight> {

    void getFlightByDestAndDate(String destination, LocalDateTime flightDateTime);
}
