import org.junit.Test;

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
        BinaryTree ints = new BinaryTree();
        ints.addNode(5, "root");
        System.out.println(ints.toString());
    }

}
