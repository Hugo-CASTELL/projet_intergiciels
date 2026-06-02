package go.test;

import go.Channel;
import go.Factory;

/** Un unique in/out, ici out */
public class TestCS03b {

    private static void quit(String msg) {
        System.out.println("TestCS13b: " + msg);
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

        int v1 = c1.in();
        if (v1 != 1) quit("KO");
        c2.out(2);
        int v3 = c3.in();
        if (v3 != 3) quit("KO");
        quit("ok");
    }
}
