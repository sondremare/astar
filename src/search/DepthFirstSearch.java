package search;

import java.util.ArrayList;

public class DepthFirstSearch extends Search{

    public DepthFirstSearch(Puzzle puzzle) {
        super(puzzle);
    }

    @Override
    public Node popNode(ArrayList<Node> nodes) {
        return nodes.get(nodes.size() - 1);
    }
}
