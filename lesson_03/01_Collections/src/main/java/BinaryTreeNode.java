/**
 *
 */
public class BinaryTreeNode<T extends Comparable> implements SearchTree<T> {
    public T value = null;
    public BinaryTreeNode<T> left = null;
    public BinaryTreeNode<T> right = null;

    /**
     * Добавление нового значения в дерево поиска
     *
     * @param value добавляемое значение
     */
    @Override
    public void add(T value) {
        // Фиктивный узел (без значения)
        if (this.value == null) {
            this.value = value;
            return;
        }
        // Игнорируем такое же значение
        if (value.equals(this.value))
            return;
        if (value.compareTo(this.value) > 0) {
            if (right == null)
                right = new BinaryTreeNode<T>();
            right.add(value);
        } else {
            if (left == null)
                left = new BinaryTreeNode<T>();
            left.add(value);
        }
    }

    /**
     * Поиск значения в дереве поиска
     *
     * @param value Значение
     * @return найдено ли значение?
     */
    @Override
    public boolean find(T value) {
        // Это фиктивный узел => мы не нашли значения
        if (this.value == null)
            return false;
        // Нашли нужное значение
        if (value.equals(this.value))
            return true;
        if (value.compareTo(this.value) > 0)
            return right != null && right.find(value);
        else
            return left != null && left.find(value);
    }

    /**
     * @return Глубина дерево (максимальное расстояние от корня до листа)
     */
    @Override
    public int deep() {
        // Тернарный оператор
        //   (УСЛОВИЕ) ? ЗНАЧЕНИЕ_ЕСЛИ_TRUE : ЗНАЧЕНИЕ_ЕСЛИ_FALSE
        int result = (value == null) ? 0 : 1;
        // Работает так же, как:
        //if (value == null)
        //    result = 0;
        //else
        //    result = 1;
        int rightResult = 0, leftResult = 0;
        if (right != null)
            rightResult = right.deep();
        if (left != null)
            leftResult = left.deep();

        return result + Math.max(leftResult, rightResult);
    }
}
