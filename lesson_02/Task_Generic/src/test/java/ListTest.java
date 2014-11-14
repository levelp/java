import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class ListTest extends Assert {

    @Test
    public void testList(){
        MyList<String> myList = new MyList<String>();
        assertEquals(0, myList.size());
        myList.addToBegin("Test");
        assertEquals(1, myList.size());

        myList.show();

        myList.addToEnd("Элемент в конце");

        myList.show();
    }
}
