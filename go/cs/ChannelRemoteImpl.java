package go.cs;

import go.Direction;
import go.Observer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class ChannelRemoteImpl<T> extends UnicastRemoteObject implements ChannelRemote<T> {

    private go.shm.Channel<T> channel;

    public ChannelRemoteImpl(String name) throws RemoteException {
        this.channel = new go.shm.Channel(name);
    }

    public void out(T v) throws RemoteException {
        this.channel.out(v);
    }

    public T in() throws RemoteException {
        return this.channel.in();
    }

    public String getName() throws RemoteException {
        return this.channel.getName();
    }

    public void observe(Direction dir, Observer observer) throws RemoteException {
        this.channel.observe(dir, observer);
    }

    public void notify(Direction dir) throws RemoteException {
        this.channel.notify(dir);
    }

}
