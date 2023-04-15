package com.galactica.model;

import java.util.Objects;

public class Coordinate { // Send new coordinates to player
    private char x;
    private int y;

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

    public void setX(char newX) {
        this.x = newX;
    }

    public void setY(int newY) {
        this.y = newY;
    }

    public Coordinate up() throws OutOfBoundsException {
        if (y > 0)
            return new Coordinate(x, y - 1);
        else
            throw new OutOfBoundsException("Cannot go up");
    }

    public Coordinate down() throws OutOfBoundsException {
        if (y < 9)
            return new Coordinate(x, y + 1);
        else
            throw new OutOfBoundsException("Cannot go down");
    }

    public Coordinate left() throws OutOfBoundsException {
        if (x > 'a')
            return new Coordinate((char) (x - 1), y);
        else
            throw new OutOfBoundsException("Cannot go left");
    }

    public Coordinate right() throws OutOfBoundsException {
        if (x < 'j')
            return new Coordinate((char) (x + 1), y);
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
}
