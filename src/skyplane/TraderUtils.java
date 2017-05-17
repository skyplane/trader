package skyplane;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by skyplane on 26.04.17.
 */
public class TraderUtils {

    public final static java.text.SimpleDateFormat DATE_FORMAT_WITH_MINUTES =
            new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");

    static {
        DATE_FORMAT_WITH_MINUTES.setTimeZone(TimeZone.getTimeZone("GMT+3:00"));
    }

}
