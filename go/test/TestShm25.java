package go.test;

import go.Channel;
import go.Direction;
import go.Factory;
import go.Selector;

/* select mixte */
public class TestShm25 {

    private static void quit(String msg) {
        System.out.println("TestShm25: " + msg);
        System.exit(msg.equals("ok") ? 0 : 1);
    }

    public static void main(String[] a) {
        Factory factory = new go.shm.Factory();
        Channel<Integer> c1 = factory.newChannel("c1");
        Channel<Integer> c2 = factory.newChannel("c2");
        Channel<Integer> c3 = factory.newChannel("c3");
        Channel<Integer> c4 = factory.newChannel("c4");
        Channel<Integer> c5 = factory.newChannel("c5");

        Selector sIn = factory.newSelector(java.util.Map.of(
                c1, Direction.In,
                c2, Direction.In,
                c3, Direction.In,
                c4, Direction.In,
                c5, Direction.In));

        Selector sOut = factory.newSelector(java.util.Map.of(
                c1, Direction.Out,
                c2, Direction.Out,
                c3, Direction.Out,
                c4, Direction.Out,
                c5, Direction.Out));

        new Thread(() -> {
                try { Thread.sleep(2000);  } catch (InterruptedException e) { }
                quit("KO (deadlock)");
        }).start();

        new Thread(() -> {
                c1.out(1);

                int v = c2.in();
                if (v != 2) quit("KO");

                try { Thread.sleep(100);  } catch (InterruptedException e) { }

                @SuppressWarnings("unchecked")
                Channel<Integer> cOut = sOut.select();
                cOut.out(3);

                c4.out(4);

                @SuppressWarnings("unchecked")
                Channel<Integer> cIn = sIn.select();
                v = cIn.in();
                if (v != 5) quit("KO");

                quit("ok");
        }).start();

        new Thread(() -> {
                @SuppressWarnings("unchecked")
                Channel<Integer> cIn = sIn.select();
                int v = cIn.in();
                if (v != 1) quit("KO");

                try { Thread.sleep(100);  } catch (InterruptedException e) { }

                @SuppressWarnings("unchecked")
                Channel<Integer> cOut = sOut.select();
                cOut.out(2);

                v = c3.in();
                if (v != 3) quit("KO");

                v = c4.in();
                if (v != 4) quit("KO");

                c5.out(5);
        }).start();
                   
    }
}
