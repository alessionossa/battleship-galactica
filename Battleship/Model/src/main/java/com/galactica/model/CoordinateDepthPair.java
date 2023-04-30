package com.galactica.model;

import java.math.BigDecimal;
import java.util.Objects;

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

    // Override equals() method to compare CoordinateDepthPair objects
    @Override
    public boolean equals(Object o) {

        if (o == this)
            return true;
        if (!(o instanceof CoordinateDepthPair))
            return false;

        CoordinateDepthPair coordinateDepthPairObject = (CoordinateDepthPair) o;
        if (coordinateDepthPairObject.getCoordinate().equals(this.getCoordinate())
                && (coordinateDepthPairObject.getDepth() == this.getDepth())) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(coordinate, depth);
    }

    public JsonObject toJsonObject() {
        JsonObject jo = new JsonObject();
        jo.put("coordinate", coordinate.toJsonObject());
        jo.put("depth", depth);
        return jo;
    }

    public static CoordinateDepthPair fromJsonObject(JsonObject jo) {
        Coordinate coordinate = Coordinate.fromJsonObject((JsonObject) jo.get("coordinate"));
        int depth = ((BigDecimal) jo.get("depth")).intValue();

        return new CoordinateDepthPair(coordinate, depth);
    }
}