package classes;

import org.junit.Test;
import simple.MyClass;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * Java Reflection API Test
 */
public class ReflectionTest {

    /**
     * Получаем класс по объекту
     *
     * @throws ClassNotFoundException
     */
    @Test
    public void getObjectClass() throws ClassNotFoundException {
        Class c = "foo".getClass();
        assertEquals("java.lang.String", c.getName());

        Set<String> s = new HashSet<String>();
        s.add("test1");
        Class c1 = s.getClass();
        assertEquals("java.util.HashSet", c1.getName());

        showClass(1);
        showClass(1.1);
        showClass(1.2f);
        showClass(true);
        showClass(new MyClass());
    }

    private void showClass(Object object) {
        System.out.println("object = " + object + " -> Class = " + object.getClass());
    }


    @Test
    public void createClass() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class c = "foo".getClass();
        assertEquals("java.lang.String", c.getName());

        Class cDoubleArray = Class.forName("[D");
        assertEquals("double[]", cDoubleArray.getCanonicalName());
        //Object obj = cDoubleArray.newInstance();

        Class cStringArray = Class.forName("[[Ljava.lang.String;");
        assertEquals("java.lang.String[][]", cStringArray.getCanonicalName());
        //Object string = cStringArray.newInstance();
    }
}
