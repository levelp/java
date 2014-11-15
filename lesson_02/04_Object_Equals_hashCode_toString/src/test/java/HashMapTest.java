import org.junit.Test;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Использование ассоциативного массива
 */
public class HashMapTest {

    @Test
    public void hashMap() {
        HashMap<Integer, String> hashMap = new LinkedHashMap<Integer, String>();

        hashMap.put(1, "один");
        hashMap.put(2, "два");

        hashMap.get(2);

    }
}
