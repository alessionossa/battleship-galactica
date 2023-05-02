package com.galactica.model;

import com.github.cliftonlabs.json_simple.JsonObject;

public class Tile {
    private final int identifier = IdGenerator.get();
    private Ship ship;
    private Asteroid asteroid;
    private boolean hit = false;
    private Planet planet;

    public Tile() {
    }

    // Declare a constructor
    public Tile(Ship ship, Asteroid asteroid, Planet planet, boolean hit) {
        this.ship = ship;
        this.asteroid = asteroid;
        this.planet = planet;
        this.hit = hit;
    }

    // Declare a getter method for the ship variable
    public Ship getShip() {
        return ship;
    }

    public int getIdentifier() {
        return identifier;
    }

    // Declare a setter method for the ship variable
    public void setShip(Ship ship) {
        this.ship = ship;
    }

    // Declare a setter method for the hit variable
    public void setHit(boolean hit) {
        this.hit = hit;
    }

    // Declare a getter method for the asteroid variable
    public Asteroid getAsteroid() {
        return this.asteroid;
    }

    // Declare a setter method for the asteroid variable
    public void setAsteroid(Asteroid asteroid) {
        this.asteroid = asteroid;
    }

    // Declare a getter method for the hit variable
    public boolean isHit() {
        return hit;
    }

    // Declare a setter method for the planet variable
    public void setPlanet(Planet planet) {
        this.planet = planet;
    }

    // Declare a getter method for the planet variable
    public Planet getPlanet() {
        return this.planet;
    }

    // Returning JSonObjects
    public JsonObject toJsonObject() {
        JsonObject jo = new JsonObject();
        if (ship != null)
            jo.put("ship", ship.toJsonObject());
        else
            jo.put("ship", null);
        if (asteroid != null)
            jo.put("asteroid", asteroid.toJsonObject());
        else
            jo.put("asteroid", null);
        if (planet != null)
            jo.put("planet", planet.toJsonObject());
        else
            jo.put("planet", null);

        jo.put("hit", hit);

        return jo;
    }

    public static Tile fromJsonObject(JsonObject jo) {
        Ship ship;
        if (jo.get("ship") == null) {
            ship = null;
        } else {
            ship = Ship.fromJsonObject((JsonObject) jo.get("ship"));
        }

        Asteroid asteroid;
        if (jo.get("asteroid") == null) {
            asteroid = null;
        } else {
            asteroid = Asteroid.fromJsonObject((JsonObject) jo.get("asteroid"));
        }

        Planet planet;
        if (jo.get("planet") == null) {
            planet = null;
        } else {
            planet = Planet.fromJsonObject((JsonObject) jo.get("planet"));
        }
        boolean hit = (boolean) jo.get("hit");
        return new Tile(ship, asteroid, planet, hit);
    }
}
