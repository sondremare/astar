package gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import puzzles.navigation.GridState;
import puzzles.navigation.NavigationPuzzle;
import puzzles.navigation.gui.NavigationGUI;
import puzzles.navigation.io.file.GridReader;
import search.*;

import java.io.File;

public class Main extends Application {

    private NavigationPuzzle puzzle;
    private GUI gui;
    private Search search;

    @Override
    public void start(Stage primaryStage) throws Exception {

        final FileChooser fileChooser = new FileChooser();

        Button openFileButton = new Button("Choose file");
        Button startSearchButton = new Button("Start Search");
        startSearchButton.setDisable(true);

        Label sleepTimeLabel = new Label ("Sleep (ms): ");
        final TextField sleepTimeInput = new TextField();
        sleepTimeInput.setMaxWidth(50);
        sleepTimeInput.setText("50");

        ToggleGroup searchType = new ToggleGroup();
        RadioButton bestFirst = new RadioButton("Best-First");
        RadioButton depthFirst = new RadioButton("Depth-First");
        RadioButton breadthFirst = new RadioButton("Breadth-First");
        bestFirst.setToggleGroup(searchType);
        depthFirst.setToggleGroup(searchType);
        breadthFirst.setToggleGroup(searchType);
        bestFirst.setSelected(true);


        GridPane controlPane = new GridPane();
        controlPane.add(openFileButton, 0, 0);
        controlPane.add(bestFirst, 0, 1);
        controlPane.add(depthFirst, 0, 2);
        controlPane.add(breadthFirst, 0, 3);
        controlPane.add(sleepTimeLabel, 0, 4);
        controlPane.add(sleepTimeInput, 1, 4);
        controlPane.add(startSearchButton, 0, 5);

        final GridPane gridPane = new GridPane();
        gridPane.setPrefSize(600, 800);
        gridPane.add(controlPane, 0, 0);

        primaryStage.setScene(new Scene(gridPane));
        primaryStage.show();

        openFileButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                File gridDirectory = new File("D:\\School\\AiProgramming\\astar\\resources\\navigation\\");
                if (gridDirectory != null && gridDirectory.exists()) {
                    fileChooser.setInitialDirectory(gridDirectory);
                }
                File file = fileChooser.showOpenDialog(primaryStage);

                GridState state = GridReader.loadFile(file);
                if (state != null) {
                    puzzle = new NavigationPuzzle(state);
                    gui = new NavigationGUI(puzzle);
                    GridPane guiRoot = gui.initGUI();
                    gridPane.add(guiRoot, 0, 1);
                    startSearchButton.setDisable(false);
                } else {
                    startSearchButton.setDisable(true);
                }

            }
        });

        startSearchButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (search != null) {
                    search.stop();
                }
                Search.sleepTime = Integer.parseInt(sleepTimeInput.getText());
                if (bestFirst.isSelected()) {
                    search = new BestFirstSearch(puzzle);
                } else if (depthFirst.isSelected()) {
                    search = new DepthFirstSearch(puzzle);
                } else if (breadthFirst.isSelected()) {
                    search = new BreadthFirstSearch(puzzle);
                }

                search.initObservableLists();
                search.getObservableClosedList().addListener(new ListChangeListener<Node>() {
                    @Override
                    public void onChanged(Change<? extends Node> c) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                gui.update(search);
                            }
                        });
                    }
                });
                search.getObservableOpenList().addListener((new ListChangeListener<Node>() {
                    @Override
                    public void onChanged(Change<? extends Node> c) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                gui.update(search);
                            }
                        });
                    }
                }));

                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        search.search(gui);
                    }
                }).start();
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
