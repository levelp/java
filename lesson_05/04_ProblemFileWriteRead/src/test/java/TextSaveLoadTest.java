import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

/**
 * Базовые тесты для сохранения/загрузки текста и объекта в/из текстового файла
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
        myClass.aChar = 'G';
        String fileName = "object.txt";

        ObjectSaveLoad.save(fileName, myClass);

        MyClass result = (MyClass) ObjectSaveLoad.load(fileName);
        assertEquals(myClass.str, result.str);
        assertEquals(myClass.b, result.b);
        assertEquals(myClass.aFloat, result.aFloat, 1e-15);
        assertEquals(myClass.aDouble, result.aDouble, 1e-15);
        assertEquals(myClass.i, result.i);
        assertEquals(myClass.aChar, result.aChar);
    }
}
