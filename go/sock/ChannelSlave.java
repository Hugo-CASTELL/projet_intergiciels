package go.sock;

import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ChannelSlave{

    private Socket socket = null;

    public ChannelSlave() {
        // Cherche à s'enregistrer dans le DNS
        Host address = null;
        try {
            Registry dns = LocateRegistry.getRegistry(Naming.PORT);
            address = (Host) dns.lookup(ChannelMaster.HOSTNAME);
        } catch (RemoteException | NotBoundException e) {
            throw new RuntimeException(e);
        }

        // Se connecter au serveur
        this.socket = new Socket(address.getIP(), address.getPort());
        
        
    }

    // TODO faire des méthodes de in et out

    public void out(T v) {
        try {
            this.socket.out(v);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    
    public T in() {
        try {
            return this.socket.in();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}