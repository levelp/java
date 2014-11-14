import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 *
 */
public class ATest {


    public static void main(String[] args) throws Exception {

        Class cls = Class.forName("A");
        Object obj = cls.newInstance();
        Method method = cls.getMethod("newMethod");
        Integer integer = (Integer) method.invoke(obj);
        System.out.println("integer = " + integer);

        // Вызываем все статические методы
        for (Method m : cls.getDeclaredMethods()) {
            if ((m.getModifiers() & Modifier.STATIC) == 0)
                continue;
            m.invoke(null);
        }
    }
}
