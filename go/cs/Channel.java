package go.cs;

import go.Direction;
import go.Observer;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class Channel<T> implements go.Channel<T> {

    private final go.cs.ChannelRemote<T> channel;

    public Channel(String name) {
        try {
            Registry dns = LocateRegistry.getRegistry(ServerImpl.PORT);

            // récupération de l'ancien channel
            ChannelRemote channelBinding = null;
            try {
                channelBinding = (go.cs.ChannelRemote) dns.lookup(name);
            } catch (NotBoundException e) {
                channelBinding = new ChannelRemoteImpl(name);
                dns.bind(name, channelBinding);
            }

            this.channel = channelBinding;

        } catch (RemoteException | AlreadyBoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void out(T v) {
        try {
            this.channel.out(v);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public T in() {
        try {
            return this.channel.in();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public String getName() {
        try {
            return this.channel.getName();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void observe(Direction dir, Observer observer) {
        try {
            this.channel.observe(dir, observer);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void notify(Direction dir) throws RemoteException {
        this.channel.notify(dir);
    }
}
