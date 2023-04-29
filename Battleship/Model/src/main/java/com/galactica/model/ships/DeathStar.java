package com.galactica.model.ships;

import com.galactica.model.Coordinate;
import com.galactica.model.Direction;
import com.galactica.model.Ship;

public class DeathStar extends Ship {

    public DeathStar(int identifier) {
        this.length = 5;
        this.identifier = identifier;
    }

    public DeathStar(int length, int identifier, boolean sunk, Coordinate coordinate, Direction direction) {
        this.length = length;
        this.identifier = identifier;
        this.sunk = sunk;
        this.coordinate = coordinate;
        this.direction = direction;
    }
}
