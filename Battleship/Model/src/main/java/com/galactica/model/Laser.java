package com.galactica.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import com.github.cliftonlabs.json_simple.JsonObject;

// Laser class inherits from the Weapon class
public class Laser extends Weapon {

    // Default constructor for the Laser class, sets the amount of uses to 1
    public Laser() {
        this.amountOfUses = 1;
    }

    // Constructor for the Laser class, takes an integer as the amount of uses
    public Laser(int amountOfUses) {
        this.amountOfUses = amountOfUses;
    }

    // Method that returns a list of coordinates the laser passes through on the
    // opponent's grid
    public List<Coordinate> getLaserCoordinates(Coordinate coordinate, Grid opponentGrid, char rowOrColumn) {
        List<Coordinate> coordinateList = new ArrayList<Coordinate>();
        boolean hitPlanet = false;
        char x = coordinate.getX();
        int y = coordinate.getY();

        // Loop through the entire grid size
        for (int i = 0; i < opponentGrid.getGridSize(); i++) {
            if (rowOrColumn == 'r') { // row
                x = (char) ('a' + i);
            } else { // column
                y = i;
            }
            Coordinate newCoordinate = new Coordinate(x, y);
            // Add the coordinate to the list if the laser has not hit a planet yet
            if (hitPlanet == false)
                coordinateList.add(newCoordinate);
            // Check if there is a planet at the new coordinate, and set hitPlanet to true
            // if there is
            if (opponentGrid.getPlanetAtCoordinate(newCoordinate) != null) {
                hitPlanet = true;
            }
        }

        // Return the list of coordinates the laser passes through
        return coordinateList;
    }

    // Static method to create a Laser object from a JsonObject
    public static Laser fromJsonObject(JsonObject jo) {
        int amountOfUses = ((BigDecimal) jo.get("amountOfUses")).intValue();
        return new Laser(amountOfUses);
    }

    @Override
    public String toString() {
        return "Laser";
    }
}
