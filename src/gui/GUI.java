package gui;

import javafx.scene.layout.AnchorPane;
import search.Search;

public interface GUI {

    abstract AnchorPane initGUI();
    abstract void update(Search search);
}
