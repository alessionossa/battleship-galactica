package com.galactica.model;

import java.math.BigDecimal;
import java.util.Objects;

import com.github.cliftonlabs.json_simple.JsonObject;

public class Coordinate { // Send new coordinates to player
    private char x;
    private int y;
    private static int maxValue;

    public static final void setMaxValue(int maxValue) {
        Coordinate.maxValue = maxValue;
    }

    public Coordinate(char x, int y) {
        this.x = x;
        this.y = y;
    }

    public char getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Coordinate up(int distance) throws OutOfBoundsException {
        if (y > 0)
            return new Coordinate(x, y - distance);
        else
            throw new OutOfBoundsException("Cannot go up");
    }

    public Coordinate down(int distance) throws OutOfBoundsException {
        if (y < (maxValue - 1))
            return new Coordinate(x, y + distance);
        else
            throw new OutOfBoundsException("Cannot go down");
    }

    public Coordinate left(int distance) throws OutOfBoundsException {
        if (x > 'a')
            return new Coordinate((char) (x - distance), y);
        else
            throw new OutOfBoundsException("Cannot go left");
    }

    public Coordinate right(int distance) throws OutOfBoundsException {
        if (x < (char) ('a' + (maxValue - 1)))
            return new Coordinate((char) (x + distance), y);
        else
            throw new OutOfBoundsException("Cannot go right");
    }

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
        return Objects.hash(x, y);
    }

    public JsonObject toJsonObject() {
        JsonObject jo = new JsonObject();
        jo.put("x", String.valueOf(x));
        jo.put("y", y);
        jo.put("maxValue", maxValue);
        return jo;
    }

    public static Coordinate fromJsonObject(JsonObject jo) {
        char x = ((String) jo.get("x")).charAt(0);
        int y = ((BigDecimal) jo.get("y")).intValue();
        
        return new Coordinate(x, y);
    }

}
