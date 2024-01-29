package edu.ualberta.med.biobank.util;

public class StringUtil {
    public static final String EMPTY_STRING = "";

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
}
