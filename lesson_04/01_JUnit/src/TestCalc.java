import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Тестирование класса Calc
 */
public class TestCalc {

    @Test
    public void testAdd() {
        Calc c = new Calc();
        int res = c.add(2, 5);
        if (res != 7)
            fail("Неправильная сумма");
        assertEquals(7, c.add(2, 5));
    }
}
