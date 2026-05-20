package go.sock;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class HostImpl extends UnicastRemoteObject implements Host {

    private final String ip;
    private final int port;

    public HostImpl(String ip, int port) throws RemoteException {
        this.ip = ip;
        this.port = port;
    }

    public String getIP() throws RemoteException {
        return this.ip;
    }

    public int getPort() throws RemoteException {
        return this.port;
    }
}
