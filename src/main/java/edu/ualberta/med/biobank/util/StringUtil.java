package edu.ualberta.med.biobank.util;

import java.util.regex.Pattern;

public class StringUtil {

    // Used in StudyEventAttr and EventAttr
    public static final String MUTIPLE_VALUES_DELIMITER = ";";

    public static final String EMPTY_STRING = "";

    private static final Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");

    private StringUtil() {
        throw new AssertionError();
    }

    /**
     * @param unit to repeat
     * @param n number of times to repeat
     * @param delimiter to separate unit-s
     * @return a string of {@code unit} repeated {@code n} times, with a
     *         {@code delimiter} in between each {@code unit}.
     */
    public static String repeat(String unit, int n, String delimiter) {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= n; i++) {
            sb.append(unit);
            if (i < n) {
                sb.append(delimiter);
            }
        }
        return sb.toString();
    }

    public static String truncate(String string, int n, String suffix) {
        if (string.length() - suffix.length() > n) {
            return string.substring(0, n) + suffix;
        }
        return string;
    }


    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        return pattern.matcher(strNum).matches();
    }

}
