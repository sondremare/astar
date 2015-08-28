package search;

import java.util.ArrayList;

public class Node {
    State state;
    private float g;
    private float h;
    private float f;
    private Status status;
    private Node parent;
    private ArrayList<Node> kids = new ArrayList<>();

    public Node(State state, float g, float h) {
        this.state = state;
        this.g = g;
        this.h = h;
        this.f = this.g + this.h;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public float getG() {
        return g;
    }

    public void setG(float g) {
        this.g = g;
    }

    public float getH() {
        return h;
    }

    public void setH(float h) {
        this.h = h;
    }

    public float getF() {
        return f;
    }

    public void setF(float f) {
        this.f = f;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void open() {
        setStatus(Status.OPEN);
    }

    public void close() {
        setStatus(Status.CLOSED);
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public ArrayList<Node> getKids() {
        return kids;
    }

    public void setKids(ArrayList<Node> kids) {
        this.kids = kids;
    }

    public enum Status {
        OPEN, CLOSED
    }
}