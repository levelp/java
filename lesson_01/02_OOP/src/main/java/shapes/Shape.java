package shapes;

/**
 * Фигура (интерфейс)
 */
public interface Shape {
    /**
     * @return Вывести строчку презентации
     */
    String show();

    /**
     * @return Площадь фигуры
     */
    double area();
}
