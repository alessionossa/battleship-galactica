package com.galactica.model;

public abstract class Ship {

    protected int length;
    protected boolean sunk; // Status of the ship, true if ship is sunk, false if not
    protected int identifier;

    Coordinate coordinate;
    Direction direction;

    public int getLength() {
        return length;
    }

    public int getIdentifier() {
        return identifier;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setSunk(boolean sunk) {
        this.sunk = sunk;
    }

    public boolean isSunk() {
        return sunk;
    }

    public boolean isPlaced() {
        if (coordinate != null)
            return true;
        else
            return false;
    }
}