import geometry.Point;
import org.junit.Test;

import static geometry.Settings.DELTA;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Тестирование класса "Точка"
 */
public class PointTest {

    /**
     * Создаём точку, передавая координаты
     * x, y.
     * Получаем x с помощью метода getX
     * Получаем y с помощью метода getY
     * Сравнение двух точек
     */
    @Test
    public void createPoints() {
        Point A = new Point(1, 2);
        Point B = new Point(1, 2);

        assertEquals(1.0, A.getX(), DELTA);
        assertEquals(2.0, A.getY(), DELTA);

        assertEquals(1.0, B.getX(), DELTA);
        assertEquals(2.0, B.getY(), DELTA);

        assertEquals(A, B);

        Point C = new Point(1, 2.5);
        assertNotEquals(A, C);
    }

    @Test
    public void pointHashCode() {
        Point A = new Point(1, 2);
        Point B = new Point(1, 2);
        Point C = new Point(1, 2.5);
        Point D = new Point(2, 1);

        assertEquals(A.hashCode(), B.hashCode());
        assertNotEquals(A.hashCode(), C.hashCode());

        assertEquals(A.hashCode(), D.hashCode());
        assertNotEquals(A, D);
    }


}
