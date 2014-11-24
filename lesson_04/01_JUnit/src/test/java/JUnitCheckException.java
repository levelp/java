import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 *
 */
public class JUnitCheckException {

    @Test
    public void testArray() {
        MyArray myArray = new MyArray();
        try {
            myArray.get(-1);
            fail("Должно быть исключение, т.к. индекс за пределами массива");
        } catch (IndexOutOfBoundsException ex) {
            assertEquals("Индекс за пределами массива", ex.getMessage());
        }
    }
}
