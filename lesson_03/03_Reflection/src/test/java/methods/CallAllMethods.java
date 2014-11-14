package methods;

import java.lang.reflect.Method;

/**
 * Вызво всех методов класса
 */
public class CallAllMethods {
    public static void main(String[] args) throws Exception {
        Class c = Class.forName("simple.MyClass");
        // Создаю экземпляр класса
        Object obj = c.newInstance();

        for (Method method : c.getDeclaredMethods()) {
            if (method.getParameterTypes().length == 1) {
                method.invoke(obj, 2);
            } else {
                method.invoke(obj);
            }
        }
    }
}
