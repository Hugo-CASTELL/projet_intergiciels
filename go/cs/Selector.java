package go.cs;

import go.Direction;
import go.Channel;
import java.util.Map;
import java.util.Set;

public class Selector extends go.shm.Selector {

    public Selector(Map<Channel, Direction> channels) {
        super(channels);
    }

}
