package com.galactica.cli;

import com.galactica.model.Asteroid;
import com.galactica.model.Planet;
import com.galactica.model.Ship;
import com.galactica.model.Tile;

public class TileCLI {

    public static String displayValue(Tile tile, boolean showGridObjects) {
        boolean hit = tile.isHit();
        Ship ship = tile.getShip();
        Planet planet = tile.getPlanet();
        Asteroid asteroid = tile.getAsteroid();
        if (ship != null) {
            if (ship.isSunk()) {
                return "S";
            } else if (hit)
                return "X";
            else {
                if (showGridObjects)
                    return Integer.toString(ship.getIdentifier());
                else
                    return "0";
            }

        } else if (planet != null) {
            if (hit || showGridObjects) {
                return "P";
            } else
                return "0";

        } else {
            if (hit) {
                if (asteroid != null) {
                    return "X";
                } else
                    return "/";
            }

            else if (showGridObjects && asteroid != null) {
                return "A";
            } else
                return "0";

        }
    }
}