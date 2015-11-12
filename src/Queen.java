import java.util.HashSet;

/**
 * Created by brianzhao on 11/11/15.
 */
public class Queen {
    private Position position;

    public Queen(Position position) {
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Queen queen = (Queen) o;

        return position.equals(queen.position);

    }

    @Override
    public int hashCode() {
        return position.hashCode();
    }

    public HashSet<Position> getAllAttackingPositions(){
        int lowerBound = 0;
        int upperBound = this.position.getDimension() * this.position.getDimension();
        HashSet<Position> toReturn = new HashSet<>();
        int representativePosition = this.position.getRepresentativeNumber();
        int dimension = this.position.getDimension();
        int rowNumber = this.position.getRowNumber();
        int columnNumber = this.position.getColumnNumber();

        int currentRowNumber = rowNumber;
        int currentColumnNumber = columnNumber;



        //get all items on same column, above the current position
        int currentNumber = representativePosition - dimension;
        while (withinBounds(lowerBound,upperBound,currentNumber)) {
            if (!toReturn.add(new Position(currentNumber, dimension))) {
                throw new RuntimeException("TO RETURN ALREADY CONTAINED THIS POSITION!!!");
            }
            currentNumber -= dimension;
        }

        //get all items on same column, below the current position
        currentNumber = representativePosition + dimension;
        while (withinBounds(lowerBound, upperBound, currentNumber)) {
            if (!toReturn.add(new Position(currentNumber, dimension))) {
                throw new RuntimeException("TO RETURN ALREADY CONTAINED THIS POSITION!!!");
            }
            currentNumber += dimension;
        }

        //get all items on the same row, right of the current position
        currentNumber = representativePosition + 1;
        while (withinBounds(lowerBound, upperBound, currentNumber) && currentNumber % dimension != 0) {
            if (!toReturn.add(new Position(currentNumber, dimension))) {
                throw new RuntimeException("TO RETURN ALREADY CONTAINED THIS POSITION!!!");
            }
            currentNumber++;
        }

        //get all items on the same row, right of the current position
        currentNumber = representativePosition - 1;
        while (withinBounds(lowerBound, upperBound, currentNumber) && currentNumber % dimension != dimension-1) {
            if (!toReturn.add(new Position(currentNumber, dimension))) {
                throw new RuntimeException("TO RETURN ALREADY CONTAINED THIS POSITION!!!");
            }
            currentNumber--;
        }

        //get all items on upper left diagonal
        currentRowNumber = rowNumber-1;
        currentColumnNumber = columnNumber-1;

        Position upperLeft = new Position(currentRowNumber, currentColumnNumber, dimension);
        while (upperLeft.stillOnBoard()) {
            if (!toReturn.add(upperLeft)) {
                throw new RuntimeException("TO RETURN ALREADY CONTAINED THIS POSITION!!!");
            }
            currentRowNumber--;
            currentColumnNumber--;
            upperLeft = new Position(currentRowNumber, currentColumnNumber, dimension);
        }

        //get all items on upper right diagonal
        currentRowNumber = rowNumber - 1;
        currentColumnNumber = columnNumber + 1;
        Position upperRight = new Position(currentRowNumber, currentColumnNumber, dimension);
        while (upperRight.stillOnBoard()) {
            if (!toReturn.add(upperRight)) {
                throw new RuntimeException("TO RETURN ALREADY CONTAINED THIS POSITION!!!");
            }
            currentRowNumber--;
            currentColumnNumber++;
            upperRight = new Position(currentRowNumber, currentColumnNumber, dimension);
        }


        //get all items on lower left diagonal
        currentRowNumber = rowNumber + 1;
        currentColumnNumber = columnNumber - 1;
        Position lowerLeft = new Position(currentRowNumber, currentColumnNumber, dimension);
        while (lowerLeft.stillOnBoard()) {
            if (!toReturn.add(lowerLeft)) {
                throw new RuntimeException("TO RETURN ALREADY CONTAINED THIS POSITION!!!");
            }
            currentRowNumber++;
            currentColumnNumber--;
            lowerLeft= new Position(currentRowNumber, currentColumnNumber, dimension);
        }


        //get all items on lower right diagonal
        currentRowNumber = rowNumber + 1;
        currentColumnNumber = columnNumber + 1;
        Position lowerRight = new Position(currentRowNumber, currentColumnNumber, dimension);
        while (lowerRight.stillOnBoard()) {
            if (!toReturn.add(lowerRight)) {
                throw new RuntimeException("TO RETURN ALREADY CONTAINED THIS POSITION!!!");
            }
            currentRowNumber++;
            currentColumnNumber++;
            lowerRight= new Position(currentRowNumber, currentColumnNumber, dimension);
        }


        return toReturn;
    }

    /**
     * lowerbound inclusive, upperbound exclusive
     * @param lowerBound
     * @param upperBound
     * @param x
     * @return
     */
    private static boolean withinBounds(int lowerBound, int upperBound, int x) {
        return x >= lowerBound && x < upperBound;
    }

}
