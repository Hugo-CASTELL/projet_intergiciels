package go.shm;

import go.Direction;
import go.Observer;
import go.Channel;
import java.util.Map;
import java.util.Set;

public class Selector implements go.Selector {

    private final Map<Channel, Direction> chanelsMap;

    public Selector(Map<Channel, Direction> channels) {
        this.chanelsMap = channels;
    }

    public Channel select() {
        // TODO

        // je peut faire un in si un channel est bloqué en out

        // je peut faire un out si un channel est bloqué en in


        for (var entry : this.chanelsMap.entrySet()){
            Channel channel = entry.getKey();
            Direction direction = entry.getValue();
            
            Observer observer = new Observer();
            channel.observe(Direction.inverse(direction), observer);
        }


        return null;
    }

}
