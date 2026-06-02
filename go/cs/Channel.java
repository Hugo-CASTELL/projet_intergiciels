package go.cs;

import go.Direction;
import go.Observer;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Channel<T> implements go.Channel<T> {

    private final go.cs.ChannelRemote<T> channelRemote;

    public Channel(String name) {
        try {
            Registry dns = LocateRegistry.getRegistry(ServerImpl.PORT);

            // récupération de l'ancien channel
            ChannelRemote<T> channelBinding = null;
            try {
                channelBinding = (go.cs.ChannelRemote<T>) dns.lookup(name);
            } catch (NotBoundException e) {
                channelBinding = new ChannelRemoteImpl<T>(new go.shm.Channel<>(name));
                dns.bind(name, channelBinding);
            }

            this.channelRemote = channelBinding;

        } catch (RemoteException | AlreadyBoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void out(T v) {
        try {
            this.channelRemote.out(v);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public T in() {
        try {
            return this.channelRemote.in();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public String getName() {
        try {
            return this.channelRemote.getName();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void observe(Direction dir, Observer observer) {
        try {
            this.channelRemote.observe(dir, new ObserverRemoteImpl(observer));
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void notify(Direction dir) throws RemoteException {
        this.channelRemote.notify(dir);
    }
}
