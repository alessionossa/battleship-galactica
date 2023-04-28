package com.galactica.model;

import com.github.cliftonlabs.json_simple.JsonObject;

public class Tile {
    private Ship ship;
    private Asteroid asteroid;
    private boolean hit;
    private Planet planet;

    public Tile() {
        this.hit = false;
    }


    public Ship getShip() {
        return ship;
    }

    public void setShip(Ship ship) {
        this.ship = ship;
    }

    public void setHit(boolean hit) {
        this.hit = hit;
    }


    public Asteroid getAsteroid() {
        return this.asteroid;
    }

    public void setAsteroid(Asteroid asteroid) {
        this.asteroid = asteroid;
    }

    public boolean isHit() {
        return hit;
    }

    public void setPlanet(Planet planet) {
        this.planet = planet;
    }

    public Planet getPlanet() {
        return this.planet;
    }

    public JsonObject toJsonObject() {
        JsonObject jo = new JsonObject();
        jo.put("ship", ship.toJsonObject());
        jo.put("asteroid", asteroid.toJsonObject());
        jo.put("hit", hit);
        jo.put("planet", planet.toJsonObject());
        return jo;
    }
}
