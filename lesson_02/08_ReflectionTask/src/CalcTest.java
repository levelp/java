import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 *
 */
public class CalcTest {

    public static void main(String[] args) throws Exception {

        callMethod("Calc", "add");
        callMethod("Calc2", "sub");
    }

    private static void callMethod(String className, String methodName) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Class c = Class.forName(className);
        Object obj = c.newInstance();

        Method method = c.getMethod(methodName,
                int.class, int.class);

        System.out.println("res = " +
                method.invoke(obj, 2, 10));
    }
}
