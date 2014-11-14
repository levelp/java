import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class PropertiesTest extends Assert {

    @Test
    public void test() {
        // Получение несуществующего свойства
        assertNull(System.getProperty("NOT-EXISTING-PROPERTY"));

        String value = System.getProperty("NOT-EXISTING-PROPERTY");
        if (value != null && !value.isEmpty()) {
            fail("Нет такого свойства :)");
        }
    }
}
