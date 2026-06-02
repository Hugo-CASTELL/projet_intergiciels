package go.sock;

import go.Channel;
import go.Direction;
import go.Observer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ChannelSlave<T> implements Channel<T> {
    private String name;
    private Socket socket = null;
    private BufferedReader reader = null;
    private PrintWriter writer = null;
    private Host address = null;

    public ChannelSlave(String name) {
        this.name=name;
        // Cherche à s'enregistrer dans le DNS
        try {
            Registry dns = LocateRegistry.getRegistry(Naming.PORT);
            this.address = (Host) dns.lookup(ChannelMaster.HOSTNAME);
        } catch (RemoteException | NotBoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void ouvrirConnection(){
        try{
            this.socket = new Socket(this.address.getIP(), this.address.getPort());
            this.reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream())),true);
        }
        catch (IOException e){
            System.out.println(e);
        }
        
    }

    private void fermerConnection(){
        try {this.reader.close();}  catch(IOException e){System.out.println(e);}
        this.writer.close();
        try {this.socket.close();}  catch(IOException e){System.out.println(e);}
    }

    public void out(T v) {
        this.ouvrirConnection();

        this.writer.println("OUT");
        this.writer.println(v);

        fermerConnection();
    }

    @SuppressWarnings("unchecked")
    private T parseResponse(String response) {
        if (response == null) {
            return null;
        }

        String trim = response.trim();

        try {
            return (T) Integer.valueOf(trim);
        } catch (NumberFormatException e) { }

        if (trim.equalsIgnoreCase("true") || trim.equalsIgnoreCase("false")) {
            return (T) Boolean.valueOf(trim);
        }

        System.out.println("DEBUG: Server sent as  response: '" + response + "'");

        return (T) trim;
    }
    
    public T in() {
        this.ouvrirConnection();

        this.writer.println("IN");
        String response = null;
        try {
            while(response == null) {
                response = this.reader.readLine();
                try { Thread.sleep(10); } catch (InterruptedException e2) { }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        fermerConnection();
        return this.parseResponse(response);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void observe(Direction direction, Observer observer) {

    }

}