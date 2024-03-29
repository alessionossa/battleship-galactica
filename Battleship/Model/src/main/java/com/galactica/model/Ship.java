package com.galactica.model;

import java.math.BigDecimal;

import com.galactica.model.ships.Cruiser;
import com.galactica.model.ships.DeathStar;
import com.galactica.model.ships.Scout;
import com.github.cliftonlabs.json_simple.JsonObject;

public abstract class Ship {

    // Fields
    protected int length;
    protected boolean sunk;
    protected int identifier;
    protected Coordinate coordinate;
    protected Direction direction;

    // Getter methods
    public int getLength() {
        return length;
    }

    public int getIdentifier() {
        return identifier;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public Direction getDirection() {
        return direction;
    }

    public boolean isSunk() {
        return sunk;
    }

    // Setter methods
    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void setSunk() {
        this.sunk = true;
    }

    // Check if the ship is placed
    public boolean isPlaced() {
        return coordinate != null;
    }

    // Convert object to JSON object
    public JsonObject toJsonObject() {
        JsonObject jo = new JsonObject();
        jo.put("length", length);
        jo.put("sunk", sunk);
        jo.put("identifier", identifier);
        if (coordinate != null) {
            jo.put("coordinate", coordinate.toJsonObject());
        } else {
            jo.put("coordinate", null);
        }

        if (direction != null) {
            jo.put("direction", direction.toJsonObject());
        } else {
            jo.put("direction", null);
        }

        return jo;
    }

    // Convert JSON object to Ship object
    public static Ship fromJsonObject(JsonObject jo) {
        int length = ((BigDecimal) jo.get("length")).intValue();
        boolean sunk = (boolean) jo.get("sunk");
        int identifier = ((BigDecimal) jo.get("identifier")).intValue();

        Coordinate coordinate;
        Direction direction;

        if (jo.get("coordinate") != null) {
            coordinate = Coordinate.fromJsonObject((JsonObject) jo.get("coordinate"));
        } else {
            coordinate = null;
        }

        if (jo.get("direction") != null) {
            direction = Direction.fromJsonObject((JsonObject) jo.get("direction"));
        } else {
            direction = null;
        }

        // Create a new instance of the correct subclass based on the length of the ship
        if (length == 5)
            return new DeathStar(length, identifier, sunk, coordinate, direction);
        else if (length == 3)
            return new Cruiser(length, identifier, sunk, coordinate, direction);
        else
            return new Scout(length, identifier, sunk, coordinate, direction);
    }
}
