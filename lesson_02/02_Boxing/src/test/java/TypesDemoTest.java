import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 */
public class TypesDemoTest {



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
