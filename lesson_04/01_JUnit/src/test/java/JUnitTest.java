import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import org.junit.rules.Timeout;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * JUnit = Java Unit - библиотека для модульного тестирования (unit tests)
 * В JUnit4 при создании тестов используются Java-аннотации
 * Сслыка: http://habrahabr.ru/post/120101/ - статья про JUnit
 */
public class JUnitTest extends Assert {
    /**
     * Чтобы создаваемые файлы и каталоги гарантировано удалялись после завершения теста
     */
    @Rule
    public final TemporaryFolder folder = new TemporaryFolder();
    @Rule
    public final Timeout timeout = new Timeout(1000);
    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    /**
     * Аннотация @BeforeClass обозначает методы, которые будут вызваны до создания экземпляра тест-класса,
     * методы должны быть public static void.
     * Имеет смысл размещать предустановки для теста в случае, когда класс содержит несколько тестов использующих
     * различные предустановки, либо когда несколько тестов используют одни и те же данные, чтобы
     * не тратить время на их создание для каждого теста.
     */
    @BeforeClass
    public static void beforeClass() {
        System.out.println("@BeforeClass - перед всеми тестами");
    }

    @BeforeClass
    public static void beforeClass2() {
        System.out.println("@BeforeClass2 - перед всеми тестами");
    }

    @AfterClass
    public static void afterClass() {
        System.out.println("@AfterClass - после всех тестов");
    }

    /**
     * Аннотация @Before обозначает методы, которые будут вызваны до исполнения теста, методы должны быть public void.
     * Здесь обычно размещаются предустановки для теста (например генерация тестовых данных).
     */
    @Before
    public void beforeEveryTest() {
        System.out.println("@Before - Перед каждым тестом");
    }

    @Before
    public void beforeEveryTest2() {
        System.out.println("@Before - Ещё один метод перед каждым тестом");
    }

    @After
    public void afterEveryTest() {
        System.out.println("@After - после каждого теста");
    }

    @Test
    public void test1() {
        System.out.println("@Test - Первый тест");
    }

    @Test
    public void test2() {
        System.out.println("@Test - Второй тест");
    }

    // Если в тестируемом коде мы ожимдаем исключение (исключение сообщает об ощибке),
    // то используем параметр expected
    @Test(expected = NumberFormatException.class)
    public void testToHexStringWrong() {
        int x = Integer.parseInt(null);
        System.out.println("x = " + x);
    }

    @Ignore // Отключаем тест (например, чтобы исправить его в когда-нибудь в будущем)
    @Test(timeout = 1000) // 1000 миллисекунд = 1 секунда
    public void infinity() {
        while (true) ;
    }

    @SuppressWarnings("unused") // Чтобы не показывались предупреждения UnusedDeclaration
    @Test
    public void testUsingTempFolder() throws IOException {
        File createdFile = folder.newFile("myfile.txt");
        File createdFolder = folder.newFolder("subfolder");
        // ...
        // Пишем что-то в файл
        PrintWriter writer = new PrintWriter(createdFile);
        writer.println("Выведем строчку в файл");
        writer.close();
    }

    @Test
    public void testAsserts() {
        assertTrue(true);
        assertTrue("Должно быть true", true);
        assertFalse("Должно быть false", false);
        assertEquals(4, 2 * 2);
        assertEquals(4.0, 2.0 * 2.0, 1e-10);
    }
}
