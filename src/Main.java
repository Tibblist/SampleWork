import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {


    public static void main(String[] args) {

        int fileNumber = 1;

        Grid grid = parseJson(fileNumber);

        Position[][] map = populateMap(grid);

        aStarSearch(map, grid);

        printResults(map[grid.getEnd().getX()][grid.getEnd().getY()]);

    }
    /**
     * This function will parse the file number specified into the specified objects
     * @param  fileNumber  number of the file to be used
     * @return      the object created by the json file
     */
    public static Grid parseJson(int fileNumber) {

        Gson gson = new Gson();
        String json = "";

        try {

            switch (fileNumber) {

                case 1: json = new Scanner(new File("json")).useDelimiter("\\A").next();
                    break;

                case 2: json = new Scanner(new File("json2")).useDelimiter("\\A").next();
                    break;
            }

        } catch (JsonSyntaxException e) {

            System.out.println(e);

        } catch(FileNotFoundException e) {

            System.out.println(e);

        }

        Grid grid = null;

        try {

            grid = gson.fromJson(json,Grid.class);

        } catch (com.google.gson.JsonSyntaxException e){

            System.out.println(e);

        }

        return grid;
    }
    /**
     * Creates a blank map then adds in the obstacles based on the grid object
     * @param  grid  The grid object to use to populate the map
     * @return      the map that has been created with obstacles from the grid
     */
    public  static Position[][] populateMap(Grid grid) {

        Position[][] output = generateBlankMap(grid.getDimension());

        output[grid.getStart().getX()][grid.getStart().getY()].stepsBefore = 0;

        for (int x = 0; x < output.length; x++) {

            for (int y = 0; y < output[x].length; y++) {
                if (grid.isObstacleAt(x, y)) {

                    output[x][y].blocked = true;
                } else {

                    output[x][y].heuristicCost = output[x][y].calculateDistanceTo(grid.getEnd());
                }
            }
        }

        return output;
    }
    /**
     * Creates a blank map
     * @param  dimensions  Dimensions of the map
     * @return      Blank map
     */
    public static Position[][] generateBlankMap(int dimensions) {

        Position[][] output = new Position[dimensions][dimensions];

        for (int x = 0; x < output.length; x++) {

            for (int y = 0; y < output[x].length; y++) {

                Position position = new Position();
                position.x = x;
                position.y = y;
                output[x][y] = position;
            }
        }

        return output;
    }
    /**
     * This performs the search using the A* algorithim to find the best path
     * @param  map  The map to navigate
     */
    public static void aStarSearch(Position[][] map, Grid grid) {

        ArrayList<Position> openSet = new ArrayList<>();
        Set closedSet = new HashSet();

        Position current = map[grid.getStart().getX()][grid.getStart().getY()];

        openSet.add(map[grid.getStart().getX()][grid.getStart().getY()]);

        boolean running = true;
        while(running) {

            current = findLowestFinalValue(openSet);
            openSet.remove(current);
            openSet = populateOpenSet(openSet, current, map, closedSet);

            if (openSet.contains(map[grid.getEnd().getX()][grid.getEnd().getY()])) {
                running = false;
            }

            closedSet.add(current);
        }
    }
    /**
     * Finds the lowest finalcost of the given set of positons
     * @param  list  list of positions to check
     * @return      the position with the lowest Finalcost value
     */
    public static Position findLowestFinalValue(ArrayList<Position> list) {

            Position lowestValue = list.get(0);

        for (Position position : list) {

            if (position.finalCost < lowestValue.finalCost) {

                lowestValue = position;
            }
        }

        return lowestValue;
    }
    /**
     * adds all possible moves that are not already in closed set to the open set
     * @param  openSet  set of positions that still need to be checked
     * @param current  The current position in the map
     * @param map  The map to use to add positions to openset
     * @param closedSet  set of positions already checked
     * @return      the new openset
     */
    public static ArrayList<Position> populateOpenSet(ArrayList<Position> openSet, Position current, Position[][] map, Set closedSet) {

        int x = current.getX();
        int y = current.getY();

        Position toRight = null;
        Position above = null;
        Position toLeft = null;
        Position below = null;

        if (x + 1 < map.length) toRight = map[x + 1][y];
        if (y + 1 < map[current.getX()].length) above = map[x][y + 1];
        if (x - 1 >= 0) toLeft = map[x - 1][y];
        if (y - 1 >= 0) below = map[x][y - 1];


        if (toRight != null && !toRight.blocked && !closedSet.contains(toRight)) {

            processPosition(current, toRight, openSet);
            if (!openSet.contains(toRight)) openSet.add(toRight);
        }

        if (above != null && !above.blocked && !closedSet.contains(above)) {

            processPosition(current, above, openSet);
            if (!openSet.contains(above)) openSet.add(above);
        }

        if (toLeft != null && !toLeft.blocked && !closedSet.contains(toLeft)) {

            processPosition(current, toLeft, openSet);
            if (!openSet.contains(toLeft)) openSet.add(toLeft);
        }

        if (below != null && !below.blocked && !closedSet.contains(below)) {

            processPosition(current, below, openSet);
            if (!openSet.contains(below)) openSet.add(below);
        }

        return openSet;
    }

    public static void processPosition(Position current, Position next, ArrayList<Position> openSet) {

        if (!openSet.contains(next)) {

            next.stepsBefore = current.stepsBefore + 1;
            next.parent = current;
            next.finalCost = next.stepsBefore + next.heuristicCost;

        }else if (next.finalCost != 0 && next.finalCost >= current.stepsBefore + 1 + next.heuristicCost) {

            next.stepsBefore = current.stepsBefore + 1;
            next.parent = current;
            next.finalCost = next.stepsBefore + next.heuristicCost;
        }
    }

    public static void printResults(Position position) {

        while(position.parent != null) {

            System.out.println(position.toString());
            position = position.parent;
        }
    }

}
