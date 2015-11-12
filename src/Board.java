import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

/**
 * Created by brianzhao on 10/17/15.
 */

public class Board {

    /**
     * dimension must be 2 or higher
     */
    private final int dimension;    //this is the size of rows/columns
    private final Random rand = new Random();
    private Integer evaluationCost;


    /**
     * arraylist will be of queen objects
     * if a position in the arraylist is null, that means no queen exists
     * if it is not null, then a queen exists in tha position of the arraylist
     */
    private Queen[][] underlying; //this is the underlying arraylist


    /**
     * this maps each column to the queen present in each column
     */
    private HashMap<Integer, Queen> columnToQueens;


    //entry point for creating a random puzzle
    public Board(int dimension) {
        this.dimension = dimension;
        generateBoardAndMapOneQueenPerColumn(dimension);
    }

    private Board(Board board, Move action) {
        this.dimension = board.dimension;
        underlying = new Queen[dimension][dimension];
        Queen[][] oldQueenUnderlying = board.underlying;
        for (int i = 0; i < oldQueenUnderlying.length; i++) {
            for (int j = 0; j < oldQueenUnderlying[i].length; j++) {
                underlying[i][j] = oldQueenUnderlying[i][j];
            }
        }

        columnToQueens = new HashMap<>();
        for (Integer i : board.columnToQueens.keySet()) {
            columnToQueens.put(i, board.columnToQueens.get(i));
        }

        Position startPosition = action.getStartPosition();
        Position endPosition = action.getEndPosition();
        if (startPosition.getColumnNumber() != endPosition.getColumnNumber()) {
            throw new RuntimeException("can't move to separate column");
        }
        if (underlying[startPosition.getRowNumber()][startPosition.getColumnNumber()] == null) {
            throw new RuntimeException("no queen to move");
        }
        if (underlying[endPosition.getRowNumber()][endPosition.getColumnNumber()] != null) {
            throw new RuntimeException("queen exists at endpoint");
        }

        Queen toPlace = new Queen(
                new Position(endPosition.getRowNumber(), endPosition.getColumnNumber(), dimension));
        underlying[startPosition.getRowNumber()][startPosition.getColumnNumber()] = null;
        underlying[endPosition.getRowNumber()][endPosition.getColumnNumber()] = toPlace;

        columnToQueens.put(startPosition.getColumnNumber(), toPlace);
    }


    /**
     * will fill the board and hashmap, only allowing one queen per column
     *
     * @param dimension
     * @return
     */
    private void generateBoardAndMapOneQueenPerColumn(int dimension) {
        Queen[][] underlyingQueenBoard = new Queen[dimension][dimension];
        HashMap<Integer, Queen> toSetHashMap = new HashMap<>();
        for (int column = 0; column < dimension; column++) {
            int row = rand.nextInt(dimension);
            //maps column to queen in column
            Queen toInsert = new Queen(new Position(row, column, dimension));
            toSetHashMap.put(column, toInsert);
            underlyingQueenBoard[row][column] = toInsert;
        }
        this.underlying = underlyingQueenBoard;
        this.columnToQueens = toSetHashMap;
    }


    @Override
    public String toString() {
        StringBuilder toReturn = new StringBuilder();
        for (int i = 0; i < underlying.length; i++) {
            for (int j = 0; j < underlying[i].length; j++) {
                toReturn.append((underlying[i][j] == null ? " - " : " Q "));
            }
            toReturn.append("\n");
        }
        return toReturn.toString();
    }


    public Integer getEvaluationCost() {
        if (evaluationCost == null) {
            evaluationCost = calculateNumberOfAttackingPairsOfQueens();
        }
        return evaluationCost;
    }

    /**
     * calculates the number of attacking
     *
     * @return
     */
    private int calculateNumberOfAttackingPairsOfQueens() {
        ArrayList<HashSet<Position>> attackedPositions = new ArrayList<>();
        for (Integer integer : columnToQueens.keySet()) {
            attackedPositions.add(columnToQueens.get(integer).getAllAttackingPositions());
        }
        int count = 0;
        for (int i = 0; i < attackedPositions.size(); i++) { //I is the current queen i'm considering
            HashSet<Position> allThingsCurrentQueenIsAttacking = attackedPositions.get(i);
            for (int j = 0; j < attackedPositions.size(); j++) {
                if (i == j) {
                    continue;
                }
                if (allThingsCurrentQueenIsAttacking.contains(columnToQueens.get(j).getPosition())) {
                    count++;
                }
            }
        }
        if (count % 2 != 0) {
            throw new RuntimeException("count is not even");
        }
        count /= 2;
        return count;
    }

    private ArrayList<Board> getChildren() {
        ArrayList<Board> toReturn = new ArrayList<>();
        for (int column = 0; column < dimension; column++) { //for each column in the board
            Queen currentQueen = columnToQueens.get(column);
            Position currentQueenPosition = currentQueen.getPosition();
            int currentRow = currentQueenPosition.getRowNumber();
            for (int row = 0; row < dimension; row++) {
                if (currentRow == row) {
                    continue;
                }
                toReturn.add(new Board(this,
                        new Move(currentQueenPosition, new Position(row, currentQueenPosition.getColumnNumber(), dimension))));
            }
        }
        if (toReturn.size() != dimension * (dimension - 1)) {
            throw new RuntimeException("getchildren's dimension is wrong");
        }
        return toReturn;
    }

    public Board getLowestHeuristicChild() {
        ArrayList<Board> children = getChildren();
        Board min = children.get(0);
        for (Board child : children) {
            if (child.getEvaluationCost() < min.getEvaluationCost()) {
                min = child;
            }
        }
        return min;
    }


//    public ArrayList<Board> generateChildren() {
//        Tuple zeroPosition = getZeroPosition();
//        ArrayList<Board> children = new ArrayList<>();
//
//        Tuple leftSwap = zeroPosition.add(new Tuple(0, -1, dimension));
//        if (leftSwap.isValid()) {
//            children.add(generateChildBoard(performSwap(zeroPosition, leftSwap), BoardAction.LeftSwap));
//        }
//
//        Tuple rightSwap = zeroPosition.add(new Tuple(0, 1, dimension));
//        if (rightSwap.isValid()) {
//            children.add(generateChildBoard(performSwap(zeroPosition, rightSwap), BoardAction.RightSwap));
//        }
//
//        Tuple downSwap = zeroPosition.add(new Tuple(1, 0, dimension));
//        if (downSwap.isValid()) {
//            children.add(generateChildBoard(performSwap(zeroPosition, downSwap), BoardAction.DownSwap));
//        }
//
//        Tuple topSwap = zeroPosition.add(new Tuple(-1, 0, dimension));
//        if (topSwap.isValid()) {
//            children.add(generateChildBoard(performSwap(zeroPosition, topSwap), BoardAction.TopSwap));
//        }
//        return children;
//    }
//
//
//    //deep copies underyling, performs the enumerated action on the copy and returns it
//    private ArrayList<ArrayList<Integer>> performSwap(Tuple startCoord, Tuple endCoord) {
//        ArrayList<ArrayList<Integer>> toReturn = gridCloner(underlying);
//        int start = toReturn.get(startCoord.rowNumber).get(startCoord.columnNumber);
//        int end = toReturn.get(endCoord.rowNumber).get(endCoord.columnNumber);
//
//        //set start to end
//        toReturn.get(startCoord.rowNumber).set(startCoord.columnNumber, end);
//        //set end to start
//        toReturn.get(endCoord.rowNumber).set(endCoord.columnNumber, start);
//        return toReturn;
//    }
//
//
//    //uses shallow copy
//    private Board generateChildBoard(ArrayList<ArrayList<Integer>> input, BoardAction parentAction) {
//        return new Board(dimension, input, this, cost + 1, heuristicDelegate.calculateHeuristic(input), parentAction, heuristicDelegate);
//    }
//
//


    private static ArrayList<ArrayList<Integer>> gridCloner(ArrayList<ArrayList<Integer>> input) {
        ArrayList<ArrayList<Integer>> toReturn = new ArrayList<>();
        for (int i = 0; i < input.size(); i++) {
            toReturn.add((ArrayList<Integer>) input.get(i).clone());
        }
        return toReturn;
    }


}
