package go.sock;

import go.cs.ChannelRemote;
import go.cs.ChannelRemoteImpl;
import go.cs.ServerImpl;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ChannelMaster{

    public static final String HOSTNAME = "baobab.n7.fr";

    private static final int PORT = 20001;

    public static void main(String[] args) throws Exception {
        // Cherche à s'enregistrer dans le DNS
        try {
            Registry dns = LocateRegistry.getRegistry(Naming.PORT);
            try {
                Host address = (Host) dns.lookup(HOSTNAME);
                throw new RuntimeException("Un hôte est déjà bindé à l'adresse " + HOSTNAME + ":" + PORT + " dans le service de nommage");
            } catch (NotBoundException e) {
                // Donne son IP et son Port en s'enregistrant
                dns.bind(HOSTNAME, new HostImpl(Util.getLocalHostIP(), PORT));
            }
        } catch (RemoteException | AlreadyBoundException e) {
            throw new RuntimeException(e);
        }

        // Lancer le serveur
        ServerSocket serverSocket = new ServerSocket(PORT);
        // TODO while avec accept()
    }
}