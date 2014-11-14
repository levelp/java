/**
 *
 */
public class Sort<T extends Comparable> {
    private final T[] array;

    public Sort(T[] array) {
        this.array = array;
    }

    public T[] sorted() {
        T[] A = array.clone();
        // Сортировка
        for (int i = 0; i < array.length; ++i)
            for (int j = i + 1; j < array.length; ++j)
                if (A[i].compareTo(A[j]) > 0) {
                    swap(A, i, j);
                }
        // Возвращаем результат
        return A;
    }

    private void swap(T[] a, int i, int j) {
        T temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }
}
