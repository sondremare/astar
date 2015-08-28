package search;

import java.util.ArrayList;

public interface Puzzle {

    abstract Heuristic getHeuristic();
    abstract Action[] getActions();
    abstract ResultFunction getResultFunction();
    abstract State getState();
    abstract GoalTest getGoalTest();


}
