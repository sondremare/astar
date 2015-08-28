package search;

public interface ResultFunction {

    abstract State result(Action action, State state);
}
