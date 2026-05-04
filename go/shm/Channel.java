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

    private static final int QUEUE_SIZE = 1000;

    public Channel(String name) {
        this.queue = new ArrayBlockingQueue<>(QUEUE_SIZE);
        this.queueIn = new ArrayBlockingQueue<>(QUEUE_SIZE);
        this.name = name;
    }
    
    public void out(T v) {
        try {
            // si aucun lecteur, bloquer jusqu'au prochain in
            this.queueIn.take();
            // ajouter la donner dans la file
            this.queue.put(v);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    
    public T in() {
        try {
            // indiquer la presence d'un lecteur
            this.queueIn.put(1);
            // si la queue est vide bloquer jusqu'au prochain out
            return this.queue.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public String getName() {
        return this.name;
    }

    public void observe(Direction dir, Observer observer) {
        // TODO
    }
        
}
