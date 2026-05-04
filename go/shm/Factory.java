package go.shm;

import go.Direction;
import java.util.Set;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Factory implements go.Factory {

    /** Création ou accès à un canal existant. */
    public <T> go.Channel<T> newChannel(String name) {
        return new go.shm.Channel<>(name);
    }
    
    /** Spécifie quels sont les canaux écoutés et la direction pour chacun. */
    public go.Selector newSelector(Map<go.Channel, Direction> channels) {
        return new Selector(channels); 
    }

    /** Spécifie quels sont les canaux écoutés et la même direction pour tous. */
    public go.Selector newSelector(Set<go.Channel> channels, Direction direction) {
        Map<go.Channel, Direction> channelsMap = new Map<go.Channel, Direction>();

        for (Channel channel : channels){
            channelsMap.add(channel, direction);
        }
        return this.newSelector(channelsMap);
    }

}

