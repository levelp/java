/**
 *
 */
public class BoxingDemo {
    public static void main(String[] args) {
        int i = 10;
        intMethod(i);

        Integer ii = 20;
        intMethod2(ii);
    }

    private static void intMethod2(int ii) {
        System.out.println("ii = " + ii);
    }

    // immutable
    private static void intMethod(Integer i) {
        System.out.println("i = " + i);
    }
}
