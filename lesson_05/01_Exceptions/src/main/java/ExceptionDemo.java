import com.sun.javaws.exceptions.InvalidArgumentException;

/**
 * Exception
 * RuntimeException
 */
public class ExceptionDemo {

    public static void main(String[] args) throws InvalidArgumentException {
        f(10);
    }

    private static void f(int i) throws InvalidArgumentException {
        if (i == 0)
            throw new InvalidArgumentException(new String[]{"i == 0"});
        f(i - 1);
    }
}
