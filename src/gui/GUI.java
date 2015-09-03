package gui;

import javafx.scene.layout.GridPane;
import search.Search;

public interface GUI {

    abstract GridPane initGUI();
    abstract void update(Search search);
}
