import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicLong;

/**
 *
 */
public class AtomicTest extends Assert {

    /**
     * Типы данных с "атомарными" операциями из пакета:
     * java.util.concurrent.atomic
     */
    @Test
    public void testAtomicDatatypes() {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        assertEquals(1, atomicInteger.incrementAndGet());
        assertEquals(2, atomicInteger.incrementAndGet());
        assertEquals(3, atomicInteger.incrementAndGet());

        AtomicLong atomicLong = new AtomicLong(1000L);
        assertEquals(1001, atomicLong.incrementAndGet());

        AtomicIntegerArray integerArray = new AtomicIntegerArray(new int[]{1, 2, 3});
        assertEquals(12, integerArray.addAndGet(1, 10));

    }


}
