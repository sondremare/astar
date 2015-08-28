package search;

public interface Heuristic {

    abstract float calculateHeuristicValue(State state);
}
