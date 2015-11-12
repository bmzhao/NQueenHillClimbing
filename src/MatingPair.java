import java.util.ArrayList;
import java.util.Random;

/**
 * Created by brianzhao on 11/11/15.
 */
public class MatingPair {
    private static final Random rand = new Random();
    Board guy;
    Board girl;

    public MatingPair(Board girl, Board guy) {
        this.girl = girl;
        this.guy = guy;
    }

    public Board getGirl() {
        return girl;
    }

    public Board getGuy() {
        return guy;
    }

    public Board[] getTwins(Double mutationProbability) {
        if (guy.getDimension() != girl.getDimension()) {
            throw new RuntimeException("Incorrect number of chromosomes");
        }
        int dimension = guy.getDimension();
        /**
         * guarantee some proportion of parent is kept in child
         */
        int cutOff = 1 + rand.nextInt(dimension - 2);


        ArrayList<Integer> guyFirstHalf = new ArrayList<>(guy.getBoardState().subList(0, cutOff));
        ArrayList<Integer> guySecondHalf = new ArrayList<>(guy.getBoardState().subList(cutOff, dimension));

        ArrayList<Integer> girlFirstHalf = new ArrayList<>(girl.getBoardState().subList(0, cutOff));
        ArrayList<Integer> girlSecondHalf = new ArrayList<>(girl.getBoardState().subList(cutOff, dimension));


        Board[] twins = new Board[2];
        guyFirstHalf.addAll(girlSecondHalf);
        girlFirstHalf.addAll(guySecondHalf);


        if (mutationProbability != null) {
            //consider each position in the guy
            for (int i = 0; i < dimension; i++) {
                if (rand.nextDouble() < mutationProbability) {
                    guyFirstHalf.set(i, rand.nextInt(dimension));
                }
                if (rand.nextDouble() < mutationProbability) {
                    girlFirstHalf.set(i, rand.nextInt(dimension));
                }
            }
        }

        twins[0] = new Board(guyFirstHalf);
        twins[1] = new Board(girlFirstHalf);
        return twins;
    }
}
