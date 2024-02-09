package edu.ualberta.med.biobank.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.sql.Timestamp;

public class DateUtil {

    public static final String DATE_PATTERN = "yyyy-MM-dd";

    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm'Z'";

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat(DATE_PATTERN);

    private static final DateFormat DATE_TIME_FORMAT = new SimpleDateFormat(DATE_TIME_PATTERN);

    private DateUtil() {
        throw new AssertionError();
    }

    public static Date parseDate(String datestring) throws ParseException {
        return DATE_FORMAT.parse(datestring);
    }

    public static Date parseDateTime(String timestring) throws ParseException {
        return DATE_TIME_FORMAT.parse(timestring);
    }

    public static String datetimeToString(Date date) {
        DATE_TIME_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
        return DATE_TIME_FORMAT.format(date);
    }

    public static String datetimeToString(Timestamp timestamp) {
        DATE_TIME_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
        return DATE_TIME_FORMAT.format(timestamp);
    }
}
