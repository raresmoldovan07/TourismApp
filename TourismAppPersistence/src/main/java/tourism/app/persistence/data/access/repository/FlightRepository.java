package tourism.app.persistence.data.access.repository;

import tourism.app.model.entity.Flight;

import java.time.LocalDateTime;

public interface FlightRepository extends CrudRepository<Integer, Flight> {

    void getFlightByDestAndDate(String destination, LocalDateTime flightDateTime);
}
