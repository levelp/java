import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

/**
 *
 */
public class SortTest {

    @Test(timeout = 10)
    public void testSortInt() {
        Integer[] ints = {2, 3, 1, 10};
        Sort<Integer> sortInt = new Sort<Integer>(ints);
        assertArrayEquals(new Integer[]{1, 2, 3, 10},
                sortInt.sorted());

        String[] strings = {"adsa", "sdfsdf", "sdfsdf", "sdfdsf"};
        Sort<String> stringSort = new Sort<String>(strings);
        //assertArrayEquals(new String[]);
    }
}
