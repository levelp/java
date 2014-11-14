/**
 * Динамическая память
 */
public class DynMemoryClass {

    static int staticVar = 10;

    public static void main(String[] args) {
        MyClass myClass = new MyClass();

        int i = 2;
        rec(i);
    }

    private static void rec(int i) {
        double d = 10;
        rec(i + 1);
    }
}
