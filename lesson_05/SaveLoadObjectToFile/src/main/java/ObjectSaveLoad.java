import java.io.*;
import java.lang.reflect.Field;
import java.util.Scanner;

/**
 * Класс, который умеет сохранять в файл
 * русский текст в кодировке UTF-8 и загружать
 * его обратно
 */
public class ObjectSaveLoad {
    /**
     * Сохранение текста в кодировке UTF-8
     *
     * @param fileName имя файла
     * @param object   объект
     */
    public static void save(String fileName, Object object) throws IOException, IllegalAccessException {
        OutputStreamWriter writer = new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(fileName)), "UTF-8");
        Class<?> tClass = object.getClass();
        writer.write(tClass.getName() + "\n");
        for (Field field : tClass.getDeclaredFields()){
            writer.write(field.getName() + ": " + field.get(object).toString());
        }
        writer.close();
    }

    /**
     * Загрузка текста из файла
     *
     * @param fileName имя файла
     * @return загруженный объект
     */
    public static Object load(String fileName) throws Exception {
        Scanner scanner = new Scanner(new File(fileName));

        Class<?> tClass = Class.forName(scanner.nextLine().trim());

        Object object = tClass.newInstance();
        while (scanner.hasNextLine()) {
            String name = scanner.next();
            name = name.substring(0, name.length() - 1);
            String value = scanner.next();
            value = value.substring(0, value.length() - 1);
            scanner.nextLine();
            setField(object, name, value);
        }
        return object;
    }

    /**
     * Установка значения поля
     *
     * @param object    Объект
     * @param fieldName Имя поля
     * @param value     Значение поля
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    private static void setField(Object object, String fieldName, String value) throws NoSuchFieldException, IllegalAccessException {
        Field field = object.getClass().getField(fieldName);
        Class<?> type = field.getType();
        if (type.equals(Byte.TYPE))
            field.setByte(object, Byte.parseByte(value));
        else if (type.equals(Short.TYPE))
            field.setShort(object, Short.parseShort(value));
        else if (type.equals(Integer.TYPE))
            field.setInt(object, Integer.parseInt(value));
        else if (type.equals(Long.TYPE))
            field.setLong(object, Long.parseLong(value));
        else if (type.equals(Float.TYPE))
            field.setFloat(object, Float.parseFloat(value));
        else if (type.equals(Double.TYPE))
            field.setDouble(object, Double.parseDouble(value));
        else if (type.equals(Boolean.TYPE))
            field.setBoolean(object, Boolean.parseBoolean(value));
        else if (type.equals(Character.TYPE))
            field.setChar(object, value.charAt(0));
        else
            field.set(object, value);
    }
}
