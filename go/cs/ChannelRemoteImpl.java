package go.cs;

import go.Direction;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ChannelRemoteImpl<T> extends UnicastRemoteObject implements ChannelRemote<T> {

    private final go.shm.Channel<T> channel;

    public ChannelRemoteImpl(go.shm.Channel<T> channel) throws RemoteException {
        this.channel = channel;
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

    public void observe(Direction dir, ObserverRemote observer) throws RemoteException {
        this.channel.observe(dir, () -> {
            try {
                observer.update();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void notify(Direction dir) throws RemoteException {
        this.channel.notify(dir);
    }

}
