package p01_datatypes;

/**
 * Передача параметров по ссылке и значению
 */
public class E_ByValueByReference {

    public static void main(String[] args) {
        int i = 10;
        myMethod(i);
        System.out.println("i = " + i);

        MyClass object = new MyClass();
        object.i = 10;
        myMethod2(object);
        System.out.println("object = " + object.i);
    }

    /**
     * @param i значение копируется
     */
    private static void myMethod(int i) {
        i += 20;
        System.out.println("i = " + i);
    }

    /**
     * @param i передаётся по ссылке
     */
    private static void myMethod2(MyClass i) {
        i.i += 20;
        System.out.println("i = " + i.i);
    }

    static class MyClass {
        public int i;
    }
}
