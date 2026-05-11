package go.cs;

import go.Direction;
import go.Observer;

import java.rmi.Remote;
import java.rmi.server.UnicastRemoteObject;

public class Channel<T> extends go.shm.Channel {

    public Channel(String name) {
        super(name);
    }

}
