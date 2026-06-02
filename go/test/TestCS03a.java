package go.test;

import go.Channel;
import go.Factory;

/** Un unique in/out, ici out */
public class TestCS03a {

    private static void quit(String msg) {
        System.out.println("TestCS13a: " + msg);
        System.exit(msg.equals("ok") ? 0 : 1);
    }

    public static void main(String[] a) {
        Factory factory = new go.cs.Factory();
        Channel<Integer> c1 = factory.newChannel("c1");
        Channel<Integer> c2 = factory.newChannel("c2");
        Channel<Integer> c3 = factory.newChannel("c3");

        new Thread(() -> {
                try { Thread.sleep(5000);  } catch (InterruptedException e) { }
                quit("KO (deadlock)");
        }).start();

        c1.out(1);
        int v = c2.in();
        if (v != 2) quit("KO");
        c3.out(3);
        quit("ok");
    }
}
