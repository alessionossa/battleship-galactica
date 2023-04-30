package com.galactica.model;

import com.github.cliftonlabs.json_simple.JsonObject;

public class Asteroid {
    private Coordinate coordinate;

    public Asteroid(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public JsonObject toJsonObject() {
        JsonObject jo = new JsonObject();
        jo.put("coordinate", coordinate.toJsonObject());
        return jo;
    }

    public static Asteroid fromJsonObject(JsonObject jo) {
        Coordinate coordinate = Coordinate.fromJsonObject((JsonObject) jo.get("coordinate"));
        return new Asteroid(coordinate);
    }
}