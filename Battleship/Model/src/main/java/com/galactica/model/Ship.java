package com.galactica.model;

import java.math.BigDecimal;

import com.galactica.model.ships.Cruiser;
import com.galactica.model.ships.DeathStar;
import com.galactica.model.ships.Scout;
import com.github.cliftonlabs.json_simple.JsonObject;

public abstract class Ship {

    protected int length;
    protected boolean sunk; // Status of the ship, true if ship is sunk, false if not
    protected int identifier;

    protected Coordinate coordinate;
    protected Direction direction;

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

    public JsonObject toJsonObject() {
        JsonObject jo = new JsonObject();
        jo.put("length", length);
        jo.put("sunk", sunk);
        jo.put("identifier", identifier);
        jo.put("coordinate", coordinate.toJsonObject());
        jo.put("direction", direction.toJsonObject());
        return jo;
    }

    public static Ship fromJsonObject(JsonObject jo) {
        int length = ((BigDecimal) jo.get("length")).intValue();
        boolean sunk = (boolean) jo.get("sunk");
        int identifier = ((BigDecimal) jo.get("identifier")).intValue();
        Coordinate coordinate = Coordinate.fromJsonObject((JsonObject) jo.get("coordinate")); 
        Direction direction = Direction.fromJsonObject((JsonObject) jo.get("direction"));
        
        if (length == 5)
            return new DeathStar(length, identifier, sunk, coordinate, direction);
        else if (length == 3)
            return new Cruiser(length, identifier, sunk, coordinate, direction);
        else
            return new Scout(length, identifier, sunk, coordinate, direction);
    }
    
}