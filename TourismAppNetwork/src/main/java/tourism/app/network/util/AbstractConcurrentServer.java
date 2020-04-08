package tourism.app.network.util;

import java.net.Socket;

public abstract class AbstractConcurrentServer extends AbstractServer {

    public AbstractConcurrentServer(int PORT) {
        super(PORT);
    }

    @Override
    protected void processRequest(Socket client) {
        createThread(client).start();
    }

    protected abstract Thread createThread(Socket client);
}
