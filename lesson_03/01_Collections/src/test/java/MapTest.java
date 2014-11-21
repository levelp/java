import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Map
 */
public class MapTest extends Assert {

    @Test
    public void testMapOperations() {
        Map<String, String> map = new HashMap<String, String>();

        assertTrue("Список пуст", map.isEmpty());

        map.put("Hi", "Привет");
        assertEquals(1, map.size());

        map.put("Size", "Размер");

        assertEquals("Привет", map.get("Hi"));
    }
}
