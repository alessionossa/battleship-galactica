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

        initializeShips(

        );
    }

    public abstract void placeShip(Ship ship, Coordinate coordinate, Direction direction);

    public abstract void placeShips();

    public abstract void shoot();

    private void initializeShips() {
        ships[0] = new DeathStar(1);
        ships[1] = new Cruiser(2);
        ships[2] = new Scout(3);
    }

    public boolean areAllShipsSunk() {
        return Arrays.stream(ships).allMatch(ship -> ship.isSunk());
    }

    public boolean hasAllShipsPlaced() {
        return Arrays.stream(ships).allMatch(ship -> ship.isPlaced());
    }

    public String getName() {
        return name;
    }
}
