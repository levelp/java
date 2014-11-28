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
        // Используйте: OutputStreamWriter
        OutputStreamWriter writer = new OutputStreamWriter(
                new BufferedOutputStream(
                        new FileOutputStream(fileName)
                ), "UTF-8");
        // Через Reflection API получаем класс
        Class<?> aClass = object.getClass();
        writer.write(aClass.getName() + "\n");
        // Через Reflection API получаем список полей
        for (Field field : aClass.getDeclaredFields()) {
            // Получаем значение поля
            String value = field.get(object).toString();
            writer.write(String.format("%s:%s = %s\n", field.getName(),
                    field.getType().getName(), value));
        }
        // Закрываем файл
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
        // Имя класса
        String className = scanner.nextLine().trim();
        System.out.println("Имя класса: " + className);
        // Загружаем класс по имени
        Class<?> aClass = Class.forName(className);
        // Создаём экземпляр класса
        Object object = aClass.newInstance();

        while (scanner.hasNextLine()) {
            //Pattern hh = Pattern.compile("(\\w+):");
            String nameAndType = scanner.next();
            String s[] = nameAndType.split(":");
            String name = s[0];
            String type = s[1];
            System.out.println("Имя поля: " + name);

            // Пропускаем "="
            scanner.next();

            String value = scanner.next();
            System.out.println("Значение поля: " + value);

            scanner.nextLine();

            setField(object, name, value, type);
        }

        return object;
    }

    /**
     * Установка значения поля
     *
     * @param object    Объект
     * @param fieldName Имя поля
     * @param value     Значение поля
     * @param fileType  Тип из файла
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    private static void setField(Object object, String fieldName, String value, String fileType) throws NoSuchFieldException, IllegalAccessException {
        Field field = object.getClass().getField(fieldName);
        Class<?> type = field.getType();
        if (!type.getName().equals(fileType)) {
            throw new RuntimeException("Неверный формат файла");
        }
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
