package go.cs;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/** Un canal de communication synchrone, qui permet d'envoyer et recevoir des messages de type T. */
public class ObserverRemoteImpl extends UnicastRemoteObject implements ObserverRemote {

    private final go.Observer observer;

    public ObserverRemoteImpl(go.Observer observer) throws RemoteException {
        this.observer = observer;
    }

    public void update() throws RemoteException {
        this.observer.update();
    }

}
