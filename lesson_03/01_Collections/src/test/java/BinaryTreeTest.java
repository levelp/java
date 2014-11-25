import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Список элементов
 */
public class BinaryTreeTest {
    /**
     * Список
     */
    @Test
    public void lists() {
        BinaryTree tree = new BinaryTree();
        tree.addNode(5, "five");
        tree.addNode(6, "six");
        tree.addNode(1, "one");
        tree.addNode(10, "ten");
        tree.addNode(13, "thirteen");
        tree.addNode(2, "two");

        assertEquals("five", tree.findNode(5));
        assertEquals(null, tree.findNode(99));
    }

}
