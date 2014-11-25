import org.junit.Test;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;

/**
 * Тест производительности
 * <p/>
 * String: 2 секунды
 * StringBuilder: 0.022 секунды
 */
public class StringsTest {

    public static final int ITERATIONS = 20000;
    public static final String STRING_FILENAME = "string.txt";
    public static final String STRING_BUILDER_FILENAME = "stringBuilder.txt";

    @Test
    public void testString() throws IOException {
        String s = "";
        for (int i = 0; i < ITERATIONS; ++i) {
            s = s + Integer.toString(i);
        }
        FileWriter fileWriter = new FileWriter(STRING_FILENAME);
        fileWriter.write(s);
        fileWriter.close();
    }

    @Test
    public void testStringBuilder() throws IOException {
        StringBuilder s = new StringBuilder("");
        for (int i = 0; i < ITERATIONS; ++i) {
            s.append(i);
        }
        FileWriter fileWriter = new FileWriter(STRING_BUILDER_FILENAME);
        fileWriter.write(s.toString());
        fileWriter.close();
    }

    @Test
    public void testCompareFiles() throws IOException {
        String res1 = fileToString(STRING_FILENAME);
        String res2 = fileToString(STRING_BUILDER_FILENAME);
        assertEquals(res1, res2);

        //File f1 = new File(STRING_FILENAME);
        Scanner s1 = new Scanner(new File(STRING_FILENAME));
        String str1 = s1.next();
        Scanner s2 = new Scanner(new File(STRING_BUILDER_FILENAME));
        String str2 = s2.next();
        assertEquals(str1, str2);
    }

    private String fileToString(String filename) throws IOException {
        FileReader fileReader = new FileReader(filename);
        StringBuilder builder = new StringBuilder();
        while (fileReader.ready()) {
            int charCode = fileReader.read();
            char[] chars = Character.toChars(charCode);
            builder.append(chars);
        }
        return builder.toString();
    }
}
