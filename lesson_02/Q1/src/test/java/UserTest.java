import org.junit.Test;

import static org.junit.Assert.assertSame;

/**
 *
 */
public class UserTest {

    @Test
    public void test1() {
        // Создаём тестового пользователя
        User user = new User();
        // Параметры
        user.name = "dsdsd";

        user.createResume();

        assertSame(user, user.resume.getUser());

        Resume r = user.resume;
    }

}
