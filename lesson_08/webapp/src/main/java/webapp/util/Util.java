package webapp.util;

/**
 * User: gkislin
 * Date: 04.07.2014
 */
public class Util {
    public static String mask(String str) {
        return str == null ? "" : str;
    }

    public static boolean isEmpty(Object[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

}
