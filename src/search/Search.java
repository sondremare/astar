package search;

import gui.GUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.HashMap;

public class Search {
    private Puzzle puzzle;
    private ObservableList<Node> observableClosedList;
    private ObservableList<Node> observableOpenList;
    private ArrayList<Node> closed = new ArrayList<>();
    private ArrayList<Node> open = new ArrayList<>();
    private Node currentNode;

    public static int sleepTime = 5;
    public static boolean shouldStop = false;

    public Search(Puzzle puzzle) {
        this.puzzle = puzzle;
    }

    public void stop() {
        shouldStop = true;
    }

    public Puzzle getPuzzle() {
        return this.puzzle;
    }


    public Node getCurrentNode() {
        return currentNode;
    }

    /** Helper method for real-time drawing the GUI */
    public ObservableList<Node> getObservableClosedList() {
        return observableClosedList;
    }

    public ObservableList<Node> getObservableOpenList() {
        return observableOpenList;
    }

    /** Helper method for real-time drawing the GUI */
    public void initObservableLists() {
        observableClosedList = FXCollections.observableArrayList(closed);
        observableOpenList = FXCollections.observableArrayList(open);
    }

    /** Used to attach a node to a given parent, and calculate its G, H and F-values */
    public void AttachAndEval(Node successor, Node parent) {
        Heuristic heuristic = puzzle.getHeuristic();
        successor.setParent(parent);
        successor.setG(parent.getG() + successor.getState().getGCost());
        successor.setH(heuristic.calculateHeuristicValue(successor.getState()));
        successor.setF(successor.getG() + successor.getH());
    }

    /** If a "better" parent node is found for a given node, we set this new "better" node as parent,
     * recalculate the F and G values. This is done recursively for all nodes' children, and their children.
     */
    public void PropagatePathImprovements(Node parent) {
        for (Node kid : parent.getKids()) {
            if (parent.getG() + kid.getState().getGCost() < kid.getG()) {
                kid.setParent(parent);
                kid.setG(parent.getG() + kid.getState().getGCost());
                kid.setF(kid.getG() + kid.getH());
                PropagatePathImprovements(kid);
            }
        }
    }

    /** Generates all possible neighbouring nodes from a current node,
     * based on a set of puzzle specific actions, and a puzzle specific result function
     */
    public ArrayList<Node> generateAllSuccessors(Node currentNode, Puzzle puzzle) {
        ArrayList<Node> children = new ArrayList<>();
        for (Action action : puzzle.getActions()) {
            ResultFunction resultFunction = puzzle.getResultFunction();
            State state = resultFunction.result(action, currentNode.getState());
            if (state != null) {
                Node childNode = new Node(state, currentNode.getG(), puzzle.getHeuristic().calculateHeuristicValue(state));
                children.add(childNode);
            }
        }
        return children;
    }

    /** Used to retrieve the next node to evaluate from the list of open nodes **/
    public Node popNode(ArrayList<Node> nodes) {
        return nodes.get(0);
    }

    /** This function tries to find a solution to a given puzzle using search */
    public boolean search(GUI gui) {
        shouldStop = false;

        /** Initialization of the state, lists, and search node */
        Puzzle puzzle = getPuzzle();
        State state = puzzle.getState();
        state.initialize();
        HashMap<Integer, State> uniqueStates = new HashMap<>();
        Node initialNode = new Node(state, 0, puzzle.getHeuristic().calculateHeuristicValue(state));
        initialNode.open();
        open.add(initialNode);
        observableOpenList.add(initialNode);
        uniqueStates.put(state.hashCode(), state);

        while (!open.isEmpty()) {
            if (shouldStop) {
                return false;
            }
            currentNode = popNode(open);
            open.remove(currentNode);
            currentNode.close();
            closed.add(currentNode);
            observableClosedList.add(currentNode);

            /** Sleeping to make viewing the GUI visible to the human eye */
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {}

            /** We check if the current search node is in the desired goal state */
            if (puzzle.getGoalTest().isGoalState(currentNode.getState())) {
                break;
            }

            /** We generate all neighbouring/successor nodes of the current node */
            ArrayList<Node> successors = generateAllSuccessors(currentNode, puzzle);
            for (Node successor : successors) {
                /** If the successor node has a unique state, we add it to the list of unique states
                 * if not, we fetch the node from either the OPEN list or the CLOSED list.
                 */
                if (uniqueStates.get(successor.getState().hashCode()) == null) {
                    uniqueStates.put(successor.getState().hashCode(), state);
                } else {
                    boolean found = false;
                    for (Node closedNode : closed) {
                        if (closedNode.getState().equals(successor.getState())) {
                            successor = closedNode;
                            found = true;
                        }
                    }
                    if (!found) {
                        for (Node openNode : open) {
                            if (openNode.getState().equals(successor.getState())) {
                                successor = openNode;
                            }
                        }
                    }

                }
                currentNode.getKids().add(successor);

                if (successor.getStatus() != Node.Status.OPEN && successor.getStatus() != Node.Status.CLOSED) {
                    AttachAndEval(successor, currentNode);
                    successor.open();
                    open.add(successor);
                    observableOpenList.add(successor);
                    /** We check if the current search node is a "better" parent node to the successor node
                     * than that previous parent node */
                } else if (currentNode.getG() + successor.getState().getGCost() < successor.getG()) {
                    AttachAndEval(successor, currentNode);
                    /** If the successor node is closed, we need to propagate this "better" parent to all of its
                     * successor nodes */
                    if (successor.getStatus() == Node.Status.CLOSED) {
                        PropagatePathImprovements(successor);
                    }
                }
            }
        }
        return true;
    }
}
