import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Все возможности форматирования даты:
 * https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html
 */
public class DateDemo {

    public static void main(String[] args) throws ParseException {
        Date date = new Date();
        System.out.println("date = " + date);

        // yyyy - год 4 цифры
        // MM - месяц 2 цифры
        // dd - день в месяце 2 цифры
        // HH - час 2 цифры
        // mm - минута 2 цифры
        // ss - секунда 2 цифры
        SimpleDateFormat dateFormat =
                new SimpleDateFormat("'Дата и время:' dd.MM.yyyy HH:mm:ss");

        System.out.println(dateFormat.format(date));

        SimpleDateFormat russianDate =
                new SimpleDateFormat("dd.MM.yyyy");
        Date date2 = russianDate.parse("11.10.2014");
        System.out.println(date2);

        System.out.println(dateFormat.format(date2));
    }
}
