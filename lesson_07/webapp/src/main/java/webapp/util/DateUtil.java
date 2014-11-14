package webapp.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * User: gkislin
 * Date: 07.07.2014
 */
public class DateUtil {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/yyyy");
    public static final String NOW = "сейчас";
    public static final String[] MONTH = new String[]{
            "", // empty value
            "JANUARY",
            "FEBRUARY",
            "MARCH",
            "APRIL",
            "MAY",
            "JUNE",
            "JULY",
            "AUGUST",
            "SEPTEMBER",
            "OCTOBER",
            "NOVEMBER",
            "DECEMBER"};

    private DateUtil() {
    }

    public static Date getDate(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        return cal.getTime();
    }

    public static String getYear(Date date) {
        if (date == null) {
            return "";
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return String.valueOf(cal.get(Calendar.YEAR));
    }

    public static int getMonth(Date date) {
        if (date == null) {
            return -1;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MONTH);
    }

    public static synchronized String format(Date date) {
        return (date == null) ? NOW : DATE_FORMAT.format(date);
    }
}
