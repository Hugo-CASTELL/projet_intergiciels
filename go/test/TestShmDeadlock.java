package go.test;

import go.Channel;
import go.Direction;
import go.Factory;
import go.Selector;

/* select mixte */
public class TestShmDeadlock {

    private static void quit(String msg) {
        System.out.println("TestShmDeadlock: " + msg);
        System.exit(msg.equals("ok") ? 0 : 1);
    }

    public static void main(String[] a) {
        Factory factory = new go.shm.Factory();
        Channel<Integer> c = factory.newChannel("c");

        Selector sIn = factory.newSelector(java.util.Map.of(c, Direction.In));
        Selector sOut = factory.newSelector(java.util.Map.of(c, Direction.Out));

        new Thread(() -> {
                try { Thread.sleep(2000);  } catch (InterruptedException e) { }
                // On veut un deadlock
                quit("ok");
        }).start();

        new Thread(() -> {
            @SuppressWarnings("unchecked")
            Channel<Integer> cc = sOut.select();
            cc.out(4);
            quit("KO (devrait deadlock)");
        }).start();

        new Thread(() -> {
            try { Thread.sleep(100);  } catch (InterruptedException e) { }
            @SuppressWarnings("unchecked")
            Channel<Integer> cc = sIn.select();
            int v = cc.in();
            quit("KO (devrait deadlock)");
        }).start();

    }
}
