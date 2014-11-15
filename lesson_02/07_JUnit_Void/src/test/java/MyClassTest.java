import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import static junit.framework.Assert.assertEquals;

/**
 *
 */
public class MyClassTest {

    @Test
    public void voidMethod() throws IOException {
        MyClass myClass = new MyClass();
        assertEquals("SHOW", myClass.getText());

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(stream));
        myClass.show();
        stream.close();

        String result = stream.toString();
        assertEquals("SHOW\r\n", result);
    }
}
