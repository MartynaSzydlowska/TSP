package com.company;

public class GreedyPoint extends Point {

    private boolean visited;

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public GreedyPoint(int number, int x, int y, boolean visited) {
        super(number, x, y);
        this.visited = visited;
    }

    @Override
    public String toString() {
        return "GreedyPoint{" + "number=" + getNumber()
                + " x= " + getX() +
                " y=" + getY() +
                " visited=" + visited +
                '}';
    }
}
