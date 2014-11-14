import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Scanner;

/**
 *
 */
public class CreateInstance {

    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {
        System.out.print("Class name: ");
        Scanner scanner = new Scanner(System.in);
        String className = scanner.next();

        // Object objectA = new A();
        Class myClass = Class.forName(className);
        System.out.println(myClass.getCanonicalName());
        Object objectA = myClass.newInstance();

        for (Field field : myClass.getDeclaredFields()) {
            System.out.println("Field: " + field.getName());
            if ((field.getModifiers() & Modifier.PRIVATE) != 0) {
                System.out.println("PRIVATE");
                field.setAccessible(true);
            }
            System.out.println("value = " + field.get(objectA));
        }

        for (Method method : myClass.getDeclaredMethods()) {
            System.out.println("Method: " + method.getName());
            method.invoke(objectA);
        }

        Method staticMethod = myClass.getMethod("classMethod");
        staticMethod.invoke(null);

    }
}
