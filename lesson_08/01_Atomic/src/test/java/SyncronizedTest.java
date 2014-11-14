import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

/**
 *
 */
public class SyncronizedTest extends Assert {

    public static final int ITERATIONS = 10000;
    final Random random = new Random();

    /**
     * Демонстрация ошибок при отсутсвии синхронизации
     *
     * @throws InterruptedException
     */
    @Test
    public void testSyncronized() throws InterruptedException {
        final MyClass myClass = new MyClass();

        // Страртуем 10000 потоков на increment
        for (int i = 0; i < ITERATIONS; ++i) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    // Поток пусть поспит случайное время
                    wain10s();
                    myClass.incrementAndGetCounter();
                }
            });
            thread.start();
        }

        // Подождём теперь 10 секунд
        Thread.sleep(10000);

        System.out.println("Без syncronized (< 10000): " + myClass.counter);

        // Страртуем 10000 потоков на increment
        for (int i = 0; i < ITERATIONS; ++i) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    wain10s();
                    myClass.incrementAndGetCounterSync();
                }
            });
            thread.start();
        }

        // Подождём теперь 10 секунд
        Thread.sleep(10000);

        System.out.println("С syncronized всё точно: " + myClass.syncCounter);
        assertEquals(ITERATIONS, myClass.syncCounter);
    }

    private void wain10s() {
        try {
            Thread.sleep(random.nextInt(100));
        } catch (InterruptedException e) {
            System.out.println("Глотаем исключение :)");
        }
    }

    static class MyClass {
        public int counter = 0;
        int syncCounter = 0;


        public int incrementAndGetCounter() {
            return ++counter;
        }

        public synchronized int incrementAndGetCounterSync() {
            return ++syncCounter;
        }
    }

}
