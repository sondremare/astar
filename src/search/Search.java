package search;

import java.util.*;

public class Search {
    private Problem problem;
    private boolean shouldDrawOpenAndClosedNodes;

    public BreadthFirstSearch(Problem problem, boolean shouldDrawOpenAndClosedNodes) {
        this.problem = problem;
        this.shouldDrawOpenAndClosedNodes = shouldDrawOpenAndClosedNodes;
    }

    public Problem getProblem() {
        return this.problem;
    }

    /** Used to decide if the GUI component should draw the open and closed nodes
     * TODO: move GUI code out of this class */
    public boolean isShouldDrawOpenAndClosedNodes() {
        return this.shouldDrawOpenAndClosedNodes;
    }

    /** Used to attach a node to a given parent, and calculate its G, H and F-values */
    public void AttachAndEval(SearchNode successor, SearchNode parent) {
        Heuristic heuristic = problem.getHeuristic();
        successor.setParent(parent);
        successor.setG(parent.getG() + successor.getState().getGCost());
        successor.setH(heuristic.calculateHeuristic(successor.getState()));
        successor.setF(successor.getG() + successor.getH());
    }

    /** If a "better" parent node is found for a given node, we set this new "better" node as parent,
     * recalculate the F and G values. This is done recursively for all nodes' children, and their children.
     */
    public void PropagatePathImprovements(SearchNode successor) {
        for (SearchNode kid : successor.getKids()) {
            if (successor.getG() + kid.getState().getGCost() < kid.getG()) {
                kid.setParent(successor);
                kid.setG(successor.getG() + kid.getState().getGCost());
                kid.setF(kid.getG() + kid.getH());
                PropagatePathImprovements(kid);
            }
        }
    }

    /** Generates all possible neighbouring nodes from a current node,
     * based on a set of problem specific actions, and a problem specific result function
     */
    public ArrayList<SearchNode> generateAllSuccessors(SearchNode currentSearchNode, Problem problem) {
        ArrayList<SearchNode> children = new ArrayList<SearchNode>();
        for (Action action : Action.values()) {
            ResultFunction resultFunction = problem.getResultFunction();
            GridMap state = resultFunction.result(action, currentSearchNode.getState());
            if (state != null) {
                SearchNode childSearchNode = new SearchNode(state, currentSearchNode.getG(), problem.getHeuristic().calculateHeuristic(state));
                children.add(childSearchNode);
            }
        }
        return children;
    }

    /** Used to retrieve the next node to evaluate from the list of open nodes **/
    public SearchNode popNode(ArrayList<SearchNode> nodes) {
        return nodes.get(0);
    }

    /** This function tries to find a solution to a given problem using search */
    public void search() throws Exception {

        /** Initialization of the state, lists, and search node */
        Problem problem = getProblem();
        GridMap map = problem.getGridMap();
        map.setCurrentPosition(map.getStart());
        HashMap<Integer, ArrayList<ArrayList<Character>>> uniqueStates = new HashMap<Integer, ArrayList<ArrayList<Character>>>();
        ArrayList<SearchNode> closed = new ArrayList<SearchNode>();
        ArrayList<SearchNode> open = new ArrayList<SearchNode>();
        SearchNode initialSearchNode = new SearchNode(map, 0, problem.getHeuristic().calculateHeuristic(map));
        initialSearchNode.open();
        open.add(initialSearchNode);
        uniqueStates.put(map.getCurrentPosition().getX()*100+map.getCurrentPosition().getY(), map.getState());

        while (!open.isEmpty()) {
            SearchNode currentSearchNode = popNode(open);
            open.remove(currentSearchNode);
            currentSearchNode.close();
            closed.add(currentSearchNode);

            /** We check if the current search node is in the desired goal state */
            if (problem.getGoalTest().isGoalState(currentSearchNode.getState())) {
                SearchNode parent = currentSearchNode;
                ArrayList<Position> solutionChain = new ArrayList<Position>();
                /** We iterate backwards from the goal state via the nodes' parents to find a chain of nodes
                 * which is the solution to the problem
                 */
                while (parent != null) {
                    solutionChain.add(solutionChain.size(), parent.getState().getCurrentPosition());
                    parent = parent.getParent();
                }

                /** After the solution is found, we draw it visually */
                GUI.createAndShowGUI(currentSearchNode.getState().getState(), solutionChain, open, closed, isShouldDrawOpenAndClosedNodes());
                break;
            }

            /** We generate all neighbouring/successor nodes of the current node */
            ArrayList<SearchNode> successors = generateAllSuccessors(currentSearchNode, problem);
            for (SearchNode successor : successors) {
                /** If the successor node has a unique state, we add it to the list of unique states
                 * if not, we fetch the node from either the OPEN list or the CLOSED list.
                 */
                if (uniqueStates.get(successor.getState().getCurrentPosition().getX()*100+successor.getState().getCurrentPosition().getY()) == null) {
                    uniqueStates.put(successor.getState().getCurrentPosition().getX()*100+successor.getState().getCurrentPosition().getY(), map.getState());
                } else {
                    for (SearchNode closedNode : closed) {
                        if (closedNode.getState().getCurrentPosition().equals(successor.getState().getCurrentPosition())) {
                            successor = closedNode;
                        }
                    }
                    for (SearchNode openNode : open) {
                        if (openNode.getState().getCurrentPosition().equals(successor.getState().getCurrentPosition())) {
                            successor = openNode;
                        }
                    }
                }
                currentSearchNode.getKids().add(successor);

                if (successor.getStatus() != Status.OPEN && successor.getStatus() != Status.CLOSED) {
                    AttachAndEval(successor, currentSearchNode);
                    successor.open();
                    open.add(successor);
                    /** We check if the current search node is a "better" parent node to the successor node
                     * than that previous parent node */
                } else if (currentSearchNode.getG() + successor.getState().getGCost() < successor.getG()) {
                    AttachAndEval(successor, currentSearchNode);
                    /** If the successor node is closed, we need to propagate this "better" parent to all of its
                     * successor nodes */
                    if (successor.getStatus() == Status.CLOSED) {
                        PropagatePathImprovements(successor);
                    }
                }
            }
        }
    }
}
