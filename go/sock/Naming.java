package go.sock;

import go.cs.ServerImpl;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Naming {

    public static int PORT = 1099;

    public static void main(String args[]) throws Exception {
        Registry dns = LocateRegistry.createRegistry(PORT);
        Thread.currentThread().join();
    }

}
