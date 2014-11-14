import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Демонстрация работы с потоками
 */
public class Main {
    public static final SharedClass lock = new SharedClass();
    public static final Queue<String> strings = new LinkedList<String>();
    public static int globalVar = 0;
    public static AtomicInteger atomicInteger = new AtomicInteger(0);
    public static boolean var2 = false;

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Имя главного потока: " +
                Thread.currentThread().getName());

        Thread1 thread1 = new Thread1("Первый поток");
        thread1.start();

        Thread thread = new Thread(new Thread2());
        thread.start();

        System.out.println("Ждём завершения потока 2");
        thread.join();
        // Остановились
        System.out.println("Ждём завершения потока 1");
        thread1.join();

        System.out.println("globalVar = " + globalVar);
        System.out.println("atomicInteger = " + atomicInteger);
        System.out.println("Программа завершена!");
    }
}
