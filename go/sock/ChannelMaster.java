package go.sock;

import go.shm.Channel;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ChannelMaster extends Thread{

    public static final String HOSTNAME = "baobab.n7.fr";

    private static final int PORT = 20001;

    @Override
    public void run() {
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
        } catch (RemoteException | AlreadyBoundException | UnknownHostException e) {
            throw new RuntimeException(e);
        }

        // Préparation
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        go.shm.Channel channel = new Channel("shared");

        // Lancer le serveur
        try {
            while(true){
                Socket sock = serverSocket.accept();
                BufferedReader received = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                PrintWriter answer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(sock.getOutputStream())), true);

                boolean wasOutBefore = false;
                boolean endSocket = false;
                while(endSocket) {
                    String message = received.readLine();
                    if(wasOutBefore){
                        channel.out(message);
                        endSocket = true;
                    } else {
                        if(message.equals("IN")) {
                            answer.println(channel.in());
                            endSocket = true;
                        } else if (message.equals("OUT")) {
                            wasOutBefore = true;
                        } else {
                            break;
                        }
                    }
                }

                answer.println("END_SOCKET");
                answer.close();
                received.close();
                sock.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}