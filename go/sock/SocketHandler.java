package go.sock;

import java.io.*;
import java.net.Socket;

class SocketHandler implements Runnable {
    private final Socket sock;
    private final go.shm.Channel channel;

    public SocketHandler(Socket sock, go.shm.Channel channel) {
        this.sock = sock;
        this.channel = channel;
    }

    @Override
    public void run() {
        try {
            BufferedReader received = new BufferedReader(new InputStreamReader(this.sock.getInputStream()));
            PrintWriter answer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(this.sock.getOutputStream())), true);

            System.out.println("ChannelMaster accepted a socket");

            boolean wasOutBefore = false;
            boolean endSocket = false;
            while(!endSocket) {
                String message = null;

                while(message == null) {
                    message = received.readLine();
                }

                System.out.println("ChannelMaster read a message " + message);

                if(wasOutBefore){
                    System.out.println("ChannelMaster runs out(" + message + ")");
                    channel.out(message);
                    endSocket = true;
                } else {
                    if(message.equals("IN")) {
                        System.out.println("ChannelMaster runs in()");
                        answer.println(channel.in());
                        endSocket = true;
                    } else if (message.equals("OUT")) {
                        System.out.println("ChannelMaster received OUT");
                        wasOutBefore = true;
                    } else {
                        break;
                    }
                }
            }

            System.out.println("ChannelMaster closed the socket");
            answer.close();
            received.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                this.sock.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
