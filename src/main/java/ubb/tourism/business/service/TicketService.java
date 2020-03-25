package ubb.tourism.business.service;

import ubb.tourism.data.access.repository.TicketRepository;

public class TicketService {

    private TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }
}
