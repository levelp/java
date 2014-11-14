import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 *
 */
public class CountChars {

    @Test
    public void chars() {
        // Считать количество цифр
        assertTrue(Character.isDigit('1'));
        assertFalse(Character.isDigit('X'));
        // Считать количество букв
        assertTrue(Character.isLetter('X'));
        assertFalse(Character.isLetter('#'));

        // + Подсчитывать количество букв A,
        // B, C... всех символов таблицы UTF-8
        Map<Character, Integer> map = new HashMap<Character, Integer>();
        assertFalse(map.containsKey('A'));
        map.put('A', 11);
        map.get('A');



    }

}
