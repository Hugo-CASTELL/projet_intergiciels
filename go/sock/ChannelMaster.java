package go.sock;

import go.shm.Channel;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ChannelMaster {

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

        // Préparation
        ServerSocket serverSocket = new ServerSocket(PORT);
        go.shm.Channel channel = new Channel("shared");

        // Lancer le serveur
        while(true){
            Socket sock = serverSocket.accept();
            BufferedReader received = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            PrintWriter answer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(sock.getOutputStream())), true);

            Boolean wasOutBefore = false;
            while(true) {
                String message = received.readLine();
                if(wasOutBefore){
                    channel.out(message);
                    break;
                } else {
                    if(message.equals("IN")) {
                        channel.in();
                        // TODO Alexis, il faut pas écrire un truc en réponse ?
                    } else if (message.equals("OUT")) {
                        wasOutBefore = true;
                        // TODO Alexis, il faut pas écrire un truc en réponse ?
                    } else {
                        break;
                    }
                }
            }
        }
    }
}