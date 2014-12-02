package minmax;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * Тест минимума и максимума
 */
public class MinMaxTest extends Assert {
    private static final Integer[] ARRAY_INT;
    private static final String[] ARRAY_STRING;

    static {
        ARRAY_INT = new Integer[]{2, 4, 7, 1, 4, 9, 123, -5};
        ARRAY_STRING = new String[]{"2", "4", "7", "1", "4", "9", "123", "-5"};
    }

    @Test
    public void testToString() throws Exception {
        System.out.println(Arrays.toString(ARRAY_INT));
    }

    @Test
    public void testIntegerCalculate() throws Exception {
        MinMax.Pair p1 = new MinMax<>(ARRAY_INT).calculate();
        assertEquals(new MinMax<>(null).new Pair(-5, 123), p1);

        MinMaxStatic.Pair p2 = MinMaxStatic.calculate(ARRAY_INT);
        assertEquals(new MinMaxStatic.Pair<>(-5, 123), p2);
    }

    @Test
    public void testStringCalculate() throws Exception {
        MinMax.Pair p1 = new MinMax<>(ARRAY_STRING).calculate();
        assertEquals(new MinMax<>(null).new Pair("-5", "9"), p1);

        MinMaxStatic.Pair p2 = MinMaxStatic.calculate(ARRAY_STRING);
        assertEquals(new MinMaxStatic.Pair<>("-5", "9"), p2);
    }
}
