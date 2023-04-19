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

        if (rowOrColumn == 'r') {
            for (int i = 0; i < opponentGrid.getGridSize(); i++) {
                char newX = (char) (coordinate.getX() + i);
                int newY = coordinate.getY();
                Coordinate newCoordinate = new Coordinate(newX, newY);
                if (hitPlanet == false)
                    coordinateList.add(newCoordinate);
                if (opponentGrid.getPlanetAtCoordinate(newCoordinate) != null) {
                    hitPlanet = true;
                }

            }
        } else { // column
            for (int i = 0; i < opponentGrid.getGridSize(); i++) {
                char newX = (char) (coordinate.getX());
                int newY = coordinate.getY() + i;
                Coordinate newCoordinate = new Coordinate(newX, newY);
                if (hitPlanet == false)
                    coordinateList.add(newCoordinate);
                if (opponentGrid.getPlanetAtCoordinate(newCoordinate) != null) {
                    hitPlanet = true;
                }

            }
        }
        return coordinateList;
    }
}
