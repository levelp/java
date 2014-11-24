import junit.framework.TestCase;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Тесты на JUnit3
 * <p/>
 * Тестовый класс должен быть наследником TestCase (junit.framework.TestCase)
 */
public class JUnit3Demo extends TestCase {

    public void testIsEmpty() {
        boolean actual = StringUtils.isEmpty(null);
        assertTrue(actual);

        actual = StringUtils.isEmpty("");
        assertTrue(actual);

        actual = StringUtils.isEmpty(" ");
        assertFalse(actual);

        actual = StringUtils.isEmpty("some string");
        assertFalse(actual);
    }


    private final Map toHexStringData = new HashMap();

    //@After
    protected void setUp() throws Exception {
        toHexStringData.put("", new byte[0]);
        toHexStringData.put("01020d112d7f", new byte[]{1, 2, 13, 17, 45, 127});
        toHexStringData.put("00fff21180", new byte[]{0, -1, -14, 17, -128});
        //...
    }

    protected void tearDown() throws Exception {
        toHexStringData.clear();
    }

    public void testToHexString() {
        for (Iterator iterator = toHexStringData.keySet().iterator(); iterator.hasNext(); ) {
            final String expected = (String) iterator.next();
            final byte[] testData = (byte[]) toHexStringData.get(expected);
            final String actual = StringUtils.toHexString(testData);
            assertEquals(expected, actual);

        }
    }
}
