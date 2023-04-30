package com.galactica.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import com.github.cliftonlabs.json_simple.JsonObject;
import java.util.Collections;

// Grenade class representing a grenade weapon, extends the Weapon class
public class Grenade extends Weapon {

    // Default constructor initializing the amount of uses to 3
    public Grenade() {
        this.amountOfUses = 3;
    }

    // Constructor allowing to set the amount of uses
    public Grenade(int amountOfUses) {
        this.amountOfUses = amountOfUses;
    }

    // Method for getting the scatter coordinates affected by the grenade on the
    // opponent's grid
    public List<Coordinate> getScatterCoordinates(Coordinate coordinate, Grid opponentGrid) {
        List<Coordinate> coordinateList = new ArrayList<Coordinate>();

        int y = coordinate.getY();
        int xInt = coordinate.getX() - 'a';

        // Loop through the surrounding coordinates of the target coordinate
        for (int i = xInt - 1; i <= xInt + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
                // Skip the target coordinate itself
                if (i == xInt && j == y)
                    continue;

                char adjacentX = (char) (i + 'a');
                Coordinate newCoordinate = new Coordinate(adjacentX, j);
                // Add the new coordinate if it's valid on the opponent's grid
                if (opponentGrid.isValidCoordinate(newCoordinate))
                    coordinateList.add(newCoordinate);
            }
        }
        // Shuffle the coordinate list and pick the first two coordinates
        Collections.shuffle(coordinateList);
        Coordinate firstCoordinatetoHit = coordinateList.get(0);
        Coordinate secondCoordinatetoHit = coordinateList.get(1);
        // Clear the coordinate list and add the target coordinate and the two randomly
        // picked coordinates
        coordinateList.clear();
        coordinateList.add(coordinate);
        coordinateList.add(firstCoordinatetoHit);
        coordinateList.add(secondCoordinatetoHit);

        return coordinateList;
    }

    // Method for creating a Grenade object from a JsonObject
    public static Grenade fromJsonObject(JsonObject jo) {
        int amountOfUses = ((BigDecimal) jo.get("amountOfUses")).intValue();
        return new Grenade(amountOfUses);
    }
}
