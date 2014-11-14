/**
 * Работа с volatile
 */
public class VolatileDemo {
    //private static volatile int x = 0;
    private static int x = 0;

    public static void main(String[] args) {
        // Запускаем 2 потока
        new ChangeListener().start();
        new ChangeMaker().start();
    }

    static class ChangeListener extends Thread {
        @Override
        public void run() {
            int local_value = x;
            while (local_value < 5) {
                if (local_value != x) {
                    System.out.println("x changed = " + x);
                    local_value = x;
                }
            }
        }
    }

    static class ChangeMaker extends Thread {
        @Override
        public void run() {
            int local_value = x;
            while (x < 5) {
                System.out.println("Incrementing x to " + (local_value + 1));
                x = ++local_value;
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}