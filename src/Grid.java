/**
 * Created by tibblist on 2/20/2017.
 */
public class Grid {

    private int dimension;
    private Position start;
    private Position end;
    private Position[] obstacles;

    public int getDimension() {
        return dimension;
    }

    public Position getStart() {
        return start;
    }

    public Position getEnd() {
        return end;
    }

    public Position[] getObstacles() {
        return obstacles;
    }

    public boolean isObstacleAt(int x, int y) {
        for (Position position: obstacles) {
            if (x == position.getX() && y == position.getY()) {
                return true;
            }
        }

        return false;
    }

}
