import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Тестируем сохранение и загрузку
 */
public class MyClassXMLTest extends Assert {

    public static final String FILENAME = "foo.xml";

    @Test
    public void testSaveLoad() throws Exception {
        Person f1 = new Person();
        f1.setName("Петя");
        MyClassHelper.write(f1, FILENAME);

        File f = new File(FILENAME);
        assertTrue(f.exists());
        assertFalse(f.isDirectory());
        assertTrue(new File(FILENAME).isFile());

        // Считываем обратно
        Person f2 = MyClassHelper.read(FILENAME);
        System.out.println("Foo" + f2.getName());
    }
}