package dao;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Вспомогательный класс, который умеет сохранять и загружать объекты в XML
 */
public class XMLFile {
    /**
     * Запись объекта в XML-файл
     *
     * @param obj      объект
     * @param filename имя файла
     * @throws Exception
     */
    public static void save(Object obj, String filename) throws Exception {
        XMLEncoder encoder =
                new XMLEncoder(
                        new BufferedOutputStream(
                                new FileOutputStream(filename)));
        encoder.writeObject(obj);
        encoder.close();
    }

    /**
     * Загрузка объекта из файла
     *
     * @param filename имя файла
     * @return объект
     * @throws Exception
     */
    public static Object load(String filename) throws Exception {
        XMLDecoder decoder =
                new XMLDecoder(new BufferedInputStream(
                        new FileInputStream(filename)));
        Object object = decoder.readObject();
        decoder.close();
        return object;
    }
}