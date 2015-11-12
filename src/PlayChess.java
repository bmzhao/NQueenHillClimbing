import java.util.ArrayList;

/**
 * Created by brianzhao on 11/11/15.
 */
public class PlayChess {
    public static void main(String[] args) {
        int successCount = 0;
        for (int i = 0; i < 1000; i++) {
            Board board = new Board(8);
            int currentHeuristic = board.getEvaluationCost();

            boolean done = false;
            int counter = 0;
            while (!done) {
                if (currentHeuristic == 0) {
                    successCount++;
                    done = true;
                    break;
                }
                Board bestChild = board.getLowestHeuristicChild();
                if (currentHeuristic <= bestChild.getEvaluationCost()) {
//                    System.out.println("You did not get to the finished state");
                    break;
                } else {
                    currentHeuristic = bestChild.getEvaluationCost();
                    board = bestChild;
                }
//                }else if (currentHeuristic == bestChild.getEvaluationCost()) {
//                    if (counter == 100) {
//                        System.out.println("Got stuck at a permanent plateau");
//                        break;
//                    }
//                    counter++;
//                } else {
//                    currentHeuristic = bestChild.getEvaluationCost();
//                    counter = 0;
//                }
            }
            if (done) {
                System.out.println("YAY");
                System.out.println(board.toString());
            }
        }
        System.out.println(successCount);
    }
}
