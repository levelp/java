package webapp.web;

import webapp.util.Util;

/**
 * User: gkislin
 * Date: 06.03.14
 */
public class HtmlUtil {
    public static final String EMPTY_TD = "<img src='img/s.gif'>";

    public static String mask(String value) {
        return Util.isEmpty(value) ? EMPTY_TD : value;
    }

    public static String getUtl(String url) {
        if (Util.isEmpty(url)) return "";
        return (url.startsWith("http")) ? url : "http://" + url;
    }
}
