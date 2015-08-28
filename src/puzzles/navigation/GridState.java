package puzzles.navigation;

import search.State;

import java.util.ArrayList;

public class GridState implements State {
    private ArrayList<ArrayList<Character>> mapRepresentation;
    private int height;
    private int width;
    private Position start;
    private Position goal;
    private Position current;
    private static final char BLOCKED_NODE = 'B'; //TODO

    public GridState(GridState gridState) {
        this(gridState.getMapRepresentation(), gridState.getHeight(), gridState.getWidth(), gridState.getCurrentPosition(), gridState.getGoalPosition(), gridState.getStartPosition());
    }

    public GridState(ArrayList<ArrayList<Character>> mapRepresentation, int height, int width, Position currentPosition, Position goalPosition, Position startPosition) {
        this.mapRepresentation = mapRepresentation;
        this.height = height;
        this.width = width;
        this.current = currentPosition;
        this.goal = goalPosition;
        this.start = startPosition;
    }

    public Position getStartPosition() {
        return start;
    }

    public Position getGoalPosition() {
        return goal;
    }

    public Position getCurrentPosition() {
        return current;
    }

    public void setCurrentPosition(Position current) {
        this.current = current;
    }

    public ArrayList<ArrayList<Character>> getMapRepresentation() {
        return mapRepresentation;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    @Override
    public void initialize() {
        setCurrentPosition(getStartPosition());
    }

    @Override
    public float getGCost() {
        return 1;
    }

    @Override
    public boolean isGoalState(State state) {
        GridState gridState = (GridState) state;
        return gridState.getGoalPosition().equals(gridState.getCurrentPosition());
    }

    @Override
    public boolean equals(State state) {
        GridState gridState = (GridState) state;
        return getCurrentPosition().equals(gridState.getCurrentPosition());
    }



    public boolean isBlocked(Position position) {
        return mapRepresentation.get(position.getX()).get(position.getY()) == BLOCKED_NODE;
    }

    public boolean isPositionWithinBounds(Position position) {
        return (position.getX() >= 0 && position.getX() < this.height
                && position.getY() >= 0 && position.getY() < this.width);
    }

    public boolean isValidPosition(Position position) {
        return isPositionWithinBounds(position) && !isBlocked(position);
    }

    public void move(Position newPosition) {
        setCurrentPosition(newPosition);
    }

    @Override
    public int hashCode() {
        return getCurrentPosition().getX()*100 + getCurrentPosition().getY();
    }
}
