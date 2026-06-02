package go.test;

import go.Channel;
import go.Factory;

/** Un unique in/out, ici in */
public class TestCS30b {

    private static void quit(String msg) {
        System.out.println("TestCS30b: " + msg);
        System.exit(msg.equals("ok") ? 0 : 1);
    }

    public static void main(String[] a) {
        Factory factory = new go.cs.Factory();
        Channel<Integer> c = factory.newChannel("c");

        new Thread(() -> {
                try { Thread.sleep(5000);  } catch (InterruptedException e) { }
                quit("KO (deadlock)");
        }).start();

        int v = c.in();
        int v2 = c.in();
        quit(v == 4 && v2 == 5 ? "ok" : "KO");
    }
}
