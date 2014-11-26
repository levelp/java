package dao;

import java.util.List;

/**
 * Интерфейс для постоянного хранения данных
 */
public interface Storage<T extends Entity> {

    /**
     * Сохранение объекта в постоянное хранилище
     *
     * @param object объект
     */
    void save(T object) throws Exception;

    /**
     * Загрузка из хранилища
     *
     * @param id
     * @return объект
     */
    T load(int id) throws Exception;

    /**
     * Обновление сущности в хранилище
     *
     * @param object объект
     */
    void update(T object) throws Exception;

    /**
     * Удаление объекта
     *
     * @param object
     */
    void delete(T object) throws Exception;

    /**
     * @return список всех объектов
     */
    List<T> getList() throws Exception;
}
