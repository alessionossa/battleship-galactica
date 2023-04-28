package com.galactica.model;

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
}
