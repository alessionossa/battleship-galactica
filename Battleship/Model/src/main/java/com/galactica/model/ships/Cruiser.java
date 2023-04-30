package com.galactica.model.ships;

import com.galactica.model.Coordinate;
import com.galactica.model.Direction;
import com.galactica.model.Ship;

public class Cruiser extends Ship {

    public Cruiser(int identifier) {
        this.length = 3;
        this.identifier = identifier;
    }

    public Cruiser(int length, int identifier, boolean sunk, Coordinate coordinate, Direction direction) {
        this.length = length;
        this.identifier = identifier;
        this.sunk = sunk;
        this.coordinate = coordinate;
        this.direction = direction;
    }
}