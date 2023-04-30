package com.galactica.model.ships;

import com.galactica.model.Coordinate;
import com.galactica.model.Direction;
import com.galactica.model.Ship;

// Declare the DeathStar class, which extends the Ship class
public class DeathStar extends Ship {

    // Define a constructor with a single parameter, identifier
    public DeathStar(int identifier) {
        // DeathStar has a fixed length of 5
        this.length = 5;
        // Assign the provided identifier to the ship
        this.identifier = identifier;
    }

    // Define another constructor with five parameters: length, identifier, sunk, coordinate, and direction
    public DeathStar(int length, int identifier, boolean sunk, Coordinate coordinate, Direction direction) {
        // Assign the provided length to the ship
        this.length = length;
        // Assign the provided identifier to the ship
        this.identifier = identifier;
        // Assign the provided sunk status to the ship
        this.sunk = sunk;
        // Assign the provided coordinate to the ship
        this.coordinate = coordinate;
        // Assign the provided direction to the ship
        this.direction = direction;
    }
}
