import java.util.*;

/**
 * Created by brianzhao on 11/11/15.
 */
public class GeneticAlgorithm {
    private static final Random rand = new Random();

    //gene algo
    private static final int POPULATION_FOR_GENETIC_ALGORITHM = 100;
    private static final double PROPORTION_OF_POPULATION_TO_MATE = 1;
    private static final double PROBABILITY_OF_MUTATION_OF_EACH_POSITION = 0.01; //for each position

    public static void main(String[] args) {
        geneticAlgorithm(POPULATION_FOR_GENETIC_ALGORITHM, PROPORTION_OF_POPULATION_TO_MATE, PROBABILITY_OF_MUTATION_OF_EACH_POSITION, 17);
    }

    public static void geneticAlgorithm(int initialPopulation, double proportionOfPopToMate, Double mutationProbability, int dimensionOfBoard) {

        List<Board> population = new ArrayList<>();

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
            /**
             * magicHashmap maps each possible randomly generated number to a board, obeying the probabilities
             * of the heuristic function for the genetic algorithm
             */
            HashMap<Integer, Board> randomNumberToBoard = new HashMap<>();
            int totalNumberOfNonAttackingPairs = 0;

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
                System.out.println("Child 1: " + twins[0].getHeuristicAttackingQueens());
//                System.out.println(twins[0]);
                System.out.println("Child 2: " + twins[1].getHeuristicAttackingQueens());
//                System.out.println(twins[1]);

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
