package com.galactica.model;

public class Ship {

    enum ShipType {
        Cruiser, DeathStar, Scout
    }

    private int length;
    private boolean sunk; // Status of the ship, true if ship is sunk, false if not
    private ShipType shipType;
    private int identifier;

    Coordinate coordinate;
    Direction direction;

    public Ship(int length, ShipType shipType, int identifier) {
        this.length = length;
        this.shipType = shipType;
        this.identifier = identifier;
        this.sunk = false;
    }

    public int getLength() {
        return length;
    }

    public ShipType getShipType() {
        return shipType;
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