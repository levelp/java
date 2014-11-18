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
        System.out.println("minInt = " + minInt);

        String str = "123";
        int value = Integer.parseInt(str);
        System.out.println("value = " + value);

        int binValue = Integer.parseInt("01011", 2);
        System.out.println("binValue = " + binValue);


    }
}
