import org.junit.Test;

import javax.swing.tree.TreeNode;
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

        BinaryTree<Integer> ints = new BinaryTree<Integer>();
        ints.add(3);
        ints.add(8);
        ints.add(1);
        ints.add(5);
        ints.add(1);
        assertTrue(ints.find(5));
    }

    /**
     * Большой случайный тест
     *//*
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
    }*/

}
