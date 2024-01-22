package edu.ualberta.med.biobank.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.sql.Timestamp;

public class DateUtil {

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    private static final DateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");

    private DateUtil() {
        throw new AssertionError();
    }

    public static Date parseDate(String datestring) throws ParseException {
        return DATE_FORMAT.parse(datestring);
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
