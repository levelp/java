import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.Period;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 *
 */
public class JodaTimeDemo {

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

    public static void main(String[] args) {
        LocalDate now = new LocalDate();
        System.out.println(now);
        System.out.println(now.dayOfMonth().getAsText());
        System.out.println(now.plusDays(10));
        System.out.println(now.minusMonths(4));

        SimpleDateFormat russianDate =
                new SimpleDateFormat("dd.MM.yyyy");
        System.out.println(russianDate.format(now.toDate()));

        LocalDate date = new LocalDate(2013, 5, 3);
        System.out.println(russianDate.format(date));
    }
}
