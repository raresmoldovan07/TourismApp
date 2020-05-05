package tourism.app.services;

import tourism.app.persistence.data.access.entity.Flight;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Observer extends Remote {

    void update(Flight[] flights) throws RemoteException;
}