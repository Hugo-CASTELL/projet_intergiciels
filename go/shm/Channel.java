package go.shm;

import java.util.ArrayList;
import java.util.List;

import go.Direction;
import go.Observer;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue; 

public class Channel<T> implements go.Channel<T> {

    private String name;
    private final BlockingQueue<T> queue;
    private final BlockingQueue<Integer> queueIn;

    public Channel(String name) {
        this.queue = new ArrayBlockingQueue<T>();
        this.queueIn = new ArrayBlockingQueue<T>();
        this.name = name;
    }
    
    public void out(T v) {
        // si aucun lecteur, bloquer jusqu'au prochain in
        this.queueIn.take();

        // ajouter la donner dans la file
        this.queue.put(v);
    }
    
    public T in() {
        // indiquer la presence d'un lecteur
        this.queueIn.put(1);
        
        // si la queue est vide bloquer jusqu'au prochain out
        return this.queue.take();
    }

    public String getName() {
        return this.name;
    }

    public void observe(Direction dir, Observer observer) {
        // TODO
    }
        
}
