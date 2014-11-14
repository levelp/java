/**
 * Конструкторы
 */
public class Constructors {

    public static void main(String[] args) {
        MyClass myClass = new MyClass(2);
        MyClass myClass1 = new MyClass(2.0);

        MyClass2 myClass2 = new MyClass2();
    }

    static class MyClass {
        // Конструктор
        public MyClass() {
            System.out.println("Конструктор");
        }

        public MyClass(double i) {
            System.out.println("Конструктор: d = " + i);
        }

        public MyClass(int i) {
            System.out.println("Конструктор: i = " + i);
        }
    }

    static class MyClass2 extends MyClass {

    }
}
