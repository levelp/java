import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Тесты в JUnit
 * TDD - Test Driven Development - Разработка через тестирование
 */
public class MyClassTest {

    /**
     * Первый тест
     */
    @Test // Аннотация (начиная с Java7) указывает что это тест
    public void simplest() {
        // assertEquals(message, expected, actual)
        assertEquals("Дважды два четыре", 4, 2 * 2);

        assertEquals("Дважды два четыре", 4.0, 2.0 * 2.0, 1e-15);

        // Сравнение массивов
        assertArrayEquals(new byte[]{1, 2, 3}, new byte[]{1, 2, 3});
    }

    /**
     * Тестируем вычисление факториала
     */
    @Test
    public void factorial() {
        assertEquals(1, MyClass.fact(1));
        assertEquals(2, MyClass.fact(2));
        assertEquals(2 * 3, MyClass.fact(3));
        assertEquals(2 * 3 * 4, MyClass.fact(4));
        assertEquals(2 * 3 * 4 * 5, MyClass.fact(5));
        assertEquals(2 * 3 * 4 * 5 * 6, MyClass.fact(6));
        assertEquals(2 * 3 * 4 * 5 * 6 * 7, MyClass.fact(7));
        assertEquals(2 * 3 * 4 * 5 * 6 * 7 * 8, MyClass.fact(8));
        assertEquals(2 * 3 * 4 * 5 * 6 * 7 * 8 * 9, MyClass.fact(9));
        assertEquals(2 * 3 * 4 * 5 * 6 * 7 * 8 * 9 * 10, MyClass.fact(10));
        assertEquals(2 * 3 * 4 * 5 * 6 * 7 * 8 * 9 * 10 * 11, MyClass.fact(11));
    }
}
