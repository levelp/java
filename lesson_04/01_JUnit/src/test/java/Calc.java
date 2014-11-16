/**
 * Тестируемый класс "Калькулятор"
 */
public class Calc {
    /**
     * Сложение
     *
     * @param a первый аргумент
     * @param b второй аргумент
     * @return сумма
     */
    public int add(int a, int b) {
        return a + b;
    }

    /**
     * Деление (надо обработать деление на ноль)
     */
    public double div(int a, int b) {
        return a / b;
    }
}
