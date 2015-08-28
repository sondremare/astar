package puzzles.navigation;

import search.Heuristic;
import search.State;

public class ManhattanDistance implements Heuristic {
    @Override
    public float calculateHeuristicValue(State state) {
        return 0;
    }
}
