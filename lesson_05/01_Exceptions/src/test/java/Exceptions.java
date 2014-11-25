import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Исключения
 */
public class Exceptions {


    @Test
    public void testRuntimeException() {
        try {
            ///....

            f(10);

            // ...
        } catch (MyException ex) {
            assertEquals("Моё исключение", ex.name);
            assertEquals(23, ex.i);
            assertEquals(2.3, ex.d, 1e-15);
        } catch (Exception ex) {

        }
    }

    private void f(int i) {
        if (i == 0)
            throw new MyException("Моё исключение", 23, 2.3);
        f(i - 1);
    }
}
