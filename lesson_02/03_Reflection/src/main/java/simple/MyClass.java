package simple;

/**
 * Класс для проверки работы Reflection API
 */
public class MyClass {
    public int publicIntField;
    public String publicStringField;
    int packageLocalIntField = 50;
    private int privateIntField = 100;


    public String method1() {
        System.out.println("MyClass.method1");
        return "MyClass.method1";
    }

    public String method2(int x) {
        System.out.println("MyClass.method2: x = " + x);
        return "MyClass.method2";
    }

    public String method3() {
        System.out.println("MyClass.method3");
        return "MyClass.method3";
    }
}
