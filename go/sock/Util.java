package go.sock;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Util {

    public static String getLocalHostIP() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostAddress();
    }
}
