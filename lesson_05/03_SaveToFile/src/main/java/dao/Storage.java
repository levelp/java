package dao;

import java.util.List;

/**
 * Интерфейс для постоянного хранения данных
 */
public interface Storage<T extends Entity> {

    /**
     * Сохранение объекта в постоянное хранилище
     *
     * @param object
     */
    void save(T object);

    /**
     * Загрузка из хранилища
     *
     * @param id
     * @return
     */
    T load(int id);

    /**
     * Обновление сущности в хранилище
     *
     * @param object
     */
    void update(T object);

    /**
     * Удаление объекта
     *
     * @param object
     */
    void delete(T object);

    /**
     * @return список всех объектов
     */
    List<T> getList();
}
