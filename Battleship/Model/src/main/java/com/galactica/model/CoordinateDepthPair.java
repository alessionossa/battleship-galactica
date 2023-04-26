package com.galactica.model;

public class CoordinateDepthPair {
    private Coordinate coordinate;
    private int depth;

    public CoordinateDepthPair(Coordinate coordinate, int depth) {
        this.coordinate = coordinate;
        this.depth = depth;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public int getDepth() {
        return depth;
    }

    public void setCoordinate(Coordinate l) {
        this.coordinate = coordinate;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    @Override
    public boolean equals(Object o) {

        if (o == this) return true;
        if (!(o instanceof CoordinateDepthPair)) return false;

        CoordinateDepthPair coordinateDepthPairObject = (CoordinateDepthPair) o;
        if (coordinateDepthPairObject.getCoordinate().equals(this.getCoordinate()) && (coordinateDepthPairObject.getDepth() == this.getDepth())) {
            return true;
        } else {
            return false;
        }
    }

}