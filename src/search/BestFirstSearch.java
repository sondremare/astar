package search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class BestFirstSearch extends Search {

    public BestFirstSearch(Puzzle puzzle) {
        super(puzzle);
    }

    /**
     * Overrides the popNode method from BreadthFirstSearch and adds sorting of OPEN nodes
     * based primarily on lowest F-value, and secondarily on lowest heuristical value.
     * @param nodes - the list of currently open nodes.
     * @return ascending sorted list.
     */
    @Override
    public Node popNode(ArrayList<Node> nodes) {
        Comparator<Node> lowestFAndHValues = new Comparator<Node>() {

            @Override
            public int compare(Node node1, Node node2) {
                if (node1.getF() > node2.getF()) return 1;
                else if (node1.getF() < node2.getF()) return -1;
                else {
                    if (node1.getH() > node2.getH()) return 1;
                    else if (node1.getH() < node2.getH()) return -1;
                    else return 0;
                }
            }
        };

        Collections.sort(nodes, lowestFAndHValues);
        return nodes.get(0);
    }
}
