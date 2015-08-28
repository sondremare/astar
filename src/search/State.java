package search;

public interface State {
    abstract void initialize();
    abstract float getGCost();
    abstract boolean isGoalState(State state);
    abstract boolean equals(State state);
}
