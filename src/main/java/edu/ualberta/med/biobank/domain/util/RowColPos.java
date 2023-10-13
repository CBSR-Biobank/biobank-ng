package edu.ualberta.med.biobank.domain.util;

import edu.ualberta.med.biobank.util.NullUtil;

public class RowColPos implements Comparable<RowColPos> {

    public static Integer PALLET_96_ROW_MAX = 8;
    public static Integer PALLET_96_COL_MAX = 12;

    public static Integer ROWS_DEFAULT = 8;
    public static Integer COLS_DEFAULT = 12;

    private final Integer row;
    private final Integer col;

    public RowColPos(Integer row, Integer col) {
        if (row == null) {
            throw new IllegalArgumentException("row is null");
        }
        if (row < 0) {
            throw new IllegalArgumentException("row is negative: " + row);
        }
        if (col == null) {
            throw new IllegalArgumentException("column is null");
        }
        if (col < 0) {
            throw new IllegalArgumentException("column is negative: " + col);
        }

        this.row = row;
        this.col = col;
    }

    public Integer getRow() {
        return row;
    }

    public Integer getCol() {
        return col;
    }

    public boolean equals(Integer row, Integer col) {
        return (this.row.equals(row) && this.col.equals(col));
    }

    @Override
    public String toString() {
        return "RowColPos: {row:" + row + ", col:" + col + "}";
    }

    @Override
    public int compareTo(RowColPos that) {
        int cmp = NullUtil.cmp(row, that.row);
        return cmp != 0 ? cmp : NullUtil.cmp(col, that.col);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((col == null) ? 0 : col.hashCode());
        result = prime * result + ((row == null) ? 0 : row.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        RowColPos other = (RowColPos) obj;
        if (col == null) {
            if (other.col != null) return false;
        } else if (!col.equals(other.col)) return false;
        if (row == null) {
            if (other.row != null) return false;
        } else if (!row.equals(other.row)) return false;
        return true;
    }
}
