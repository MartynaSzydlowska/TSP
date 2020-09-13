package com.company;

import java.util.ArrayList;

public class Point {
    private int number;
    private int x;
    private int y;

    public Point(int number, int x, int y) {
        this.number = number;
        this.x = x;
        this.y = y;
    }

    public Point() {
    }

    public Point(ArrayList<Point> path) {
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public double calculateDistance(Point city){
        return Math.sqrt(((this.getX() - city.getX()) * (this.getX() - city.getX())) + ((this.getY() - city.getY()) * (this.getY() - city.getY()))
        );
    }

    @Override
    public String toString() {
        return "{" +
                "n=" + number +
                '}';
    }
}
