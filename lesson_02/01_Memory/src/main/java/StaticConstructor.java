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

        static {
            //counter = 0;
            System.out.println("counter = " + counter);
            System.out.println("a = " + a);
            for(int i = 0; i < 10; i++)
                a += i;
        }

        static int cnt2 = counter + 10;

        int id;

        String name = "Test";

        public MyClass() {
            id = ++counter;
            System.out.println("id = " + id + "  " + name);
        }
    }
}