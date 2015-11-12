import java.util.*;

/**
 * Created by brianzhao on 11/11/15.
 */
public class PlayChess {
    private static final int NUM_TEST_CASES = 1000;
    private static final int LIMIT_FOR_SIMULATED_ANNEALING = 200;

    //gene algo
    private static final int POPULATION_FOR_GENETIC_ALGORITHM = 1000;
    private static final double PROPORTION_OF_POPULATION_TO_MATE = 0.90;
    private static final double PROBABILITY_OF_MUTATION_OF_EACH_POSITION = 0.05;


    private static final Random rand = new Random();

    public static void main(String[] args) {

//        simulatedAnnealing(17,LIMIT_FOR_SIMULATED_ANNEALING);
//        steepestHillClimbingNoSideWaysMove(17);
//        steepestHillClimbingWithSidewaysMove(8);
//        steepestHillClimbingWithSidewaysMove(2);
        geneticAlgorithm(POPULATION_FOR_GENETIC_ALGORITHM,PROPORTION_OF_POPULATION_TO_MATE,PROBABILITY_OF_MUTATION_OF_EACH_POSITION,8);

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
//        double L = 300;
////        double k = 0.02;
//        double k = 3;
//        return L / (1 + Math.exp(k * x));
        return 300 * Math.pow(0.5, x);
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

    public static void geneticAlgorithm(int initialPopulation, double proportionOfPopToMate, Double mutationProbability, int dimensionOfBoard) {
        List<Board> population = new ArrayList<>();
        int totalNumberOfNonAttackingPairs = 0;

        /**
         * magicHashmap maps each possible randomly generated number to a board, obeying the probabilities
         * of the heuristic function for the genetic algorithm
         */
        HashMap<Integer, Board> randomNumberToBoard = new HashMap<>();
        for (int i = 0; i < initialPopulation; i++) {
            Board toAdd = new Board(dimensionOfBoard);
            if (toAdd.getHeuristicAttackingQueens() == 0) {
                System.out.println("Obtained a solution while generating initial population: ");
                System.out.println(toAdd);
                return;
            }
            population.add(toAdd);
        }

        int iterationOfLoop = 0;
        while (true) {
            System.out.println(++iterationOfLoop);
            int currentRandomNumber = 0;
            for (int i = 0; i < initialPopulation; i++) {
                Board toAdd = population.get(i);
                totalNumberOfNonAttackingPairs += toAdd.getHeuristicNonAttackingQueens();
                for (; currentRandomNumber < totalNumberOfNonAttackingPairs; currentRandomNumber++) {
                    randomNumberToBoard.put(currentRandomNumber, toAdd);
                }
            }
            int numberOfMatingIndividuals = (int) (proportionOfPopToMate * initialPopulation);
            //ensure the number of mating individuals is even
            numberOfMatingIndividuals = (numberOfMatingIndividuals % 2 == 0 ? numberOfMatingIndividuals : numberOfMatingIndividuals + 1);

            //generating mating pairs
            ArrayList<MatingPair> allmatings = new ArrayList<>();
            for (int i = 0; i < numberOfMatingIndividuals; i += 2) {
                allmatings.add(
                        new MatingPair(
                                randomNumberToBoard.get(rand.nextInt(totalNumberOfNonAttackingPairs)),
                                randomNumberToBoard.get(rand.nextInt(totalNumberOfNonAttackingPairs)))
                );
            }

            //generating children of mating pairs
            ArrayList<Board> children = new ArrayList<>();
            for (MatingPair matingPair : allmatings) {
                Board[] twins = matingPair.getTwins(mutationProbability);
                if (twins[0].getHeuristicAttackingQueens() == 0) {
                    System.out.println("Obtained solution with child: ");
                    System.out.println(twins[0]);
                    return;
                }
                if (twins[1].getHeuristicAttackingQueens() == 0) {
                    System.out.println("Obtained solution with child: ");
                    System.out.println(twins[1]);
                    return;
                }
                children.add(twins[0]);
                children.add(twins[1]);
            }

            for (Board child : children) {
                population.add(child);
            }

            Collections.sort(population);
            population = population.subList(children.size(), population.size());
            if (population.size() != initialPopulation) {
                throw new RuntimeException("Incorrect Population amount");
            }
            Collections.shuffle(population);
        }
    }
}
