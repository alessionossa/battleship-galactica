package com.galactica.model;

import java.util.ArrayList;
import java.util.List;

public class Laser extends Weapon {
    public Laser() {
        this.areaOfEffect = 3;
        this.amountOfUses = 1;
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
}
