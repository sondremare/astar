package puzzles.navigation;

import search.*;

public class NavigationPuzzle implements Puzzle {
    private ManhattanDistance heuristic;
    private NavigationResultFunction resultFunction;
    private GridState state;
    private NavigationGoalTest goalTest;

    public NavigationPuzzle(GridState state) {
        this.state = state;
        this.heuristic = new ManhattanDistance();
        this.resultFunction = new NavigationResultFunction();
        this.goalTest = new NavigationGoalTest();
    }

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
