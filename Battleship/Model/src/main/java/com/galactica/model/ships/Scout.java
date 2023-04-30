package com.galactica.model.ships;

import com.galactica.model.Coordinate;
import com.galactica.model.Direction;
import com.galactica.model.Ship;

public class Scout extends Ship {

    public Scout(int identifier) {
        this.length = 1;
        this.identifier = identifier;
    }

    public Scout(int length, int identifier, boolean sunk, Coordinate coordinate, Direction direction) {
        this.length = length;
        this.identifier = identifier;
        this.sunk = sunk;
        this.coordinate = coordinate;
        this.direction = direction;
    }
}