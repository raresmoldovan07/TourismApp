package tourism.app.network.protocol.request;

import tourism.app.network.dto.TicketDTO;

public class SaveTicketRequest implements Request {

    private TicketDTO ticketDTO;

    public SaveTicketRequest(TicketDTO ticketDTO) {
        this.ticketDTO = ticketDTO;
    }

    public TicketDTO getTicketDTO() {
        return ticketDTO;
    }
}
