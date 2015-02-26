package util;

/**
 * Число в строку
 */
public class NumberToString {
    /**
     * Числа от 1 до 19
     */
    static String[] digits = {
            "", "один", "два", "три",
            "четыре", "пять", "шесть", "семь",
            "восемь", "девять", "десять",
            "одиннадцать", "двенадцать",
            "тринадцать", "четырнадцать",
            "пятнадцать", "шестнадцать", "семьнадцать",
            "восемьнадцать", "девятьнадцать",
    };
    /**
     * Десятки
     */
    static String[] decs = {
            "", "", "двадцать", "тридцать",
            "сорок", "пятьдесят", "шестьдесят",
            "семьдесят", "восемьдесят",
            "девяносто"
    };
    static String hundreds[] = {
            "", "сто", "двести", "триста", "четыреста", "пятьсот",
            "шестьсот", "семьсот", "восемьсот", "девятьсот",
    };
    static String thousands[] = {"тысяч", "тысяча", "тысячи"};

    // Статический конструктор
    //-->
    static int calls;

    static {
        // ... действия по инициализации класса
        //  в общем...
        calls = 0; // Инициализация статической
        // переменной
    }
    //<--

    /**
     * Число в строку
     */
    //-->
    public static String intToStr(int N) {
        String res = "";
        if (N == 0)
            res = "ноль";
        if (N >= 1000) {
            // Количество тысяч
            int t = N / 1000;

            if (t >= 100) {
                // Количество сотен
                int h = t / 100;
                res += " " + hundreds[h];
                t -= h * 100;
                N -= h * 100000;
            }

            int dec_t = t / 10;
            if (dec_t > 1) {
                res += " " + decs[dec_t];
                t -= dec_t * 10;
                N -= dec_t * 10000;
            }

            switch (t) {
                case 0:
                    res += " " +
                            thousands[numForm(t)];
                    break;
                case 1:
                    res += " одна " +
                            thousands[numForm(t)];
                    break;
                case 2:
                    res += " две " +
                            thousands[numForm(t)];
                    break;
                default:
                    res += " " + digits[t] + " " +
                            thousands[numForm(t)];
            }
            N -= t * 1000;
        }
        if (N >= 100) {
            // Количество сотен
            int h = N / 100;
            res += " " + hundreds[h];
            N -= h * 100;
        }
        if (N >= 20) { // Есть десятки
            // Количество десятков
            int dec = N / 10;
            res += " " + decs[dec];
            N -= dec * 10;
        }
        if (N > 0)
            res += " " + digits[N];
        // Убираем начальные и конечные пробелы
        res = res.trim();
        // Делаем первую букву большой
        res = res.substring(0, 1).toUpperCase() + res.substring(1);
        return res;
    }

    // Форма числительного в зависимости от числа
    private static int numForm(int N) {
        if (N == 1)
            return 1;
        if (N >= 2 && N <= 4)
            return 2;
        return 0;
    }
    //<--
}
