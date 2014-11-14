import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 */
public class VolatileTest {

    int counter = 0;
    volatile int volatileCounter = 0;
    AtomicInteger atomicInteger = new AtomicInteger(0);

    @Test
    public void testVolatile() throws InterruptedException {
        // Создаём много потоков 
        int numberOfThreads = 10000;
        for (int i = 0; i < numberOfThreads; ++i) {
            new Thread() {
                @Override
                public void run() {
                    for (int j = 0; j < 1000; j++) {
                        counter++;
                        volatileCounter++;
                        atomicInteger.incrementAndGet();
                    }
                }
            }.start();
        }
        Thread.sleep(10000);

        System.out.println("counter = " + counter);
        System.out.println("volatileCounter = " + volatileCounter);
        System.out.println("atomicInteger = " + atomicInteger);
    }


}
