package com.galactica.model;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import com.github.cliftonlabs.json_simple.JsonObject;

// Class representing a pair of a Coordinate and an integer depth value
public class CoordinateDepthPair {
    private Coordinate coordinate;
    private int depth;

    // Constructor to initialize the CoordinateDepthPair with a Coordinate and depth
    // value
    public CoordinateDepthPair(Coordinate coordinate, int depth) {
        this.coordinate = coordinate;
        this.depth = depth;
    }

    // Getter for the coordinate
    public Coordinate getCoordinate() {
        return coordinate;
    }

    // Getter for the depth
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

    // Override hashCode() method to generate unique hash code based on values of
    // coordinate and depth fields
    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(coordinate)
                .append(depth)
                .toHashCode();
    }

    // Method for converting the CoordinateDepthPair to a JsonObject
    public JsonObject toJsonObject() {
        JsonObject jo = new JsonObject();
        jo.put("coordinate", coordinate.toJsonObject());
        jo.put("depth", depth);
        return jo;
    }

    // Method for creating a CoordinateDepthPair object from a JsonObject
    public static CoordinateDepthPair fromJsonObject(JsonObject jo) {
        Coordinate coordinate = Coordinate.fromJsonObject((JsonObject) jo.get("coordinate"));
        int depth = ((BigDecimal) jo.get("depth")).intValue();

        return new CoordinateDepthPair(coordinate, depth);
    }
}
