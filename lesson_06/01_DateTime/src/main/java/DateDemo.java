import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
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
        SimpleDateFormat format =
                new SimpleDateFormat("'Дата и время:' HH:mm:ss dd.MM.yyyy");

        System.out.println(format.format(date));

        SimpleDateFormat russianDate =
                new SimpleDateFormat("dd.MM.yyyy");
        Date date2 = russianDate.parse("10.10.2014");
        System.out.println(date2);
    }
}
