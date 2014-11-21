import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Список элементов
 */
public class BinaryTreeTest {
    /**
     * Список
     */
    @Test
    public void lists() {
        // Интерфейс =  Класс с реализацией этого интерфейса
        BinaryTree<Integer> ints = new BinaryTree<Integer>();
        ints.add(3);
        assertTrue(ints.find(3));

    }
}
