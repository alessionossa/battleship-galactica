package com.galactica.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import com.github.cliftonlabs.json_simple.JsonObject;

public class Laser extends Weapon {
    public Laser() {
        this.amountOfUses = 1;
    }

    public Laser(int amountOfUses) {
        this.amountOfUses = amountOfUses;
    }

    public List<Coordinate> getLaserCoordinates(Coordinate coordinate, Grid opponentGrid, char rowOrColumn) {
        List<Coordinate> coordinateList = new ArrayList<Coordinate>();
        boolean hitPlanet = false;
        char x = coordinate.getX();
        int y = coordinate.getY();

        for (int i = 0; i < opponentGrid.getGridSize(); i++) {
            if (rowOrColumn == 'r') { // row
                x = (char) ('a' + i);
            } else { // column
                y = i;
            }
            Coordinate newCoordinate = new Coordinate(x, y);
            if (hitPlanet == false)
                coordinateList.add(newCoordinate);
            if (opponentGrid.getPlanetAtCoordinate(newCoordinate) != null) {
                hitPlanet = true;
            }
        }

        return coordinateList;
    }

    public static Laser fromJsonObject(JsonObject jo) {
        int amountOfUses = ((BigDecimal) jo.get("amountOfUses")).intValue();
        return new Laser(amountOfUses);
    }

    @Override
    public String toString() {
        return "Laser";
    }
}