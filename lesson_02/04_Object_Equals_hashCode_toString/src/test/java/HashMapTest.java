import org.junit.Test;

import java.util.HashMap;
import java.util.LinkedHashMap;

import static org.junit.Assert.assertEquals;

/**
 * Использование ассоциативного массива
 */
public class HashMapTest {

    @Test
    public void hashMap() {
        HashMap<Integer, String> hashMap = new LinkedHashMap<Integer, String>();

        hashMap.put(1, "один");
        hashMap.put(2, "два");

        assertEquals("два", hashMap.get(2));

    }
}
