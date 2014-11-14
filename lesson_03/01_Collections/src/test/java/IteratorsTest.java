import org.junit.Test;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * Чем полезны итераторы?
 */
public class IteratorsTest {

    @Test
    public void iterators() {
        Set<Integer> integerSet = new TreeSet<Integer>();
        integerSet.add(1);
        integerSet.add(2);
        integerSet.add(2);
        integerSet.add(3);

        show(integerSet);


    }

    private void show(Collection<Integer> cc) {
        Iterator<Integer> iterator = cc.iterator();

        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }

    }
}
