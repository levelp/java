import dao.XMLFile;

import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;

/**
 * Вспомогательный класс, который умеет сохранять и загружать наш объект
 */
public class MyClassHelper {
    /**
     * Запись объекта в XML-файл
     *
     * @param obj      объект
     * @param filename имя файла
     * @throws Exception
     */
    public static void write(Object obj, String filename) throws Exception {
        XMLEncoder encoder =
                new XMLEncoder(
                        new BufferedOutputStream(
                                new FileOutputStream(filename)));
        encoder.writeObject(obj);
        encoder.close();
    }

    public static Person read(String filename) throws Exception {
        XMLFile<Person> xmlFile = new XMLFile<Person>();
        return xmlFile.load(filename);
    }
}