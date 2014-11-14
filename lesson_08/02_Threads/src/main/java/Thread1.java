/**
 * Первый поток
 */
public class Thread1 extends Thread {
    public static boolean ready = false;

    public Thread1(String name) {
        super(); // Конструктор предка
        setName(name);
    }

    @Override
    public void run() {
        for (int i = 0; i < 50000; ++i) {
            Main.lock.increment();
            Main.atomicInteger.incrementAndGet();

            Main.var2 = true;

            Main.strings.add("i = " + i);
        }
        ready = true;
        for (int i = 0; i < 20; ++i) {
            System.out.println(getName() + " i = " + i);
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Поток 1 завершён!");
    }
}
