/**
 *
 */
public class DivideByZero {

    public static void main(String[] args) {
        double d = 1.0 / 0.0;
        div(1.0, 0.0);
        //int i = 10 / 0;
        System.out.println("d = " + d);
    }

    private static void div(double v, double v1) {
        if (Math.abs(v1) < 1e-15)
            throw new ArithmeticException("/ by zero");
    }
}
