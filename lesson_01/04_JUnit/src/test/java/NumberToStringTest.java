import org.junit.Assert;
import org.junit.Test;

import static util.NumberToString.intToStr;

/**
 * Тестирование перевода числа в строку
 */
public class NumberToStringTest extends Assert {

    @Test
    public void testIntToStrBefore100() {
        assertEquals("Ноль", intToStr(0));
        assertEquals("Один", intToStr(1));
        assertEquals("Два", intToStr(2));
        assertEquals("Три", intToStr(3));
        assertEquals("Четыре", intToStr(4));
        assertEquals("Пять", intToStr(5));
        assertEquals("Девять", intToStr(9));
        assertEquals("Десять", intToStr(10));
        assertEquals("Одиннадцать", intToStr(11));
        assertEquals("Двенадцать", intToStr(12));
        assertEquals("Тринадцать", intToStr(13));
        assertEquals("Четырнадцать", intToStr(14));
        assertEquals("Пятнадцать", intToStr(15));
        assertEquals("Шестнадцать", intToStr(16));
        assertEquals("Семьнадцать", intToStr(17));
        assertEquals("Восемьнадцать", intToStr(18));
        assertEquals("Девятьнадцать", intToStr(19));
        assertEquals("Двадцать", intToStr(20));
        assertEquals("Двадцать один", intToStr(21));
        assertEquals("Двадцать два", intToStr(22));
        assertEquals("Двадцать шесть", intToStr(26));
        assertEquals("Тридцать", intToStr(30));
        assertEquals("Тридцать один", intToStr(31));
        assertEquals("Тридцать два", intToStr(32));
        assertEquals("Сорок", intToStr(40));
        assertEquals("Сорок один", intToStr(41));
        assertEquals("Пятьдесят два", intToStr(52));
        assertEquals(intToStr(63), ("Шестьдесят три"));
        assertEquals(intToStr(74), ("Семьдесят четыре"));
        assertEquals(intToStr(80), ("Восемьдесят"));
        assertEquals(intToStr(90), ("Девяносто"));
        assertEquals(intToStr(91), ("Девяносто один"));
        assertEquals(intToStr(92), ("Девяносто два"));

    }

    @Test
    public void testintToStr_100_to_199() {
        assertEquals(intToStr(100), "Сто");
        assertEquals(intToStr(101), "Сто один");
        assertEquals(intToStr(102), "Сто два");
        assertEquals(intToStr(111), "Сто одиннадцать");
        assertEquals(intToStr(120), "Сто двадцать");
        assertEquals(intToStr(121), "Сто двадцать один");
        assertEquals(intToStr(122), "Сто двадцать два");
        assertEquals(intToStr(140), "Сто сорок");
        assertEquals(intToStr(141), "Сто сорок один");
    }

    @Test
    public void testintToStr_200_to_999() {
        assertEquals(intToStr(200), "Двести");
        assertEquals(intToStr(201), "Двести один");
        assertEquals(intToStr(202), "Двести два");
        assertEquals(intToStr(211), "Двести одиннадцать");
        assertEquals(intToStr(220), "Двести двадцать");
        assertEquals(intToStr(221), "Двести двадцать один");
        assertEquals(intToStr(222), "Двести двадцать два");
        assertEquals(intToStr(240), "Двести сорок");
        assertEquals(intToStr(241), "Двести сорок один");
        assertEquals(intToStr(245), "Двести сорок пять");
        assertEquals(intToStr(999), "Девятьсот девяносто девять");
    }

    @Test
    public void testIntToStr_1000_to_999999() {
        assertEquals(intToStr(1000), "Одна тысяча");
        assertEquals(intToStr(2000), "Две тысячи");
        assertEquals(intToStr(3000), "Три тысячи");
        assertEquals(intToStr(4000), "Четыре тысячи");
        assertEquals(intToStr(5000), "Пять тысяч");
        assertEquals(intToStr(10000), "Десять тысяч");
        assertEquals(intToStr(20000), "Двадцать тысяч");
        assertEquals(intToStr(99912), "Девяносто девять тысяч девятьсот двенадцать");
        assertEquals(intToStr(920912), "Девятьсот двадцать тысяч девятьсот двенадцать");
        assertEquals(intToStr(911912), "Девятьсот одиннадцать тысяч девятьсот двенадцать");
    }

}
