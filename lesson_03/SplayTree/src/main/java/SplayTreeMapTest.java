import org.junit.Test;

import static org.junit.Assert.*;

public class SplayTreeMapTest {

    /**
     * На каждом шаге выводить массив для сравнения
     * (вывод в консоль исходного массива и сравнение его с полученными на каждом шагу массивами)
     */
    @Test
    public void testAdd1() {
        SplayTreeMap impl = new SplayTreeMap();

        assertEquals("", impl.toString());

        impl.add(2);
        assertEquals("2 /", impl.toString());
        assertEquals(2, impl.getRootValue().intValue());
        System.out.println("Preorder after insert: " + impl);

        impl.add(4);
        assertEquals("4 / 2 /", impl.toString());
        System.out.println("Preorder after insert: " + impl);

        impl.add(3);
        assertEquals("3 / 2 / 4 /", impl.toString());
        System.out.println("Preorder after insert: " + impl);

        impl.add(9);
        System.out.println("Preorder after insert: " + impl);
        assertEquals("9 / 4 / 3 / 2 /", impl.toString());
    }

    @Test
    public void testAdd2() {
        SplayTreeMap impl = new SplayTreeMap();

        impl.add(1);
        System.out.println("Preorder after insert: " + impl);

        impl.add(4);
        System.out.println("Preorder after insert: " + impl);

        impl.add(3);
        System.out.println("Preorder after insert: " + impl);

        impl.add(2);
        System.out.println("Preorder after insert: " + impl);

        assertEquals("Error testAdd2", "2 / 1 / 3 / 4 /", impl.toString());
    }

    @Test
    public void testzig1() {
        SplayTreeMap impl = new SplayTreeMap();
        assertFalse(impl.find(1));

        impl.add(1);
        System.out.println("Preorder after insert: " + impl);
        assertTrue(impl.find(1));

        impl.add(2);
        System.out.println("Preorder after insert: " + impl);
        assertTrue(impl.find(1));
        assertTrue(impl.find(2));

        assertEquals("2 / 1 /", impl.toString());
    }

    @Test
    public void testzig2() {
        SplayTreeMap impl = new SplayTreeMap();

        impl.add(9);
        System.out.println("Preorder after insert: " + impl);

        impl.add(5);
        System.out.println("Preorder after insert: " + impl);

        assertEquals("5 / 9 /", impl.toString());
    }

    @Test
    public void testzigzig1() {
        SplayTreeMap impl = new SplayTreeMap();

        impl.add(3);
        System.out.println("Preorder after insert: " + impl);
        assertEquals("3 /", impl.toString());

        impl.add(4);
        System.out.println("Preorder after insert: " + impl);
        assertEquals("4 / 3 /", impl.toString());

        impl.add(2);
        assertEquals("2 / 3 / 4 /", impl.toString());
    }

    @Test
    public void testzigzig2() {
        SplayTreeMap impl = new SplayTreeMap();
        impl.add(5);
        System.out.println("Preorder after insert: " + impl);
        impl.add(6);
        System.out.println("Preorder after insert: " + impl);
        impl.add(9);
        assertEquals("Error testzigzig2", "9 / 6 / 5 /", impl.toString());
    }

    @Test
    public void testzigzag1() {
        SplayTreeMap impl = new SplayTreeMap();
        String obr = "4 / 3 / 5 /";
        impl.add(3);
        System.out.println("Preorder after insert: " + impl);
        impl.add(5);
        System.out.println("Preorder after insert: " + impl);
        impl.add(4);

        assertEquals(obr, impl.toString());
    }

    @Test
    public void testzigzag2() {
        SplayTreeMap impl = new SplayTreeMap();
        assertEquals("", impl.toString());

        impl.add(4);
        assertEquals("4 /", impl.toString());
        System.out.println("Preorder after insert: " + impl);

        impl.add(6);
        assertEquals("6 / 4 /", impl.toString());
        System.out.println("Preorder after insert: " + impl);

        impl.add(5);
        assertEquals("5 / 4 / 6 /", impl.toString());
    }


    /**
     * Тестирование удаления элемента из дерева
     * (вывод в консоль дерева до удаления и перестроенного дерева после удаления элемента)
     */
    @Test
    public void testRemove() {
        SplayTreeMap impl = new SplayTreeMap();
        assertEquals("", impl.toString());

        impl.add(4);
        assertEquals("4 /", impl.toString());
        System.out.println("Preorder after insert: " + impl);

        impl.add(6);
        assertEquals("6 / 4 /", impl.toString());
        System.out.println("Preorder after insert: " + impl);

        impl.add(5);
        assertEquals("5 / 4 / 6 /", impl.toString());

        // Удаляем элемент 6
        impl.remove(6);
        assertEquals("5 / 4 /", impl.toString());

        // Удаляем элемент 4
        impl.remove(4);
        assertEquals("5 /", impl.toString());

        // Удаляем элемент 5
        impl.remove(5);
        assertEquals("", impl.toString());
    }

}
