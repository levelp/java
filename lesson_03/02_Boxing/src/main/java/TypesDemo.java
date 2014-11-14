import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 */
public class TypesDemo {

    public static void main(String[] args) {
        int i = 2;
        Integer j = 2;
        int maxInt = Integer.MAX_VALUE;
        int minInt = Integer.MIN_VALUE;
        System.out.println("maxInt = " + maxInt);

        String str = "123";
        int value = Integer.parseInt(str);

        int binValue = Integer.parseInt("01011", 2);
        System.out.println("binValue = " + binValue);


    }


    @Test
    public void boxing() {

        double d = 1.2;

        assertEquals(1.4, Double.parseDouble("1.4"), 0.0001);

        //Double.MAX_VALUE;
        //Double.MIN_VALUE;
        //Double.NaN;

        assertTrue(Boolean.parseBoolean("true"));
        assertFalse(Boolean.parseBoolean("false"));

        Integer k = 65535; // FFFF
        assertEquals("FFFF", Integer.toHexString(k));

        Boolean b = true;


        Character c = 'A';
    }
}
