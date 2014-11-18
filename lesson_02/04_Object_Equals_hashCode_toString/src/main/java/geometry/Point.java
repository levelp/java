package geometry;

/**
 * Точка
 */
public class Point {
    private static final double DELTA = 1e-15;
    private double x, y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!(o instanceof Point))
            return false;

        Point point = (Point) o;
        return Math.abs(x - point.x) < DELTA &&
                Math.abs(y - point.y) < DELTA;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(x) +
                Double.hashCode(y);
    }

    @Override
    public String toString() {
        return "(" + x + "; " + y + ')';
    }
}
