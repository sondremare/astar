package gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import puzzles.navigation.GridState;
import puzzles.navigation.NavigationPuzzle;
import puzzles.navigation.gui.NavigationGUI;
import puzzles.navigation.io.file.GridReader;
import search.Node;
import search.Search;

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

        GridPane controlPane = new GridPane();
        controlPane.add(openFileButton, 0, 0);
        controlPane.add(startSearchButton, 1, 0);

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
                    AnchorPane anchorPane = gui.initGUI();
                    gridPane.add(anchorPane, 0, 1);
                    startSearchButton.setDisable(false);
                } else {
                    startSearchButton.setDisable(true);
                }

            }
        });

        startSearchButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                search = new Search(puzzle);
                search.initObservableClosedNodeList();
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
