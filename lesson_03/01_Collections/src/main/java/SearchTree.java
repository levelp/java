/**
 * Дерево поиска (интерфейс)
 */
public interface SearchTree<T extends Comparable> {
    /**
     * Добавление нового значения в дерево поиска
     *
     * @param value добавляемое значение
     */
    void add(T value);

    /**
     * Поиск значения в дереве поиска
     *
     * @param value Значение
     * @return найдено ли значение?
     */
    boolean find(T value);

    /**
     * @return Глубина дерево (максимальное расстояние от корня до листа)
     */
    int deep();
}
