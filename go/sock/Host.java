package go.sock;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Host extends Remote {

    String getIP() throws RemoteException;

    int getPort() throws RemoteException;

}
