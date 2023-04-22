package com.galactica.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class Grenade extends Weapon {

    public Grenade() {
        this.areaOfEffect = 2;
        this.amountOfUses = 3;
    }

    public List<Coordinate> getScatterCoordinates(Coordinate coordinate, Grid opponentGrid) {
        List<Coordinate> coordinateList = new ArrayList<Coordinate>();

        int y = coordinate.getY();
        int xInt = coordinate.getX() - 'a';

        for (int i = xInt - 1; i <= xInt + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
                if (i == xInt && j == y)
                    continue;

                char adjacentX = (char) (i + 'a');
                Coordinate newCoordinate = new Coordinate(adjacentX, j);
                if (opponentGrid.isValidCoordinate(newCoordinate))
                    coordinateList.add(newCoordinate);
            }
        }
        Collections.shuffle(coordinateList);
        Coordinate firstCoordinatetoHit = coordinateList.get(0);
        Coordinate secondCoordinatetoHit = coordinateList.get(1);
        coordinateList.clear();
        coordinateList.add(coordinate);
        coordinateList.add(firstCoordinatetoHit);
        coordinateList.add(secondCoordinatetoHit);

        return coordinateList;
    }
}

