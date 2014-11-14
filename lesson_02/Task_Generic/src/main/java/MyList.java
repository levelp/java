/**
 * Односвязанный список
 * Тип элементов - T
 */
public class MyList<T> {
    /**
     * Элемент списка
     */
    class MyListElement {
        T value;
        MyListElement next;
    }

    /**
     * Корневой элемент списка
     */
    MyListElement root = null;

    public int size() {
        int count = 0;
        MyListElement cur = root;
        while (cur != null) {
            ++count;
            cur = cur.next;
        }
        return count;
    }

    public void addToBegin(T value) {
        MyListElement newElement = new MyListElement();
        newElement.value = value;

        // Новый элемент становится первым
        // и ссылается на тот список, который был до операции
        // добавления
        newElement.next = root;

        root = newElement;
    }

    /**
     * Вывод списка в консоль
     */
    public void show() {
        for (MyListElement cur = root; cur != null; cur = cur.next) {
            System.out.print(cur.value);
            if (cur.next != null)
                System.out.print(" ");
        }
        System.out.println();
    }

    /**
     * Добавление элемента в конец
     *
     * @param value
     */
    public void addToEnd(T value) {
        if (root == null) {
            addToBegin(value);
            return;
        }

        MyListElement newElement = new MyListElement();
        newElement.value = value;
        // Новый элемент -> последний
        newElement.next = null;

        MyListElement cur = root;
        while (cur.next != null)
            cur = cur.next;

        // Новый элемент добавляем в конец списка
        cur.next = newElement;
    }
}
