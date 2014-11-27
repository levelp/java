import java.io.*;

/**
 * Класс, который умеет сохранять в файл
 * русский текст в кодировке UTF-8 и загружать
 * его обратно
 */
public class TextSaveLoad {
    /**
     * Сохранение текста в кодировке UTF-8
     *
     * @param fileName имя файла
     * @param text     текст
     */
    public static void save(String fileName, String text) throws IOException {
        // Используйте: OutputStreamWriter
        OutputStreamWriter writer = new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(fileName)), "UTF-8");
        writer.write(text);
        writer.close();
    }

    /**
     * Загрузка текста из файла
     *
     * @param fileName имя файла
     * @return загруженный текст
     */
    public static String load(String fileName) throws IOException {
        InputStreamReader reader = new InputStreamReader(new BufferedInputStream(new FileInputStream(fileName)));
        StringBuilder res = new StringBuilder();
        while (reader.ready()) {
            int charCode = reader.read();
            res.append(Character.toChars(charCode));
        }
        return res.toString();
    }
}
