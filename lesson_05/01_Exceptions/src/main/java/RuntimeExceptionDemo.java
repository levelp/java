import com.sun.javaws.exceptions.InvalidArgumentException;

/**
 * Exception
 * RuntimeException
 */
public class RuntimeExceptionDemo {

    public static void main(String[] args) throws InvalidArgumentException {
        g(10);
    }

    private static void g(int i) {
        if (i == 0)
            throw new RuntimeException("i == 0");
        g(i - 1);
    }
}
