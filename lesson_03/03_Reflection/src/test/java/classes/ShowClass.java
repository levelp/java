package classes;

/**
 *
 */
public class ShowClass {

    public static void main(String[] args) throws
            Exception {
        showType("Test");
        showType(1);
        showType(1.1);
        showType(1.1f);
        showType(1L);
        showType('A');
        showType(new ShowClass());
        showType(new Integer(2));
    }

    private static void showType(Object obj)
            throws Exception {
        Class c = obj.getClass();
        System.out.println("value = " + obj +
                "  ->  " + c.getName());

        try {
            Object obj2 = Class.forName(c.getName()).
                    newInstance();

            System.out.println("obj2 = " + obj2);
        } catch (InstantiationException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
