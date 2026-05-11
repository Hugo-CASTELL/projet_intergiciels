package go.cs;

import go.Direction;
import go.shm.Selector;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Set;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Factory implements go.Factory {

    /** Création ou accès à un canal existant.
     * Côté serveur, le canal est créé au premier appel avec un nom donné ;
     * les appels suivants avec le même nom donneront accès au même canal.
     */
    public <T> go.Channel<T> newChannel(String name) {
        Registry dns = LocateRegistry.getRegistry(ServerImpl.PORT);
        
        // récupération de l'ancien channel
        go.Channel channel = (go.Channel) dns.lookup(name);
        if (channel != null){
            return channel;
        }

        // creation d'un nouveau channel
        channel = new Channel(name);
        dns.bind(name, channel);
        return channel;
    }
    
    /** Spécifie quels sont les canaux écoutés et la direction pour chacun. */
    public go.Selector newSelector(Map<go.Channel, Direction> channels) {
        return new Selector(channels);
    }

    /** Spécifie quels sont les canaux écoutés et la même direction pour tous. */
    public go.Selector newSelector(Set<go.Channel> channels, Direction direction) {
        return newSelector(channels
                           .stream() 
                           .collect(Collectors.toMap(Function.identity(), e -> direction)));
    }

}

