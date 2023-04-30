package com.galactica.model;

import com.github.cliftonlabs.json_simple.JsonObject;

// Asteroid class represents an asteroid object in the game
public class Asteroid {
    private Coordinate coordinate; // Coordinate object representing the asteroid's location on the grid

    // Constructor to initialize the asteroid object with its coordinate
    public Asteroid(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    // Getter method to get the asteroid's coordinate
    public Coordinate getCoordinate() {
        return coordinate;
    }

    // Method to convert the asteroid object to a JsonObject
    public JsonObject toJsonObject() {
        JsonObject jo = new JsonObject();
        jo.put("coordinate", coordinate.toJsonObject());
        return jo;
    }

    // Method to create an Asteroid object from a JsonObject
    public static Asteroid fromJsonObject(JsonObject jo) {
        Coordinate coordinate = Coordinate.fromJsonObject((JsonObject) jo.get("coordinate"));
        return new Asteroid(coordinate);
    }
}
