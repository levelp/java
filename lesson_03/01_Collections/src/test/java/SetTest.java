import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * Set - множество элементов
 */
public class SetTest {

    static <T> Set<T> arrayToList(final T[] array) {
        final Set<T> l = new TreeSet<T>();
        Collections.addAll(l, array);
        return l;
    }

    /**
     * Основные операции на множествах
     */
    @Test
    public void sets() {
        Set<Integer> intSet = new TreeSet<Integer>();
        assertTrue("Множество пусто", intSet.isEmpty());

        // Метод add() добавить в множество
        intSet.add(7);
        intSet.add(7);
        intSet.add(10);
        assertFalse("Теперь уже не пуст", intSet.isEmpty());

        // size() - количество элементов в списке
        assertEquals("В множестве 2 элемента", 2, intSet.size());
        // get(index) - получить элемент с заданным индексом
        assertTrue(intSet.contains(7));
        assertTrue(intSet.contains(10));

        // Добавляем сразу массив элементов
        intSet.addAll(Arrays.asList(2, 5, 6));
        assertArrayEquals(new Integer[]{2, 5, 6, 7, 10}, setToArray(intSet));

        intSet.addAll(Arrays.asList(88, 99));
        assertArrayEquals(new Integer[]{2, 5, 6, 7, 10, 88, 99}, setToArray(intSet));

        // remove(Object o) - удаление элемента по значению
        assertTrue(intSet.remove(99)); // А в множестве можно так писать!
        assertArrayEquals(new Integer[]{2, 5, 6, 7, 10, 88}, setToArray(intSet));

        assertFalse("Такого элемента нет", intSet.remove(1)); // Удалить элемент со значением 1
        assertArrayEquals(new Integer[]{2, 5, 6, 7, 10, 88}, setToArray(intSet));

        // contains(Object o) - наличия элемента в списке
        assertTrue(intSet.contains(7));
        assertTrue(intSet.contains(88));
        assertFalse(intSet.contains(89));

        // Проверка, содержит ли один список другой список
        assertTrue("Содержатся все эти элементы", intSet.containsAll(arrayToList(new Integer[]{7, 10, 2})));
    }


    @Test
    public void iterators() {
        Set<Integer> list = new TreeSet<Integer>(Arrays.asList(2, 3, 5, 8, 13));

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

    private Integer[] setToArray(Set<Integer> list) {
        return list.toArray(new Integer[list.size()]);
    }

    public static void main(String[] args) {
        Set<String> strings = new HashSet<String>();
        strings.add("Hi");
        strings.add("Test");
        strings.add("Hi");
        for (String s : strings)
            System.out.println(s);
    }
}
