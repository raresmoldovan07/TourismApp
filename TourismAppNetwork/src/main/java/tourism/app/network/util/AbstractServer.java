package tourism.app.network.util;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public abstract class AbstractServer {

    private final int PORT;

    private ServerSocket serverSocket = null;

    public AbstractServer(int PORT) {
        this.PORT = PORT;
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(PORT);
            while (true) {
                System.out.println("Waiting for clients");
                Socket client = serverSocket.accept();
                System.out.println("A client has connected to the server");
                processRequest(client);
            }
        } catch (IOException e) {
            throw new ServerException("Failed to start server", e);
        }
    }

    public void stop() throws ServerException {
        try {
            serverSocket.close();
        } catch (IOException e) {
            throw new ServerException("Failed to close th server", e);
        }
    }

    protected abstract void processRequest(Socket client);
}
