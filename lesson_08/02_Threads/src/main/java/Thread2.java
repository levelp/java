/**
 * Второй поток
 */
public class Thread2 implements Runnable {

    @Override
    public void run() {
        int stringCount = 0;
        for (int i = 0; i < 50000; ++i) {
            Main.lock.increment();
            Main.atomicInteger.incrementAndGet();

            if (!Main.var2)
                System.out.println("FALSE!");

            synchronized (Main.strings) {
                while (!Main.strings.isEmpty()) {
                    assert Main.strings.size() >= 1;
                    String s = Main.strings.remove();
                    stringCount++;
                }
            }
        }
        while (!Thread1.ready) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        while (!Main.strings.isEmpty()) {
            String s = Main.strings.remove();
            stringCount++;
        }
        System.out.println("stringCount = " + stringCount);

        for (int i = 0; i < 20; ++i) {
            System.out.println("Thread2:  i = " + i);
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Поток 2 завершён!");
    }
}
