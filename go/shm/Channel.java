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

    private final AtomicInteger inOperationCurrentlyRunningCounter;
    private final AtomicInteger outOperationCurrentlyRunningCounter;

    private static final int QUEUE_SIZE = 1000;

    public Channel(String name) {
        this.queue = new ArrayBlockingQueue<>(QUEUE_SIZE);
        this.queueIn = new ArrayBlockingQueue<>(QUEUE_SIZE);
        this.inObservers = new ArrayList<>();
        this.outObservers = new ArrayList<>();
        this.inOperationCurrentlyRunningCounter = new AtomicInteger(0);
        this.outOperationCurrentlyRunningCounter = new AtomicInteger(0);

        this.name = name;
        System.out.println("Channel " + this.name + " created");
    }
    
    public void out(T v) {
        try {
            System.out.println("Channel " + this.name + " start out(" + v + ")");

            // indiquer la presence d'un redacteur
            this.outOperationCurrentlyRunningCounter.incrementAndGet();
            
            // notifier les observers inverse car l'action inverse est possible
			this.notify(this.inObservers);

            // Attente d'un lecteur pour echange 1-1
            this.queueIn.take();

            // ajouter la donnee dans la file
            this.queue.put(v);

            // Enlever la presence du redacteur
            this.outOperationCurrentlyRunningCounter.decrementAndGet();
            System.out.println("Channel " + this.name + " finish out(" + v + ")");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    
    public T in() {
        try {
            System.out.println("Channel " + this.name + " start in()");
            
            // indiquer la presence d'un lecteur
            this.inOperationCurrentlyRunningCounter.incrementAndGet();

            // notifier les observers inverse car l'action inverse est possible
            this.notify(this.outObservers);

            // indiquer la presence d'un lecteur a la fonction out
            this.queueIn.put(1); // 1 au hasard, on n'utilise pas cette valeur

            // si la queue est vide bloquer jusqu'au prochain out
            T result = this.queue.take();

            // enlever la présence du lecteur
            this.inOperationCurrentlyRunningCounter.decrementAndGet();

            System.out.println("Channel " + this.name + " finish in()");
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
            if (this.outOperationCurrentlyRunningCounter.get() > 0){
                System.out.println("Channel " + this.name + " observe instant update (for in)");
                observer.update();
            }
            else{
                this.inObservers.add(observer);
            }
            
        } else if(dir == Direction.Out){
            if (this.inOperationCurrentlyRunningCounter.get() > 0){
                System.out.println("Channel " + this.name + " observe instant update (for out)");
                observer.update();
            }
            else{
                this.outObservers.add(observer);
            }
        }
        System.out.println("Channel " + this.name + " finish observe()");
    }

	public void notify(List<Observer> observers){
		while (!observers.isEmpty()){
            Observer observer = observers.getFirst();
            if (observer != null){
                observer.update();
            }
			observers.removeFirst();
		}
        System.out.println("Channel " + this.name + " finish notify()");
	}
        
}
