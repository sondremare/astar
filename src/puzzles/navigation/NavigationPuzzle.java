package puzzles.navigation;

import search.*;

import java.util.ArrayList;

public class NavigationPuzzle implements Puzzle {
    private ManhattanDistance heuristic;
    private NavigationResultFunction resultFunction;
    private GridState state;
    private NavigationGoalTest goalTest;

    @Override
    public Heuristic getHeuristic() {
        return heuristic;
    }

    @Override
    public Action[] getActions() {
        return NavigationAction.values();
    }

    @Override
    public ResultFunction getResultFunction() {
        return resultFunction;
    }

    @Override
    public State getState() {
        return state;
    }

    @Override
    public GoalTest getGoalTest() {
        return goalTest;
    }
}
