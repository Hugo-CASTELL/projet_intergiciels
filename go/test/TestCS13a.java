package go.test;

import go.Channel;
import go.Direction;
import go.Factory;
import go.Selector;

/** Un unique in/out, ici out */
public class TestCS13a {

    private static void quit(String msg) {
        System.out.println("TestCS13a: " + msg);
        System.exit(msg.equals("ok") ? 0 : 1);
    }

    public static void main(String[] a) {
        Factory factory = new go.cs.Factory();
        Channel<Integer> c1 = factory.newChannel("c1");
        Channel<Integer> c2 = factory.newChannel("c2");
        Channel<Integer> c3 = factory.newChannel("c3");

        Selector s = factory.newSelector(java.util.Map.of(
                c1, Direction.In,
                c2, Direction.In,
                c3, Direction.In));

        new Thread(() -> {
                try { Thread.sleep(5000);  } catch (InterruptedException e) { }
                quit("KO (deadlock)");
        }).start();

        c1.out(1);
        c2.out(2);
        @SuppressWarnings("unchecked")
        Channel<Integer> cc = s.select();
        int v = cc.in();
        if (v != 3) quit("KO");
        quit("ok");
    }
}
