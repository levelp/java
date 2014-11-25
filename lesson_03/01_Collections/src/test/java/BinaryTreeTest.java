import org.junit.Test;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Тестирование дерева поиска
 */
public class BinaryTreeTest {
    /**
     * Тестирование добавления в дерево поиска элементов
     */
    @Test
    public void testTree() {
        // Интерфейс =  Класс с реализацией этого интерфейса
        BinaryTree<Integer> tree = new BinaryTree<Integer>();
        assertNull(tree.root);
        assertFalse(tree.find(3));
        tree.add(3);
        assertNotNull(tree.root);
        assertEquals(3, tree.root.value.intValue());
        assertTrue(tree.find(3));
        assertFalse(tree.find(2));

        // Добавить новое значение
        tree.add(2);
        assertNotNull(tree.root);
        assertNotNull(tree.root.left);
        assertEquals(2, tree.root.left.value.intValue());
        assertTrue(tree.find(2));
        assertTrue(tree.find(3));

        // Добавляем узел с большим значением
        tree.add(334);
        assertTrue(tree.find(2));
        assertTrue(tree.find(3));
        assertTrue(tree.find(334));
        assertFalse(tree.find(7789));
    }

    /**
     * Большой случайный тест
     */
    @Test
    public void testBig() {
        // Генератор случайных чисел
        Random random = new Random();
        // Проверочный set
        Set<Integer> test = new HashSet<Integer>();
        SearchTree<Integer> tree = new BinaryTree<Integer>();
        assertEquals("Пока что дерево пустое", 0, tree.deep());
        int valueHit = 0;
        int valueNotFound = 0;
        for (int i = 0; i < 1000; i++) {
            int value = random.nextInt(10000);
            test.add(value);
            tree.add(value);
            assertTrue(tree.find(value));

            int value2 = random.nextInt(10000);
            if (test.contains(value2)) {
                assertTrue(tree.find(value2));
                valueHit++;
            } else {
                assertFalse(tree.find(value2));
                valueNotFound++;
            }
        }
        System.out.println("value: hit " + valueHit + ", not found: " + valueNotFound);
    }

    @Test
    public void testBinaryTreeNode() {
        // Интерфейс =  Класс с реализацией этого интерфейса
        BinaryTreeNode<Integer> tree = new BinaryTreeNode<Integer>();
        assertFalse(tree.find(3));
        tree.add(3);
        assertTrue(tree.find(3));
        assertFalse(tree.find(2));

        // Добавить новое значение
        tree.add(2);
        assertTrue(tree.find(2));
        assertTrue(tree.find(3));

        // Добавляем узел с большим значением
        tree.add(334);
        assertTrue(tree.find(2));
        assertTrue(tree.find(3));
        assertTrue(tree.find(334));
        assertFalse(tree.find(7789));
    }
}
