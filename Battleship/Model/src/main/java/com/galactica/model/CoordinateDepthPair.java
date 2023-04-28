package com.galactica.model;

import com.github.cliftonlabs.json_simple.JsonObject;

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

    public JsonObject toJsonObject() {
        JsonObject jo = new JsonObject();
        jo.put("coordinate", coordinate.toJsonObject());
        jo.put("depth", depth);
        return jo;
    }
}