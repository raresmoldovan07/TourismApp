package tourism.app.network.util.impl;

import tourism.app.network.rpc.ClientRPC;
import tourism.app.network.util.AbstractConcurrentServer;
import tourism.app.services.TourismAppService;

import java.net.Socket;

public class RPCServer extends AbstractConcurrentServer {

    private TourismAppService tourismAppService;

    public RPCServer(int PORT, TourismAppService tourismAppService) {
        super(PORT);
        this.tourismAppService = tourismAppService;
    }

    @Override
    protected Thread createThread(Socket client) {
        ClientRPC clientRPC = new ClientRPC(tourismAppService, client);
        return new Thread(clientRPC);
    }
}
