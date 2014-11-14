/**
 * Описание строк форматирования:
 * http://docs.oracle.com/javase/tutorial/java/data/numberformat.html
 */
public class I_Printf {

    public static void main(String[] args) {
        int i = 13;
        System.out.printf("%d%n", i);
        // 3 символа на каждое число
        System.out.printf("%3d\n", i);

        // В шестнадцатеричном виде
        System.out.printf("%x\n", i);

        System.out.printf("%X\n", i);
        System.out.printf("%04X\n", 2132);

        // Перевод
        // Действительные числа
        float f = 1.1f;
        System.out.printf("%.2f", f);

        double d = 1.1;
        System.out.printf("%-10.3f", d);

        //

    }
}
