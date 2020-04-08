package tourism.app.network.rpc;

import tourism.app.persistence.data.access.entity.User;
import tourism.app.services.Observer;
import tourism.app.services.TourismAppService;
import tourism.app.services.exception.ServiceException;

import java.net.Socket;

public class ClientRPC implements Runnable, Observer {

    private TourismAppService tourismAppService;
    private Socket socket;

    public ClientRPC(TourismAppService tourismAppService, Socket socket) {
        this.tourismAppService = tourismAppService;
        this.socket = socket;
    }

    @Override
    public void run() {

    }

    @Override
    public void notificationReceived() {

    }

    @Override
    public void userLoggedIn(User user) throws ServiceException {

    }

    @Override
    public void userLoggedOut(User user) throws ServiceException {

    }
}
