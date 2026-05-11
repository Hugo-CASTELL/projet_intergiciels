package go.cs;

import go.Direction;
import go.Observer;

import java.rmi.Remote;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class Channel<T> extends UnicastRemoteObject implements go.Channel<T> {

    private shm.Channel<T> channel;

    public Channel(String name) {
        this.channel = new Channel(name);
    }

    public void out(T v) {
        this.channel.out(v);
    }

    public T in() {
        return this.channel.in();
    }

    public String getName() {
        return this.channel.getName();
    }

    public void observe(Direction dir, Observer observer) {
        this.channel.observe(dir, observer);
    }

    public void notify(List<Observer> observers){
        this.channel.notify(observers);
    }
}
