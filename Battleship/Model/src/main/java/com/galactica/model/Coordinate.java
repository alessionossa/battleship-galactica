package com.galactica.model;

import java.math.BigDecimal;
import com.github.cliftonlabs.json_simple.JsonObject;
import org.apache.commons.lang3.builder.HashCodeBuilder;

// Send new coordinate to player
public class Coordinate {
    private char x;
    private int y;
    private static int maxValue;

    // Set the maximum value for the coordinate axis
    public static final void setMaxValue(int maxValue) {
        Coordinate.maxValue = maxValue;
    }

    // Constructor to initialize the Coordinate with x and y values
    public Coordinate(char x, int y) {
        this.x = x;
        this.y = y;
    }

    // Getter for the x value
    public char getX() {
        return x;
    }

    // Getter for the y value
    public int getY() {
        return y;
    }

    // Move the coordinate up by the specified distance
    public Coordinate up(int distance) throws OutOfBoundsException {
        if (y > 0)
            return new Coordinate(x, y - distance);
        else
            throw new OutOfBoundsException("Cannot go up");
    }

    // Move the coordinate down by the specified distance
    public Coordinate down(int distance) throws OutOfBoundsException {
        if (y < (maxValue - 1))
            return new Coordinate(x, y + distance);
        else
            throw new OutOfBoundsException("Cannot go down");
    }

    // Move the coordinate left by the specified distance
    public Coordinate left(int distance) throws OutOfBoundsException {
        if (x > 'a')
            return new Coordinate((char) (x - distance), y);
        else
            throw new OutOfBoundsException("Cannot go left");
    }

    // Move the coordinate right by the specified distance
    public Coordinate right(int distance) throws OutOfBoundsException {
        if (x < (char) ('a' + (maxValue - 1)))
            return new Coordinate((char) (x + distance), y);
        else
            throw new OutOfBoundsException("Cannot go right");
    }

    // Calculate the distance between this Coordinate and the given Coordinate
    public int distance(Coordinate coordinate) {
        return Math.abs(x - coordinate.x) + Math.abs(y - coordinate.y);
    }

    // Override equals() method to compare values of x and y fields
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Coordinate that = (Coordinate) obj;
        return x == that.x && y == that.y;
    }

    // Override hashCode() method to generate unique hash code based on values of x
    // and y fields
    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(x).append(y).toHashCode();
    }

    // Method for converting the Coordinate to a JsonObject
    public JsonObject toJsonObject() {
        JsonObject jo = new JsonObject();
        jo.put("x", String.valueOf(x));
        jo.put("y", y);
        jo.put("maxValue", maxValue);
        return jo;
    }

    // Method for creating a Coordinate object from a JsonObject
    public static Coordinate fromJsonObject(JsonObject jo) {
        char x = ((String) jo.get("x")).charAt(0);
        int y = ((BigDecimal) jo.get("y")).intValue();

        return new Coordinate(x, y);
    }
}
