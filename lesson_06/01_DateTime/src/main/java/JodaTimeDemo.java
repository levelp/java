import org.joda.time.*;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 *
 */
public class JodaTimeDemo {

    static SimpleDateFormat dateFormat =
            new SimpleDateFormat("'Дата и время:' dd.MM.yyyy HH:mm:ss G");
    static SimpleDateFormat dateFormat2 =
            new SimpleDateFormat("'Date & time:' dd.MM.yyyy HH:mm:ss G", Locale.US);

    public static void main(String[] args) {
        // LocalDate now = new LocalDate();
        LocalDateTime now = new LocalDateTime();
        System.out.println(showDateTime(now));
        System.out.println(now.dayOfMonth().getAsText());
        System.out.println("День недели: " + now.dayOfWeek().getAsShortText());
        System.out.println("Через 10 дней: " + showDateTime(now.plusDays(10)));
        System.out.println(now.minusMonths(4));

        System.out.println("Через 3 месяца и 10 дней и 2 часа: " +
                showDateTime(now.plusMonths(3).plusDays(10).plusHours(2)));

        System.out.println("Верхний предел: " + showDateTime(now.plusYears(100000)));
        System.out.println("Нижний предел: " + showDateTime(now.minusYears(100000)));

        System.out.println("US: " + dateFormat2.format(now.minusYears(100000).toDate()));

        // Печать даты
        SimpleDateFormat russianDate =
                new SimpleDateFormat("dd.MM.yyyy");
        System.out.println(russianDate.format(now.toDate()));

        LocalDate date = new LocalDate(2013, 5, 3);
        System.out.println(russianDate.format(date.toDate()));
    }

    private static String showDateTime(LocalDateTime dateTime) {
        return dateFormat.format(dateTime.toDate());
    }

    public boolean isAfterPayDay(DateTime datetime) {
        if (datetime.getMonthOfYear() == 2) {   // February is month 2!!
            return datetime.getDayOfMonth() > 26;
        }
        return datetime.getDayOfMonth() > 28;
    }

    public Days daysToNewYear(LocalDate fromDate) {
        LocalDate newYear = fromDate.plusYears(1).withDayOfYear(1);
        return Days.daysBetween(fromDate, newYear);
    }

    public boolean isRentalOverdue(DateTime datetimeRented) {
        Period rentalPeriod = new Period().withDays(2).withHours(12);
        return datetimeRented.plus(rentalPeriod).isBeforeNow();
    }

    public String getBirthMonthText(LocalDate dateOfBirth) {
        return dateOfBirth.monthOfYear().getAsText(Locale.ENGLISH);
    }
}
