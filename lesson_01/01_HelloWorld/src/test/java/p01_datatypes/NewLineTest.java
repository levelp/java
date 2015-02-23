package p01_datatypes;

import java.util.HashMap;
import java.util.Map;

/**
 * Перевод строки на разных платформах
 */
public class NewLineTest {

    static Map<Integer, String> charNames = new HashMap<>();

    static {
        charNames.put(10, "LF");
        charNames.put(13, "CR");
    }

    public static void main(String[] args) {
        show("\n");  // 0 10 LF
        show("\r\n"); // 0 13 CR + 1 10 LF
        show("\r"); // 0 13 CR
        show("%n"); // Windows: 0 13 CR + 1 10 LF
        // MacOSX: 0 10 LF
        show(System.getProperty("line.separator"));
        // Windows: 0 13 CR + 1 10 LF
        // MacOSX: 0 10 LF
    }

    private static void show(String s) {
        String res = String.format(s);
        System.out.println(res);
        for (int i = 0; i < res.length(); i++) {
            char c = res.charAt(i);
            System.out.println(i + " " + (int) c + " " + charNames.get((int) c));
        }
    }
}
