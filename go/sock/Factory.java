package go.sock;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Map;
import java.util.Set;

import go.Direction;


public class Factory implements go.Factory {
    
    /** Création ou accès à un canal existant.
     * Lors de l’appel de Factory::newChannel, on interroge le service de nommage et on
     * crée un ChannelMaster si le canal n’existe pas ; s’il existe, on crée un ChannelSlave.
     * Note : pouvoir envoyer un canal dans un canal demande d’adapter la sérialisation.
    */


    public <T> go.Channel<T> newChannel(String name){
        try{
            Registry dns = LocateRegistry.getRegistry(Naming.PORT);
            
            // creation du master si necessaire
            try {
                Host address = (Host) dns.lookup(ChannelMaster.HOSTNAME);
            } catch (NotBoundException e) {
                Thread threadMaster = new ChannelMaster();
                threadMaster.start();
                try { Thread.sleep(200); } catch (InterruptedException e2) { }
            }
        }
        catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        return new ChannelSlave<>(name);
    }
    
    /** Hors projet */
    public go.Selector newSelector(Map<go.Channel, Direction> channels){
        return null;
    }

    /** Hors projet */
    public go.Selector newSelector(Set<go.Channel> channels, Direction direction){
        return null;
    }
}
