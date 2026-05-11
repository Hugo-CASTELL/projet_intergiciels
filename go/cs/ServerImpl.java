package go.cs;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Implantation d'un serveur hébergeant des canaux.
 *
 */
public class ServerImpl {

    public static int PORT = 1099;

    public static void main(String args[]) {
        Registry dns = LocateRegistry.getRegistry(ServerImpl.PORT);
    }

}
