/**
 * Разделяемый класс
 */
public class SharedClass {
    public synchronized void increment() {
        Main.globalVar++;
    }
}
