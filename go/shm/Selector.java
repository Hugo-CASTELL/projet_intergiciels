package go.shm;

import go.Direction;
import go.Observer;
import go.Channel;
import java.util.Map;
import java.util.Set;

public class Selector implements go.Selector {

    private final Map<Channel, Direction> chanelsMap;
    private volatile Channel chosenOne;

    public Selector(Map<Channel, Direction> channels) {
        this.chanelsMap = channels;
        this.chosenOne = null;
    }

    public Channel select() {
        for (var entry : this.chanelsMap.entrySet()){
            Channel channel = entry.getKey();
            Direction direction = entry.getValue();
            
            Observer observer = new Observer() {
                @Override
                public void update() {
                    chosenOne = channel;
                }
            };
            channel.observe(direction, observer);
        }

        while (chosenOne == null) {
            //rien
        }

        Channel alexis = chosenOne;
        chosenOne = null;

        return alexis;
    }

}
