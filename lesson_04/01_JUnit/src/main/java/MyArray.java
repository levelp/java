/**
 *
 */
public class MyArray {
    public void get(int i) {

        if (i < 0)
            throw new IndexOutOfBoundsException("Индекс за пределами массива");
    }
}
