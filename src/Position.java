/**
 * Created by tibblist on 2/20/2017.
 */
public class Position {

    protected int x;
    protected int y;
    protected double heuristicCost;
    protected double stepsBefore;
    protected double finalCost = 0;
    protected boolean blocked;
    protected Position parent;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public double calculateDistanceTo(Position end) {
        int base = x - end.x;
        int height = y - end.y;
        return Math.hypot(base,height);
    }

    @Override
    public String toString () {
        return "x=" + x + ", y=" + y;
    }
}
