import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Календарь
 */
public class CalendarDemo {
    public static final Locale LOCALE_RU = new Locale("RU");

    String months[] = {"Январь", "Февраль",
            "Март", "Апрель", "Май"
    };

    public static void main(String[] args) throws ParseException {
        Calendar now = Calendar.getInstance();
        System.out.println("День месяца: " +
                now.get(Calendar.DAY_OF_MONTH));
        System.out.println("Месяц: " +
                (now.get(Calendar.MONTH) + 1));
        System.out.println("Год: " +
                now.get(Calendar.YEAR));

        System.out.println("Час: " +
                now.get(Calendar.HOUR));
        System.out.println("Минута: " +
                now.get(Calendar.MINUTE));
        System.out.println("Секунда: " +
                now.get(Calendar.SECOND));

        SimpleDateFormat dateFormat =
                new SimpleDateFormat("'Дата и время:' dd.MM.yyyy HH:mm:ss");
        System.out.println(dateFormat.format(now.getTime()));

        SimpleDateFormat rusMonth =
                new SimpleDateFormat("MMMMM", LOCALE_RU);

        System.out.println(rusMonth.format(now.getTime()));

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(rusMonth.parse("Сентябрь"));
        System.out.println(
                (calendar.get(Calendar.MONTH) + 1)
        );

        // Дата через 2 месяца
        Calendar afterTwoMonths = (Calendar) now.clone();
        afterTwoMonths.add(Calendar.MONTH, 2);

        SimpleDateFormat russianDate =
                new SimpleDateFormat("dd.MM.yyyy");
        System.out.println(russianDate.format(afterTwoMonths.getTime()));

        Calendar date2 = (Calendar) now.clone();
        date2.add(Calendar.HOUR, -30);
        System.out.println(dateFormat.format(date2.getTime()));
    }
}
