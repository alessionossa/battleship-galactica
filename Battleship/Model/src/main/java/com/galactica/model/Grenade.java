package com.galactica.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import com.github.cliftonlabs.json_simple.JsonObject;
import java.util.Collections;

public class Grenade extends Weapon {

    public Grenade() {
        this.amountOfUses = 3;
    }

    public Grenade(int amountOfUses) {
        this.amountOfUses = amountOfUses;
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

    public static Grenade fromJsonObject(JsonObject jo) {
        int amountOfUses = ((BigDecimal) jo.get("amountOfUses")).intValue();
        return new Grenade(amountOfUses);
    }

    @Override
    public String toString() {
        return "Grenade";
    }
}