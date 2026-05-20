package go.sock;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ChannelSlave{

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
    }

    // TODO faire des méthodes de in et out

}