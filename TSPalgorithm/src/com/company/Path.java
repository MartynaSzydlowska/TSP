package com.company;

import java.util.ArrayList;
import java.util.List;

public class Path {

    private double distance;
    private ArrayList<Point> path;

    public Path(double distance, ArrayList<Point> path) {
        this.distance = distance;
        this.path = path;
    }


    public Path(double distance, List<Point> firstParentHalf) {
    }



    public Path(ArrayList<Point> path) {
        this.path = path;
    }

    public Path() {

    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public ArrayList<Point> getPath() {
        return path;
    }

    public void setPath(ArrayList<Point> path) {
        this.path = path;
    }


    public boolean equals(Object obj) {
        if(obj == this){
            return true;
        }
        if(!(obj instanceof Path)){
            return false;
        }
        Path other = (Path) obj;
        return this.path.equals(other.path);
    }

    public int hashCode(){
        return path.hashCode();
    }

    //
//    public double calculatePath(ArrayList<Point> list){
//        double distance = 0;
//        for (int i=0; i<path.size();i++){
//            distance += Math.sqrt((list.get(i).getX()-list.get(i+1).getX())*(list.get(i).getX()-list.get(i+1).getX())+(list.get(i).getY()-list.get(i+1).getY())*(list.get(i).getY()-list.get(i+1).getY()));
//        }
//        distance+=Math.sqrt((list.get(0).getX()-list.get(path.size()-1).getX())*(list.get(0).getX()-list.get(path.size()-1).getX())+(list.get(0).getY()-list.get(path.size()-1).getY())*(list.get(0).getY()-list.get(path.size()-1).getY()));
//
//        return this.distance;
//    }

    @Override
    public String toString() {
        return "Path{" +
                "distance=" + distance +
                ", path=" + getPath() +
                '}';
    }

}
