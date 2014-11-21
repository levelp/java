import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * Список элементов
 */
public class ListTest {
    static <T> List<T> arrayToList(final T[] array) {
        final List<T> l = new ArrayList<T>();
        // Оптимизация: final List<T> l = new ArrayList<T>(array.length);
        Collections.addAll(l, array);
        return l;
    }

    @Test
    public void oldList() {
        List list = new ArrayList();
        list.add(2);
        list.add("Test");
        list.add(2.3);
        list.add(new MyClass());

        assertEquals(4, list.size());
        for (int i = 0; i < list.size(); ++i)
            System.out.println("list[" + i + "] = " + list.get(i));

        for (Object obj : list)
            if (obj instanceof Integer)
                System.out.println(obj);
    }

    /**
     * Список
     */
    @Test
    public void lists() {
        // Интерфейс =  Класс с реализацией этого интерфейса
        List<Integer> integers = new ArrayList<Integer>();
        assertTrue("Список пуст", integers.isEmpty());

        // Метод add() добавить в конец списка
        integers.add(7);
        integers.add(10);
        assertFalse("Теперь уже не пуст", integers.isEmpty());

        // size() - количество элементов в списке
        assertEquals("В списке 2 элемента", 2, integers.size());
        // get(index) - получить элемент с заданным индексом
        Integer integer = integers.get(0);
        assertEquals("0-ой элемент", 7, integer.intValue());
        assertEquals("1-ый элемент", 10, integers.get(1).intValue());

        // Добавить элемент в заданнную позицию (элементы с большими индексами сдвигаются вправо)
        integers.add(1, 11);

        // Теперь 3 элемента: {7, 11, 10}
        assertEquals(3, integers.size());
        assertEquals(7, integers.get(0).intValue());
        assertEquals(11, integers.get(1).intValue());
        assertEquals(10, integers.get(2).intValue());
        // Преобразование в массив
        Integer[] intArray = listToArray(integers);
        assertArrayEquals(new Integer[]{7, 11, 10}, intArray);

        // Добавляем сразу массив элементов
        integers.addAll(Arrays.asList(2, 5, 6));
        assertArrayEquals(new Integer[]{7, 11, 10, 2, 5, 6}, listToArray(integers));

        Integer[] newValues = {88, 99};
        integers.addAll(2, Arrays.asList(newValues));
        assertArrayEquals(new Integer[]{7, 11, 88, 99, 10, 2, 5, 6}, listToArray(integers));

        // remove(Object o) - удаление элемента по значению
        integers.remove(new Integer(99)); // Не так: ints.remove(99);
        assertArrayEquals(new Integer[]{7, 11, 88, 10, 2, 5, 6}, listToArray(integers));

        integers.remove(1); // Удалить элемент с индексом 1
        assertArrayEquals(new Integer[]{7, 88, 10, 2, 5, 6}, listToArray(integers));

        // contains(Object o) - наличия элемента в списке
        assertTrue(integers.contains(7));
        assertTrue(integers.contains(88));
        assertFalse(integers.contains(89));

        // Проверка, содержит ли один список другой список
        assertTrue("Содержатся все эти элементы",
                integers.containsAll(arrayToList(new Integer[]{7, 10, 2})));

        // set(int index, E element)
        assertArrayEquals(new Integer[]{7, 88, 10, 2, 5, 6}, listToArray(integers));
        integers.set(1, 100); // Меняем элемент
        assertArrayEquals(new Integer[]{7, 100, 10, 2, 5, 6}, listToArray(integers));

        // Список с индекса по индекс [fromIndex, toIndex)
        assertArrayEquals(new Integer[]{100, 10}, listToArray(integers.subList(1, 3)));
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

        Iterator<Integer> integerIterator = list.iterator();
        while (integerIterator.hasNext()) {
            System.out.println(integerIterator.next());
        }

    }

    private Integer[] listToArray(List<Integer> list) {
        return list.toArray(new Integer[list.size()]);
    }

    @Test
    public void testMyClassInherit() {
        List<MyClass> myClassList = new ArrayList<MyClass>();
        myClassList.add(new MyClass());
        myClassList.add(new MyClass2());
    }
}
