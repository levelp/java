package dao;

import java.util.ArrayList;
import java.util.List;

/**
 * Хранение объектов в файле
 */
public class FileStorage<T extends Entity> implements Storage<T> {
    /**
     * Сохранение объекта в постоянное хранилище
     *
     * @param object
     */
    @Override
    public void save(T object) {

    }

    /**
     * Загрузка из хранилища
     * null если объект не найден
     *
     * @param id
     * @return
     */
    @Override
    public T load(int id) {
        return null;
    }

    /**
     * Обновление сущности в хранилище
     *
     * @param object
     */
    @Override
    public void update(T object) {

    }

    /**
     * Удаление объекта
     *
     * @param object
     */
    @Override
    public void delete(T object) {

    }

    /**
     * @return список всех объектов
     */
    @Override
    public List<T> getList() {
        List<T> result = new ArrayList<T>();
        // TODO: загрузка
        return result;
    }
}
