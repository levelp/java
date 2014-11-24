import org.junit.Test;

/**
 * Тесты на демонстрацию работы с внутренними классами
 */
public class InnerClassTest {

    /**
     * Внутренний интерфейс
     */
    interface MyInterface {
        void myMethod();
    }

    @Test
    public void testAnonymClasses() {

        MyInterface myInterface = new MyInterface() {
            @Override
            public void myMethod() {
                System.out.println("InnerClassTest.myMethod");
            }
        };

        myInterface.myMethod();
    }
}
