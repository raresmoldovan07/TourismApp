package ubb.tourism.business.service.impl;

import ubb.tourism.business.service.Observable;
import ubb.tourism.business.service.TicketService;
import tourism.app.persistence.data.access.entity.Ticket;
import tourism.app.persistence.data.access.repository.TicketRepository;

public class TicketServiceImpl extends Observable implements TicketService {

    private TicketRepository ticketRepository;

    public TicketServiceImpl(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public void save(Ticket ticket) {
        ticketRepository.save(ticket);
    }
}
