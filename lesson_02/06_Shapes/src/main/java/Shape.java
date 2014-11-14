/**
 * Фигура
 */
public abstract class Shape {
    protected final String name;

    public Shape(String name) {
        this.name = name;
    }

    /**
     * Имя фигуры и все параметры
     */
    abstract void show();
}
