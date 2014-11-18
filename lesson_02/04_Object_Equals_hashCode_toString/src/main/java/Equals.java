import geometry.NamedPoint;
import geometry.Point;

/**
 * Равенство объектов
 * <p>
 * 1. Создадим класс "Точка"
 * 2. Будем проверять точки на равенство
 * 3. Для этого реализуем метод сравнения
 * 4.
 */
public class Equals {

    public static void main(String[] args) {
        Integer i = 2;
        Point P = new Point(2, 3);
        System.out.println("P = " + P);

        Integer i2 = 2;
        Point P2 = new Point(2, 3);
        NamedPoint NP = new NamedPoint("NP", 2, 3);
        System.out.println("P == P2 ? " + P.equals(P2));
        System.out.println("P == NP ? " + P.equals(NP));
        System.out.println("i == i2 ? " + i.equals(i2));
    }
}
