package go.shm;

import java.util.ArrayList;

import go.Direction;
import go.Observer;

public class Channel<T> implements go.Channel<T> {

    private List<T> data;
    private String name;

    public Channel(String name) {
        this.data = new ArrayList<>();
        this.name = name;
    }
    
    public void out(T v) {
        this.data.add(v);
    }
    
    public T in() {
        return this.data.remove(this.data.size()-1);
    }

    public String getName() {
        return this.name;
    }

    public void observe(Direction dir, Observer observer) {
        // TODO
    }
        
}
