package go.cs;

import go.Direction;

import java.rmi.Remote;
import java.rmi.RemoteException;

/** Un canal de communication synchrone, qui permet d'envoyer et recevoir des messages de type T. */
public interface ChannelRemote<T> extends Remote {

    void out(T v) throws RemoteException;

    T in() throws RemoteException;

    String getName() throws RemoteException;

    void observe(Direction dir, ObserverRemote observer) throws RemoteException;

    void notify(Direction dir) throws RemoteException;

}
