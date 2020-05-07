package tourism.app.network.protocol.request;

import tourism.app.network.dto.TicketDTO;

public class SaveTicketRequest extends Request {

    private TicketDTO ticketDTO;

    public SaveTicketRequest(TicketDTO ticketDTO) {
        super("SaveTicketRequest");
        this.ticketDTO = ticketDTO;
    }

    public TicketDTO getTicketDTO() {
        return ticketDTO;
    }
}
