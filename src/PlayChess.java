import java.util.*;

/**
 * Created by brianzhao on 11/11/15.
 */
public class PlayChess {
    private static final int NUM_TEST_CASES = 100;
    private static final int LIMIT_FOR_SIMULATED_ANNEALING = 200;
    private static final Random rand = new Random();

    public static void main(String[] args) {

//        simulatedAnnealing(17,LIMIT_FOR_SIMULATED_ANNEALING);

        System.out.println("Steepest Hill Climbing with No Sideways Move 100 times");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        steepestHillClimbingNoSideWaysMove(17);

        System.out.println("Steepest Hill Climbing with Sideways Move Allowed 100 times");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        steepestHillClimbingWithSidewaysMove(17);

        System.out.println("Genetic Algorithm 10 times with 1000 generation constraint: ");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        GeneticAlgorithm.runGenetic();


    }

    public static void steepestHillClimbingNoSideWaysMove(int dimensionOfBoard) {
        int successCount = 0;
        int stepCount = 0;
        long millisecondsTaken = 0;

        for (int i = 0; i < NUM_TEST_CASES; i++) {
            System.out.println("Problem number: " + i);
            Board board = new Board(dimensionOfBoard);
            boolean done = false;
            long startTime = System.currentTimeMillis();

            while (!done) {
                if (board.getHeuristicAttackingQueens() == 0) {
                    long endTime = System.currentTimeMillis();
                    millisecondsTaken += (endTime - startTime);
                    successCount++;
                    done = true;
                    break;
                }
                stepCount++;
                Board bestChild = board.getLowestHeuristicChild(); //gets the child with minimum number of attacking pairs of queens
                if (board.getHeuristicAttackingQueens() <= bestChild.getHeuristicAttackingQueens()) {
                    long endTime = System.currentTimeMillis();
                    millisecondsTaken += (endTime - startTime);
                    System.out.println("You did not get to the finished state");
                    break;
                } else {
                    board = bestChild;
                }
            }
            if (done) {
                System.out.println("Correctly solved the problem with the following solution: ");
                System.out.println(board.toString());
            }
            System.out.println("\n\n");
        }
        System.out.println("Correctly solved: " + successCount + " out of " + NUM_TEST_CASES);
        System.out.println("Solution percentage is: " + ((successCount * 1.0) / NUM_TEST_CASES * 100 + "%"));
        System.out.println("The average number of steps taken was: " + (stepCount * 1.0) / NUM_TEST_CASES);
        System.out.println("Average seconds to solve or fail to solve each problem: " + ((millisecondsTaken * 1.0) / 1000) / NUM_TEST_CASES);

    }

    public static void steepestHillClimbingWithSidewaysMove(int dimensionOfBoard) {
        int successCount = 0;
        int stepCount = 0;
        long millisecondsTaken = 0;
        for (int i = 0; i < NUM_TEST_CASES; i++) {
            System.out.println("Problem number: " + i);
            Board board = new Board(dimensionOfBoard);
            boolean done = false;
            long startTime = System.currentTimeMillis();
            int counter = 0;
            while (!done) {
                if (board.getHeuristicAttackingQueens() == 0) {
                    long endTime = System.currentTimeMillis();
                    millisecondsTaken += (endTime - startTime);
                    successCount++;
                    done = true;
                    break;
                }
                stepCount++;
                Board bestChild = board.getLowestHeuristicChild();
                if (board.getHeuristicAttackingQueens() < bestChild.getHeuristicAttackingQueens()) {
                    long endTime = System.currentTimeMillis();
                    millisecondsTaken += (endTime - startTime);
                    System.out.println("You did not get to the finished state");
                    break;
                } else if (board.getHeuristicAttackingQueens() == bestChild.getHeuristicAttackingQueens()) {
                    if (counter == 100) {
                        System.out.println("Got stuck at a permanent plateau");
                        break;
                    }
                    board = bestChild;
                    counter++;
                } else {
                    board = bestChild;
                    counter = 0;
                }
            }
            if (done) {
                System.out.println("Correctly solved the problem with the following solution: ");
                System.out.println(board.toString());
            }
        }
        System.out.println("Correctly solved: " + successCount + " out of " + NUM_TEST_CASES);
        System.out.println("Solution percentage is: " + ((successCount * 1.0) / NUM_TEST_CASES * 100 + "%"));
        System.out.println("The average number of steps taken was: " + (stepCount * 1.0) / NUM_TEST_CASES);
        System.out.println("Average seconds to solve or fail to solve each problem: " + ((millisecondsTaken * 1.0) / 1000) / NUM_TEST_CASES);
    }

    /**
     * calculate temperature using logistic growth function
     *
     * @param x
     * @return
     */
    public static double getTemperature(int x) {
        /**
         https://en.wikipedia.org/wiki/Logistic_function
         f(x) = L/ (1 + e ^ (-k ( x - x0) ) )

         x0 = the x-value of the sigmoid's midpoint,

         L = the curve's maximum value, and

         k = the steepness of the curve.

         */
        double L = 300;
//        double k = 0.02;
        double k = 3;
        return L / (1 + Math.exp(k * x));
//        return 300 * Math.pow(0.5, x);
    }

    public static void simulatedAnnealing(int dimensionOfBoard, int limitingNumberOfSteps) {
        int successCount = 0;
        int stepCount = 0;
        long millisecondsTaken = 0;
        for (int i = 0; i < NUM_TEST_CASES; i++) {
            System.out.println("Problem number: " + i);
            Board board = new Board(dimensionOfBoard);
            boolean done = false;
            long startTime = System.currentTimeMillis();
            int currentTime = 0;

            while (!done) {
//                System.out.println(board.getHeuristicAttackingQueens());
                if (board.getHeuristicAttackingQueens() == 0 || currentTime == limitingNumberOfSteps) {
                    long endTime = System.currentTimeMillis();
                    millisecondsTaken += (endTime - startTime);
                    done = true;
                    break;
                }
                stepCount++;
                currentTime++;
                Board randChild = board.getRandomChild();
                if (randChild.getHeuristicAttackingQueens() < board.getHeuristicAttackingQueens()) {
                    board = randChild;
                } else {
                    double temperature = getTemperature(currentTime);
                    double deltaE = Math.abs(randChild.getHeuristicAttackingQueens() - board.getHeuristicAttackingQueens()) * -1;
                    double probability = Math.exp(deltaE / temperature);
                    if (rand.nextDouble() < probability) {
                        board = randChild;
                    }
                }
            }
            if (board.getHeuristicAttackingQueens() == 0) {
                successCount++;
                System.out.println("Correctly solved the problem with the following solution: ");
                System.out.println(board.toString());
            } else {
                System.out.println("Failed to find the solution of the problem in "
                        + limitingNumberOfSteps + " iterations of simulated annealing");
            }
            System.out.println("\n\n");
        }

        System.out.println("Correctly solved: " + successCount + " out of " + NUM_TEST_CASES);
        System.out.println("Solution percentage is: " + ((successCount * 1.0) / NUM_TEST_CASES * 100 + "%"));
        System.out.println("The average number of steps taken was: " + (stepCount * 1.0) / NUM_TEST_CASES);
        System.out.println("Average seconds to solve or fail to solve each problem: " + ((millisecondsTaken * 1.0) / 1000) / NUM_TEST_CASES);
    }

}
