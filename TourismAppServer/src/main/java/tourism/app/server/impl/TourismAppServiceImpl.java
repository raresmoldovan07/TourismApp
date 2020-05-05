package tourism.app.server.impl;

import tourism.app.persistence.data.access.entity.Flight;
import tourism.app.persistence.data.access.entity.Ticket;
import tourism.app.persistence.data.access.entity.User;
import tourism.app.persistence.data.access.repository.FlightRepository;
import tourism.app.persistence.data.access.repository.TicketRepository;
import tourism.app.persistence.data.access.repository.UserRepository;
import tourism.app.services.Observer;
import tourism.app.services.TourismAppService;
import tourism.app.services.exception.ServiceException;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TourismAppServiceImpl implements TourismAppService {

    private static final int DEFAULT_THREADS_NUMBER = 5;

    private FlightRepository flightRepository;
    private TicketRepository ticketRepository;
    private UserRepository userRepository;

    private Map<Integer, Observer> loggedUsers;

    public TourismAppServiceImpl(FlightRepository flightRepository, TicketRepository ticketRepository, UserRepository userRepository) {
        this.flightRepository = flightRepository;
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
        loggedUsers = new ConcurrentHashMap<>();
    }

    @Override
    public synchronized User getUserByUsernameAndPassword(String username, String password, Observer client) throws ServiceException {
        User user = userRepository.getUserByUsernameAndPassword(username, password);
        if (user == null) {
            throw new ServiceException("User not found");
        }
        loggedUsers.put(user.getId(), client);
        return user;
    }

    @Override
    public synchronized Flight[] findAll() {
        List<Flight> flightList = (List<Flight>) flightRepository.findAll();
        Flight[] flights = new Flight[flightList.size()];
        for (int i = 0; i < flightList.size(); ++i) {
            flights[i] = flightList.get(i);
        }
        return flights;
    }

    @Override
    public synchronized void save(Ticket ticket) {
        ticketRepository.save(ticket);
        Flight flight = flightRepository.findOne(ticket.getFlightId());
        flight.setAvailableSpots(flight.getAvailableSpots() - ticket.getSpots());
        flightRepository.update(flight.getId(), flight);
        notifyLoggedUser();
    }

    private void notifyLoggedUser() {
        ExecutorService executorService = Executors.newFixedThreadPool(DEFAULT_THREADS_NUMBER);
        for (Observer user : loggedUsers.values()) {
            executorService.execute(() -> {
                try {
                    user.update(findAll());
                } catch (RemoteException e) {
                    System.out.println("[ERROR]:TourismAppServiceImpl - Error while updating client" + e.getMessage());
                }
            });
        }
        executorService.shutdown();
    }
}
