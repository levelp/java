import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

/**
 *
 */
public class TextSaveLoadTest extends Assert {

    /**
     * 1. Сохраняем текст с русскими символами в файл
     * 2. Загружаем его обратно
     * 3. Сравниваем результат с исходным
     *
     * @throws IOException
     */
    @Test
    public void testHello() throws IOException {
        String sample = "Привет!";
        String fileName = "hello.txt";
        TextSaveLoad.save(fileName, sample);

        String result = TextSaveLoad.load(fileName);
        assertEquals(sample, result);
    }

    @Test
    public void testSaveLoadObject() throws IOException {
        MyClass myClass = new MyClass();
        myClass.b = true;
        myClass.d = 1.2;
        myClass.i = 23;
        myClass.str = "Строчка";
        String fileName = "object.txt";

        ObjectSaveLoad.save(fileName, myClass);

        MyClass result = (MyClass) ObjectSaveLoad.load(fileName);
        assertEquals(myClass, result);
    }
}
