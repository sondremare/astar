package puzzles.navigation.gui;

import gui.GUI;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import puzzles.navigation.GridState;
import puzzles.navigation.NavigationPuzzle;
import puzzles.navigation.Position;
import search.Node;
import search.Search;

import java.util.ArrayList;

public class NavigationGUI implements GUI{

    private NavigationPuzzle puzzle;
    private static GridPane navigationGrid;

    private final static Image barrierImage = new Image("/navigation/images/barrier.png", true);
    private final static Image goalImage = new Image("/navigation/images/goal.png", true);
    private final static Image startImage = new Image("/navigation/images/start.png", true);
    private final static Image solutionImage = new Image("/navigation/images/solution.png", true);
    private static ImageView[][] referenceArray;
    private static ArrayList<ImageView> solutionReference;
    private final static int CELL_WIDTH = 30;
    private final static int CELL_HEIGHT = 30;


    public NavigationGUI(NavigationPuzzle puzzle) {
        this.puzzle = puzzle;
    }

    @Override
    public AnchorPane initGUI() {
        AnchorPane root = new AnchorPane();
        root.setPrefSize(600, 800);
        GridState state = (GridState) puzzle.getState();
        clearGrid();
        referenceArray = new ImageView[state.getHeight()][state.getWidth()];
        navigationGrid = new GridPane();
        navigationGrid.setPrefSize(600, 800);
        navigationGrid.setGridLinesVisible(true);
        updateImages(state);
        root.getChildren().add(navigationGrid);
        return root;
    }

    @Override
    public void update(Search search) {
        Node parent = search.getCurrentNode();
        if (parent != null) {
            ArrayList<Position> solutionChain = new ArrayList<Position>();
            GridState parentState = (GridState) parent.getState();
            while (parent != null) {
                parentState = (GridState) parent.getState();
                solutionChain.add(solutionChain.size(), parentState.getCurrentPosition());
                parent = parent.getParent();
            }
            if (solutionReference != null) {
                for (ImageView imageView : solutionReference) {
                    navigationGrid.getChildren().remove(imageView);
                }
            } else {
                solutionReference = new ArrayList<>();
            }
            int width = parentState.getWidth();
            for (Position pos : solutionChain) {
                ImageView imageView = new ImageView(solutionImage);
                imageView.setFitWidth(CELL_WIDTH);
                imageView.setFitHeight(CELL_HEIGHT);
                int yPos = width - pos.getY();
                int xPos = pos.getX();
                navigationGrid.add(imageView, xPos, yPos);
                solutionReference.add(imageView);
            }
        }
    }

    public void updateImages(GridState state) {
        char[][] grid = state.getMapRepresentation();
        ImageView imageView = null;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == GridState.BARRIER) {
                    imageView = new ImageView(barrierImage);
                } else if (grid[i][j] == GridState.GOAL) {
                    imageView = new ImageView(goalImage);
                } else if (grid[i][j] == GridState.START) {
                    imageView = new ImageView(startImage);
                } else {
                    imageView = new ImageView();
                }
                imageView.setFitWidth(CELL_WIDTH);
                imageView.setFitHeight(CELL_HEIGHT);
                if (referenceArray[i][j] != null) {
                    navigationGrid.getChildren().remove(referenceArray[i][j]);
                }
                int yPos = grid[i].length - j;
                navigationGrid.add(imageView, i, yPos);
                referenceArray[i][j] = imageView;
            }
        }
    }

    public void clearGrid() {
        if (referenceArray != null) {
            for (int i = 0; i < referenceArray.length; i++) {
                for (int j = 0; j < referenceArray[i].length; j++) {
                    navigationGrid.getChildren().remove(referenceArray[i][j]);
                }
            }
        }
        if (solutionReference != null) {
            for (ImageView imageView : solutionReference) {
                navigationGrid.getChildren().remove(imageView);
            }
        }

    }
}
