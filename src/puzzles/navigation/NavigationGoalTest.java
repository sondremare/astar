package puzzles.navigation;

import search.GoalTest;
import search.State;

public class NavigationGoalTest implements GoalTest {
    @Override
    public boolean isGoalState(State state) {
        GridState gridState = (GridState) state;
        return gridState.getGoalPosition().equals(gridState.getCurrentPosition());
    }
}
