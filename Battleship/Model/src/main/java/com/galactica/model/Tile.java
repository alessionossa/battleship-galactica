package com.galactica.model;

public class Tile {
    private Ship ship;
    private Asteroid asteroid;
    private boolean hit;

    public Tile(Ship ship) { // Needs to be modified so we can have <T> type of ship, obstacles
        this.ship = ship;
        this.hit = false;
    }

    public Tile() {
        this.hit = false;
    }

    public Tile(Asteroid asteroid) {
        this.asteroid = asteroid;
    }

    /*
     * @Override
     * public String toString() {
     * if (this.ship == null) {
     * if (this.hit)
     * return "/";
     * else
     * return "0";
     * } else {
     * if (this.hit)
     * return "X";
     * else
     * return Integer.toString(ship.getIdentifier());
     * }
     * }
     */

    public String displayValue(boolean showGridObjects) {
        if (this.ship != null) {
            if (this.hit)
                return "X";
            else {
                if (showGridObjects)
                    return Integer.toString(ship.getIdentifier());
                else
                    return "0";
            }

        } else {
            if (this.hit) {
                if (this.asteroid != null) {
                    return "X";
                } else
                    return "/";
            }

            else if (showGridObjects && this.asteroid != null) {
                return "A";
            } else
                return "0";

        }
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
}
