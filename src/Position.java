/**
 * Created by brianzhao on 11/11/15.
 */
public class Position {
    private int rowNumber;
    private int columnNumber;
    private int dimension;
    private int representativeNumber;

    public Position(int rowNumber, int columnNumber, int dimension) {
        this.rowNumber = rowNumber;
        this.columnNumber = columnNumber;
        this.dimension = dimension;
        this.representativeNumber = rowNumber * dimension + columnNumber;
    }

    public Position(int representativeNumber, int dimension) {
        this.representativeNumber = representativeNumber;
        this.dimension = dimension;
        this.rowNumber = representativeNumber / dimension;
        this.columnNumber = representativeNumber % dimension;
    }

    public boolean stillOnBoard() {
        return rowNumber < dimension && rowNumber >= 0 && columnNumber < dimension && columnNumber >= 0;
    }

    public int getRepresentativeNumber() {
        return representativeNumber;
    }

    public int getColumnNumber() {
        return columnNumber;
    }

    public int getDimension() {
        return dimension;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Position position = (Position) o;

        if (rowNumber != position.rowNumber) return false;
        if (columnNumber != position.columnNumber) return false;
        if (dimension != position.dimension) return false;
        return representativeNumber == position.representativeNumber;

    }

    @Override
    public String toString() {
        return "Position{" +
                "columnNumber=" + columnNumber +
                ", rowNumber=" + rowNumber +
                ", dimension=" + dimension +
                ", representativeNumber=" + representativeNumber +
                '}';
    }

    @Override
    public int hashCode() {
        return representativeNumber;
    }
}
