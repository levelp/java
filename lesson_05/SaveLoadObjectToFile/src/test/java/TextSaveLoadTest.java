import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

/**
 * Базовые тесты для сохранения/загрузки текста и объекта в/из текстового файла
 */
public class TextSaveLoadTest extends Assert {

    @Test
    public void testSaveLoadObject() throws Exception {
        MyClass myClass = new MyClass();
        myClass.b = true;
        myClass.i = 23;
        myClass.str = "Строчка";
        myClass.aByte = 44;
        myClass.aShort = 323;
        myClass.aLong = 12312L;
        myClass.aFloat = 3.22f;
        myClass.aDouble = 1.2;
        String fileName = myClass.getClass().getName() + ".txt";

        ObjectSaveLoad.save(fileName, myClass);

        MyClass result = (MyClass) ObjectSaveLoad.load(fileName);
        assertEquals(myClass.str, result.str);
        assertEquals(myClass.b, result.b);
        assertEquals(myClass.aFloat, result.aFloat, 1e-15);
        assertEquals(myClass.aDouble, result.aDouble, 1e-15);
        assertEquals(myClass.i, result.i);
    }
}
