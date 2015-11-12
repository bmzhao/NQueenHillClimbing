/**
 * Created by brianzhao on 11/11/15.
 */
public class Move {
    private Position startPosition;
    private Position endPosition;

    public Move(Position startPosition, Position endPosition) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
    }

    public Position getEndPosition() {
        return endPosition;
    }

    public Position getStartPosition() {
        return startPosition;
    }

}
