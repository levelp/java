import org.junit.*;

/**
 *
 */
public class BeforeAfterClass {

    @BeforeClass
    public static void beforeClass() {
        System.out.println("BeforeAfterClass.beforeClass");
    }

    @AfterClass
    public static void afterClass() {
        System.out.println("BeforeAfterClass.afterClass");
    }

    @Before
    public void before() {
        System.out.println("BeforeAfterClass.before");
    }

    @After
    public void after() {
        System.out.println("BeforeAfterClass.after");
    }

    @Test
    public void test1() {
        System.out.println("BeforeAfterClass.test1");
    }

    @Test
    public void test2() {
        System.out.println("BeforeAfterClass.test2");
    }


}
