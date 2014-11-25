/**
 *
 */
public class MyException2 extends Exception {
    final double d;
    final String name;
    final int i;

    public MyException2(String name, int i, double d) {
        super();
        this.name = name;
        this.i = i;
        this.d = d;
    }
}
