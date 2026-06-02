package go.cs;

import java.rmi.Remote;
import java.rmi.RemoteException;

/** Un canal de communication synchrone, qui permet d'envoyer et recevoir des messages de type T. */
public interface ObserverRemote extends Remote {

    void update() throws RemoteException;

}
