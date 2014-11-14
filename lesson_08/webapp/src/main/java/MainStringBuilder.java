/**
 * User: gkislin
 * Date: 16.06.2014
 */
abstract public class MainStringBuilder {
    public static void main(String[] args) {
        String[] arr = new String[]{"1", "2", "3", "4"};
        StringBuilder res = new StringBuilder();
        for (String anArr : arr) {
            res.append(anArr).append("-");
        }
        System.out.println(res.toString());
    }

    static void print(int i) {
        System.out.println(i);
    }
}

