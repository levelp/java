/**
 * Точка
 */
public class Point {
    /**
     * Координаты точки x, y
     */
    public double x, y;

    /**
     * Конструктор
     *
     * @param x
     * @param y
     */
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
