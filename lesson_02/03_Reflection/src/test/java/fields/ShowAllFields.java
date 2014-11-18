package fields;

import simple.MyClass;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Ввывод всех полей класса
 */
public class ShowAllFields {
    public static void main(String[] args) {
        showAllFields(new MyClass());

        MyClass myClass = new MyClass();
        myClass.publicIntField = 10;
        myClass.publicStringField = "Test";
        showAllFields(myClass);
    }

    private static void showAllFields(Object obj) {
        Class c = obj.getClass();
        for (Field field : c.getDeclaredFields()) {
            //field.setAccessible(true);
            try {
                int modifiers = field.getModifiers();
                if ((modifiers & Modifier.PRIVATE) != 0) {
                    System.out.print("private ");
                }
                if ((modifiers & Modifier.PUBLIC) != 0) {
                    System.out.print("public ");
                }
                if (modifiers == 0) {
                    System.out.print("default ");
                    break;
                }
                System.out.println(field.getName() + " = "
                        + field.get(obj) + " " + modifiers);
            } catch (IllegalAccessException e) {
                System.out.println(field.getName());
                e.printStackTrace();
            }
        }
    }
}
