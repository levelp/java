/**
 * Стек
 */
public class MyStack<T> {
    int counter = 0;
    Object[] data = new Object[0];

    /**
     * Добавить значение в стек
     *
     * @param value
     */
    public void push(T value) {
        ++counter;
        if (data.length < counter) {
            Object[] newData = new Object[counter * 2];
            // System.arraycopy(data, 0, newData, 0, data.length);
            for (int i = 0; i < data.length; ++i)
                newData[i] = data[i];
            data = newData;
        }
        data[counter - 1] = value;
    }

    public int size() {
        return counter;
    }

    /**
     * Получить значение с вершины
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    public T pop() {
        Object value = data[data.length - 1];
        counter--;
        if (counter * 2 < data.length) {
            Object[] newData = new Object[data.length - 1];
            System.arraycopy(data, 0, newData, 0, data.length - 1);
            data = newData;
        }
        return (T) value;
    }
}
