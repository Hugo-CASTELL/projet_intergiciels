package go.sock;

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
        // TODO
        return null;
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
