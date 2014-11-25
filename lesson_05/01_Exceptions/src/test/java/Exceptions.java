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
            ex.printStackTrace();
            assertEquals("Моё исключение", ex.name);
            assertEquals(23, ex.i);
            assertEquals(2.3, ex.d, 1e-15);
        } catch (Exception ex) {
            //throw ex;
            //ex.printStackTrace();
        }
    }

    private void f(int i) {
        if (i == 0) {
            throw new MyException("Моё исключение", 23, 2.3);
        }
        f(i - 1);
    }

    @Test
    public void testException() {
        try {
            ///....

            g(10);

            // ...
        } catch (MyException2 ex) {
            assertEquals("Моё исключение", ex.name);
            assertEquals(23, ex.i);
            assertEquals(2.3, ex.d, 1e-15);
        } catch (Exception ex) {

        }
    }

    private void g(int i) throws MyException2 {
        if (i == 0)
            throw new MyException2("Моё исключение", 23, 2.3);
        g(i - 1);
    }
}
