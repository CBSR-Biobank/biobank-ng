package edu.ualberta.med.biobank.domain.util;

public class SbsLabeling {

    public static final String SBS_ROW_LABELLING_PATTERN = "ABCDEFGHIJKLMNOP";

    public static final int SBS_MAX_COLS = 24;

    public static final int ROW_DEFAULT = 8;
    public static final int COL_DEFAULT = 12;

    /**
     * Get the string corresponding to the given RowColPos and using the SBS standard. 2:1 will
     * return C2.
     */
    public static String fromRowCol(final RowColPos rcp) {
        return fromRowCol(rcp.getRow(), rcp.getCol());
    }

    public static String fromRowCol(int row, int col) {
        if (row >= SBS_ROW_LABELLING_PATTERN.length()) {
            throw new IllegalArgumentException("invalid row size for position: " + row);
        }
        if (col >= SBS_MAX_COLS) {
            throw new IllegalArgumentException("invalid column size for position: " + row);
        }
        StringBuffer sb = new StringBuffer();
        sb.append(SBS_ROW_LABELLING_PATTERN.charAt(row));
        sb.append(col + 1);
        return sb.toString();

    }

    /**
     * Get the rowColPos corresponding to the given SBS standard 2 or 3 char string position. Could
     * be A2 or F12.
     */
    public static RowColPos toRowCol(String label) {
        if ((label.length() < 2) || (label.length() > 3)) {
            throw new IllegalArgumentException("invalid length for label string: " + label);
        }

        int row = SBS_ROW_LABELLING_PATTERN.indexOf(label.charAt(0));
        if (row == -1) {
            throw new IllegalArgumentException("row is invalid in label string: " + label);
        }

        int col = Integer.parseInt(label.substring(1)) - 1;

        if (col >= SBS_MAX_COLS) {
            throw new IllegalArgumentException("column is invalid in label string: " + label);
        }
        return new RowColPos(row, col);
    }

}
