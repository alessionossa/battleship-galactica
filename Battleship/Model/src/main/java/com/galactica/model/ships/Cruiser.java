// Define package for the Cruiser class
package com.galactica.model.ships;

// Import necessary classes from other packages
import com.galactica.model.Coordinate;
import com.galactica.model.Direction;
import com.galactica.model.Ship;

// Declare the Cruiser class, which extends the Ship class
public class Cruiser extends Ship {

    // Define a constructor with a single parameter, identifier
    public Cruiser(int identifier) {
        // Cruiser has a fixed length of 3
        this.length = 3;
        // Assign the provided identifier to the ship
        this.identifier = identifier;
    }

    // Define another constructor with five parameters: length, identifier, sunk, coordinate, and direction
    public Cruiser(int length, int identifier, boolean sunk, Coordinate coordinate, Direction direction) {
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
