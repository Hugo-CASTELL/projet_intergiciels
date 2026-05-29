package go.cs;

import go.Direction;
import go.Observer;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/** Un canal de communication synchrone, qui permet d'envoyer et recevoir des messages de type T. */
public interface ChannelRemote<T> extends Remote {

    void out(T v) throws RemoteException;

    T in() throws RemoteException;

    String getName() throws RemoteException;

    void observe(Direction dir, Observer observer) throws RemoteException;

    void notify(Direction dir) throws RemoteException;

}
