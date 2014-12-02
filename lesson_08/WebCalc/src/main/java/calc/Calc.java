package calc;

/**
 * Вычисления
 */
public class Calc {
    public double sum(String as, String bs) {
        double a = Double.parseDouble(as);
        double b = Double.parseDouble(bs);
        return a + b;
    }

    public double mul(String as, String bs) {
        double a = Double.parseDouble(as);
        double b = Double.parseDouble(bs);
        return a * b;
    }
}
