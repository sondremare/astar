package puzzles.navigation;

import search.Heuristic;
import search.State;

public class ManhattanDistance implements Heuristic {
    @Override
    public float calculateHeuristicValue(State state) {
        if (state instanceof GridState) {
            GridState gridState = (GridState) state;
            Position current = gridState.getCurrentPosition();
            Position goal = gridState.getGoalPosition();
            return (Math.abs(current.getX() - goal.getX()) + Math.abs(current.getY() - goal.getY()));
        }
        return 0;
    }
}
