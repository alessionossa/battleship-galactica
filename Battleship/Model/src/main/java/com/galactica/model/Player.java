package com.galactica.model;

import java.util.Arrays;
import java.util.Scanner;

public abstract class Player {
    protected String name;
    protected Ship[] ships;
    protected Grid ownGrid;
    protected Grid opponentGrid;
    static Scanner sc = new Scanner(System.in);

    public Player(Grid ownGrid, Grid opponentGrid) {
        ships = new Ship[3]; // Placeholder 5
        this.ownGrid = ownGrid;
        this.opponentGrid = opponentGrid;

        initializeShips();
    }

    abstract void placeShips();

    abstract void shoot();

    private void initializeShips() {
        ships[0] = new Ship(5, Ship.ShipType.DeathStar, 1);
        ships[1] = new Ship(3, Ship.ShipType.Cruiser, 2);
        ships[2] = new Ship(1, Ship.ShipType.Scout, 3);
    }

    boolean areAllShipsSunk() {
        return Arrays.stream(ships).allMatch(ship -> ship.isSunk());
    }

    boolean hasAllShipsPlaced() {
        return Arrays.stream(ships).allMatch(ship -> ship.isPlaced());
    }

    public String getName() {
        return name;
    }
}
