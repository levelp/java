import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;

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

        System.out.println("Все системные свойства:");
        Properties properties = System.getProperties();
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            System.out.println(" " + entry.getKey() + " = " + entry.getValue());
        }
    }

    /**
     * Свойства из файла настроек
     *
     * @throws IOException
     */
    @Test
    public void testConfigProperties() throws IOException {
        String filename = "config.properties";
        InputStream input = getClass().getClassLoader().getResourceAsStream(filename);
        if (input == null) {
            System.out.println("Sorry, unable to find " + filename);
            return;
        }

        Properties properties = new Properties();
        properties.load(input);

        Enumeration<?> e = properties.propertyNames();
        while (e.hasMoreElements()) {
            String key = (String) e.nextElement();
            String value = properties.getProperty(key);
            System.out.println(key + " = " + value);
        }
    }
}
