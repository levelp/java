import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * User: gkislin
 * Date: 30.06.2014
 */
public class Main {
    static int sum = 0;
    volatile int i;
    static final Object LOCK = new Object();
    static final AtomicInteger ATOMIC_SUM = new AtomicInteger();
    static final CountDownLatch CDL = new CountDownLatch(100000);

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 100000; i++) {
            new Thread() {
                @Override
                public void run() {
                    inc();
                    ATOMIC_SUM.incrementAndGet();
                    CDL.countDown();
                }
            }.start();
        }
        System.out.println(sum);
        System.out.println(ATOMIC_SUM.get());
        System.out.println("wait");
//        Thread.sleep(500);
        CDL.await();
        System.out.println(sum);
        System.out.println(ATOMIC_SUM.get());
    }

    static void inc() {
        synchronized (LOCK) {
            sum++;
        }
    }
}
