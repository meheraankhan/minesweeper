package model;

import java.util.Objects;

public class Location {
    private int row;
    private int col;

    /**
     * Creates the Location.
     * @param row The row of the Location.
     * @param col The column of the Location.
     */
    public Location(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * @return The row of the Location.
     */
    public int getRow() { return row; }

    /**
     * @return The column of the Location.
     */
    public int getCol() { return col; }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Location) {
            Location o = (Location)obj;
            return this.row == o.getRow() && this.col == o.getCol();
        } return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    @Override
    public String toString() {
        return row + " " + col;
    }
}