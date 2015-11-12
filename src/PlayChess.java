/**
 * Created by brianzhao on 11/11/15.
 */
public class PlayChess {
    public static void main(String[] args) {
        int successCount = 0;
        for (int i = 0; i < 1000; i++) {
            Board board = new Board(8);
            boolean done = false;
            int counter = 0;
            while (!done) {
                if (board.getHeuristicAttackingQueens() == 0) {
                    successCount++;
                    done = true;
                    break;
                }
                Board bestChild = board.getLowestHeuristicChild();
                if (board.getHeuristicAttackingQueens() < bestChild.getHeuristicAttackingQueens()) {
//                if (board.getHeuristicAttackingQueens() <= bestChild.getHeuristicAttackingQueens()) {
                    System.out.println("You did not get to the finished state");
                    break;
                }
//                else {
//                    board = bestChild;
//                }

                else if (board.getHeuristicAttackingQueens() == bestChild.getHeuristicAttackingQueens()) {
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
//                System.out.println("YAY");
//                System.out.println(board.toString());
            }
            System.out.println(i);
        }
        System.out.println(successCount);
    }
}
