package dao;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Хранение объектов в файле
 */
public class FileStorage<T extends Entity> implements Storage<T> {
    /**
     * Имя файла для хранения объектов
     */
    private final String fileName;

    XMLFile<Map<Integer, T>> xmlFile = new XMLFile<Map<Integer, T>>();

    public FileStorage(Class<T> clazz) {
        this.fileName = clazz.getName() + ".xml";
    }

    /**
     * Сохранение объекта в постоянное хранилище
     *
     * @param object объект
     */
    @Override
    public void save(T object) throws Exception {
        System.out.println("Save: " + object);
        Map<Integer, T> map = loadFromFile();
        map.put(object.getId(), object);
        saveToFile(map);
    }

    private void saveToFile(Map<Integer, T> map) throws Exception {
        xmlFile.save(map, fileName);
    }

    /**
     * Загрузка из хранилища
     * null если объект не найден
     *
     * @param id идентификатор
     * @return Загруженный объект
     */
    @Override
    public T load(int id) throws Exception {
        System.out.println("Load: " + id);
        Map<Integer, T> map = loadFromFile();
        T object = map.get(id);
        System.out.println("Loaded: " + object);
        return object;
    }

    private Map<Integer, T> loadFromFile() throws Exception {
        try {
            return xmlFile.load(fileName);
        } catch (FileNotFoundException ex) {
            return new HashMap<Integer, T>();
        }
    }

    /**
     * Обновление сущности в хранилище
     *
     * @param object объект
     */
    @Override
    public void update(T object) throws Exception {
        System.out.println("Update: " + object);
        Map<Integer, T> map = loadFromFile();
        map.remove(object.getId());
        map.put(object.getId(), object);
        saveToFile(map);
    }

    /**
     * Удаление объекта
     *
     * @param object объект
     */
    @Override
    public void delete(T object) throws Exception {
        System.out.println("Delete: " + object);
        Map<Integer, T> map = loadFromFile();
        map.remove(object.getId());
        saveToFile(map);
    }

    /**
     * @return список всех объектов
     */
    @Override
    public List<T> getList() throws Exception {
        Map<Integer, T> map = loadFromFile();
        return new ArrayList<T>(map.values());
    }
}
