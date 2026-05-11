package go.shm;

import java.util.ArrayList;
import java.util.List;

import go.Direction;
import go.Observer;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger; 

public class Channel<T> implements go.Channel<T> {

    private final String name;
    private final BlockingQueue<T> queue;
    private final BlockingQueue<Integer> queueIn;
    private final List<Observer> inObservers;
    private final List<Observer> outObservers;
    private final AtomicInteger inCounter;
    private final AtomicInteger outCounter;

    private static final int QUEUE_SIZE = 1000;

    public Channel(String name) {
        this.queue = new ArrayBlockingQueue<>(QUEUE_SIZE);
        this.queueIn = new ArrayBlockingQueue<>(QUEUE_SIZE);
        this.inObservers = new ArrayList<>(QUEUE_SIZE);
        this.outObservers = new ArrayList<>(QUEUE_SIZE);
        this.inCounter = new AtomicInteger(0);
        this.outCounter = new AtomicInteger(0);

        this.name = name;
    }
    
    public void out(T v) {
        try {
            // indiquer la presence d'un redacteur
            this.outCounter.incrementAndGet();
            
            // notifier les observers inverse car l'action inverse est possible
			this.notify(this.inObservers);

            // Attente d'un lecteur pour echange 1-1
            this.queueIn.take();

            // ajouter la donnee dans la file
            this.queue.put(v);
            this.outCounter.decrementAndGet();

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    
    public T in() {
        try {
            // indiquer la presence d'un lecteur
            this.inCounter.incrementAndGet();

            // notifier les observers inverse car l'action inverse est possible
            this.notify(this.outObservers);

            // indiquer la presence d'un lecteur a la fonction out
            this.queueIn.put(1);

            // si la queue est vide bloquer jusqu'au prochain out
            T result = this.queue.take();

            // envoyer le resultat
            this.inCounter.decrementAndGet();
            return result;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public String getName() {
        return this.name;
    }

    public void observe(Direction dir, Observer observer) {
        if(dir == Direction.In){
            if (this.outCounter.get() > 0){
                observer.update();
            }
            else{
                this.inObservers.add(observer);
            }
            
        } else if(dir == Direction.Out){
            if (this.inCounter.get() > 0){
                observer.update();
            }
            else{
                this.outObservers.add(observer);
            }
        }
    }

	private void notify(List<Observer> observers){
		while (!observers.isEmpty()){
			observers.getFirst().update();
			observers.removeFirst();
		}
	}
        
}
