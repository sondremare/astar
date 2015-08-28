package puzzles.navigation;

import search.Action;
import search.ResultFunction;
import search.State;

public class NavigationResultFunction implements ResultFunction{
    @Override
    public State result(Action action, State state) {
        GridState gridState = (GridState) state;
        Position currentPosition = gridState.getCurrentPosition();
        Position newPosition;
        NavigationAction navigationAction = (NavigationAction) action;
        if (action == NavigationAction.NORTH) {
            newPosition = new Position(currentPosition.getX(), currentPosition.getY()-1);
            if (gridState.isValidPosition(newPosition)) {
                GridState newGridState = new GridState(gridState);
                newGridState.move(newPosition);
                return newGridState;
            }
        } else if (action == NavigationAction.SOUTH) {
            newPosition = new Position(currentPosition.getX(), currentPosition.getY()+1);
            if (gridState.isValidPosition(newPosition)) {
                GridState newGridState = new GridState(gridState);
                newGridState.move(newPosition);
                return newGridState;
            }
        } else if (action == NavigationAction.WEST) {
            newPosition = new Position(currentPosition.getX()-1, currentPosition.getY());
            if (gridState.isValidPosition(newPosition)) {
                GridState newGridState = new GridState(gridState);
                newGridState.move(newPosition);
                return newGridState;
            }
        } else if (action == NavigationAction.EAST) {
            newPosition = new Position(currentPosition.getX()+1, currentPosition.getY());
            if (gridState.isValidPosition(newPosition)) {
                GridState newGridState = new GridState(gridState);
                newGridState.move(newPosition);
                return newGridState;
            }
        }
        return null;
    }
}
