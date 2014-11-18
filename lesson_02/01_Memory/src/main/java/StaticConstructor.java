/**
 * Статический конструктор
 */
public class StaticConstructor {

    public static void main(String[] args) {
        MyClass a = new MyClass(), b = new MyClass();

    }

    static class MyClass {
        static int counter = 10;
        static int a = counter * 2;
        static int cnt2 = a + 10;
        String name = "Test " + cnt2;
        static int countInstances = 0;

        static {
            //counter = 0;
            System.out.println("counter = " + counter);
            System.out.println("a = " + a);
            for (int i = 0; i < 10; i++)
                a += i;
            System.out.println("a = " + a);
        }

        int id = ++countInstances;

        public MyClass() {
            id = ++counter;
            System.out.println("id = " + id + "  " + name);
            System.out.println("cnt2 = " + cnt2);
        }
    }
}

