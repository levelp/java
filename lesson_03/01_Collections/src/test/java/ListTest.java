import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * Список элементов
 */
public class ListTest {
    static <T> List<T> arrayToList(final T[] array) {
        final List<T> l = new ArrayList<T>(array.length);
        Collections.addAll(l, array);
        return l;
    }

    /**
     * Список
     */
    @Test
    public void lists() {
        // Интерфейс =  Класс с реализацией этого интерфейса
        List<Integer> ints = new ArrayList<Integer>();
        assertTrue("Список пуст", ints.isEmpty());

        // Метод add() добавить в конец списка
        ints.add(7);
        ints.add(10);
        assertFalse("Теперь уже не пуст", ints.isEmpty());

        // size() - количество элементов в списке
        assertEquals("В списке 2 элемента", 2, ints.size());
        // get(index) - получить элемент с заданным индексом
        Integer integer = ints.get(0);
        assertEquals("0-ой элемент", 7, integer.intValue());
        assertEquals("1-ый элемент", 10, ints.get(1).intValue());

        // Добавить элемент в заданнную позицию (элементы с большими индексами сдвигаются вправо)
        ints.add(1, 11);

        // Теперь 3 элемента: {7, 11, 10}
        assertEquals(3, ints.size());
        assertEquals(7, ints.get(0).intValue());
        assertEquals(11, ints.get(1).intValue());
        assertEquals(10, ints.get(2).intValue());
        // Преобразование в массив
        Integer[] intArray = listToArray(ints);
        assertArrayEquals(new Integer[]{7, 11, 10}, intArray);

        // Добавляем сразу массив элементов
        ints.addAll(Arrays.asList(2, 5, 6));
        assertArrayEquals(new Integer[]{7, 11, 10, 2, 5, 6}, listToArray(ints));

        Integer[] newValues = {88, 99};
        ints.addAll(2, Arrays.asList(newValues));
        assertArrayEquals(new Integer[]{7, 11, 88, 99, 10, 2, 5, 6}, listToArray(ints));

        // remove(Object o) - удаление элемента по значению
        ints.remove(new Integer(99)); // Не так: ints.remove(99);
        assertArrayEquals(new Integer[]{7, 11, 88, 10, 2, 5, 6}, listToArray(ints));

        ints.remove(1); // Удалить элемент с индексом 1
        assertArrayEquals(new Integer[]{7, 88, 10, 2, 5, 6}, listToArray(ints));

        // contains(Object o) - наличия элемента в списке
        assertTrue(ints.contains(7));
        assertTrue(ints.contains(88));
        assertFalse(ints.contains(89));

        // Проверка, содержит ли один список другой список
        assertTrue("Содержатся все эти элементы",
                ints.containsAll(arrayToList(new Integer[]{7, 10, 2})));

        // set(int index, E element)
        assertArrayEquals(new Integer[]{7, 88, 10, 2, 5, 6}, listToArray(ints));
        ints.set(1, 100); // Меняем элемент
        assertArrayEquals(new Integer[]{7, 100, 10, 2, 5, 6}, listToArray(ints));

        // Список с индекса по индекс [fromIndex, toIndex)
        assertArrayEquals(new Integer[]{100, 10}, listToArray(ints.subList(1, 3)));
    }

    /**
     * Итератор
     */
    @Test
    public void iterators() {
        // 1, 1, 2..
        List<Integer> list = Arrays.asList(2, 3, 5, 8, 13);

        for (Integer i : list) {
            System.out.println("i = " + i);
        }

        // Получаем итератор
        Iterator<Integer> i = list.iterator();

        assertTrue("Следующий элемент есть", i.hasNext());
        assertEquals(2, i.next().intValue());

        assertTrue("Следующий элемент есть", i.hasNext());
        assertEquals(3, i.next().intValue());

        assertTrue("Следующий элемент есть", i.hasNext());
        assertEquals(5, i.next().intValue());

        assertTrue("Следующий элемент есть", i.hasNext());
        assertEquals(8, i.next().intValue());

        assertTrue("Следующий элемент есть", i.hasNext());
        assertEquals(13, i.next().intValue());

        // Удаляем элемент
        // i.remove(); // java.lang.UnsupportedOperationException

        assertFalse("Элементы кончились", i.hasNext());
    }

    private Integer[] listToArray(List<Integer> list) {
        return list.toArray(new Integer[list.size()]);
    }
}
