package com.galactica.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Grenade extends Weapon {

    public Grenade() {
        this.areaOfEffect = 2;
        this.amountOfUses = 3;
    }

    public List<Coordinate> getScatterCoordinates(Coordinate coordinate, Grid opponentGrid) {
        Random random = new Random();
        List<Coordinate> coordinateList = new ArrayList<Coordinate>();
        char newX = (char) (coordinate.getX());
        int newY = coordinate.getY();
        Coordinate newCoordinate = new Coordinate(newX, newY);
        coordinateList.add(newCoordinate);

        for (int i = 0; i < 9; i++) {
            int randomInt1 = random.nextInt(3) - 1;
            int randomInt2 = random.nextInt(3) - 1;
            newX = (char) (coordinate.getX() + randomInt1);
            newY = coordinate.getY() + randomInt2;

            newCoordinate = new Coordinate(newX, newY);
            if (opponentGrid.isValidCoordinate(newCoordinate))
                coordinateList.add(newCoordinate);
        }

        return coordinateList;
    }
}
