package go.shm;

import go.Direction;
import go.Observer;
import go.Channel;

import java.util.ArrayList;
import java.util.List;
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
        synchronized (this){
            chosenOne = null;
            List<Observer> observerList = new ArrayList<>();

            System.out.println("Selector.select()");
            for (var entry : this.chanelsMap.entrySet()){
                Channel channel = entry.getKey();
                Direction direction = entry.getValue();

                Observer observer = () -> {
                    synchronized (Selector.this){
                        System.out.println("Selector.select() -> observer running update");
                        if(Selector.this.chosenOne == null){
                            System.out.println("Selector.select() -> observer changing chosenOne for " + channel.getName());
                            Selector.this.chosenOne = channel;
                            Selector.this.notify();
                        }
                    }
                };
                channel.observe(direction, observer);
                observerList.add(observer);
            }

            try {
                if(chosenOne == null){
                    System.out.println("Selector.select() -> wait");
                    this.wait();
                    System.out.println("Selector.select() -> finished wait");
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            while(!observerList.isEmpty()){
                observerList.set(0, null);
                observerList.removeFirst();
            }

            System.out.println("Selector.select() -> choix du channel " + chosenOne.getName());

            return chosenOne;
        }
    }
}
