/**
 * Точка
 */
public class Point extends Shape {
    private double x, y;

    public Point(String name, double x, double y) {
        super(name);
        this.x = x;
        this.y = y;
    }

    @Override
    void show() {
        System.out.println(toString());
    }

    public String toString() {
        return name + " (" + x + "; " + y + ")";
    }
}
