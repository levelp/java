package geometry;

/**
 *
 */
public class NamedPoint extends Point {
    private final String name;

    public NamedPoint(String name, double x, double y) {
        super(x, y);
        this.name = name;
    }
}
