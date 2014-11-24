import javax.swing.*;
import java.awt.event.ActionListener;

/**
 * Внешний класс
 */
public class OuterClass {
    static int staticOuterField;
    private int outerField;

    public OuterClass() {
    }

    /**
     * 3. Локальные классы - внутри методов основного класса.
     * Могут быть использованы только внутри этих методов.
     * Имеют доступ к членам внешнего класса. Имеют доступ как к локальным переменным,
     * так и к параметрам метода при одном условии - переменные и параметры используемые локальным классом должны
     * быть объявлены final.
     * Не могут содержать определение (но могут наследовать) статических полей, методов и классов (кроме констант).
     */
    void methodWithLocalClass(final int parameter) {
        InnerClass innerInsideMehod; // Эта линия кода синтаксически корректна
        int notFinal = 0;
        class LocalInnerClass {
            int getOuterField() {
                return OuterClass.this.outerField; // Эта линия кода синтаксически корректна
            }

            // notFinal++; // Ошибка компиляции
            int getParameter() {
                return parameter; // Эта линия кода синтаксически корректна
            }
        }
    }

    /**
     * Анонимные (безымянные) классы - объявляются внутри методов основного класса.
     * Могут быть использованы только внутри этих методов.
     * В отличие от локальных классов, анонимные классы не имеют названия.
     * Главное требование к анонимному классу - он должен наследовать существующий класс или
     * реализовывать существующий интерфейс.
     * Не могут содержать определение (но могут наследовать) статических полей, методов и классов (кроме констант).
     */
    void methodWithAnonymousLocalClass(final int interval) {
        // При определении анонимного класса применен полиморфизм - переменная listener
        // содержит экземпляр анонимного класса, реализующего существующий
        // интерфейс ActionListener
        int localVar = 2;
        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
                System.out.println("localVar = " + localVar);
                System.out.println("Эта строка выводится на экран каждые " + interval + " секунд");
            }
        };

        Timer t = new Timer(interval, listener); // Объект анонимного класса использован внутри метода
        t.start();
    }

    /**
     * 1. Статический внутренний класс (с ключевым словом static):
     * Не имеет доступа к членам внешнего класса за исключением статических.
     * Может содержать статические поля, методы и классы, в отличие от других типов внутренних классов.
     */
    static class StaticInnerClass {
        //int getOuterField() {
        //    return OuterClass.this.outerField; // Эта строка кода - ошибка компиляции
        // }

        int getStaticOuterField() {
            return OuterClass.staticOuterField; // Строка кода синтаксически корректна
        }
    }

    // LocalInnerClass inner; // Ошибка компиляции: локальные классы тут не видны

    /**
     * 2. Внутренние классы - объявляются внутри основного класса (без слова static).
     * В отличие от статических внутренних классов, имеют доступ к членам внешнего класса.
     * Не могут содержать (но могут наследовать) определение статических полей, методов и классов (кроме констант).
     */
    class InnerClass {
        public static final int INT_CONSTANT = 3;

        int getOuterField() {
            int i = INT_CONSTANT;
            System.out.println("i = " + i);
            return OuterClass.this.outerField; // Эта линия кода синтаксически корректна
        }
    }
}