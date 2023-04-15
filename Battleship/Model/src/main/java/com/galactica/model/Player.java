package com.galactica.model;

import com.galactica.model.ships.Cruiser;
import com.galactica.model.ships.DeathStar;
import com.galactica.model.ships.Scout;

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

    public abstract void shoot(Coordinate coordinate);

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

    public void placeShip(Ship ship, Coordinate coordinate, Direction direction) throws OutOfBoundsException {
        ship.setCoordinate(coordinate);
        ship.setDirection(direction);
        ownGrid.placeShip(ship, coordinate, direction);
    }

    public void removeShip(Ship ship) throws UnplacedShipException{
        ownGrid.removeShip(ship);
        ship.setCoordinate(null);
        ship.setDirection(null);
    }

    public String getName() {
        return name;
    }

    public Grid getOwnGrid() {
        return ownGrid;
    }

    public Grid getOpponentGrid() {
        return opponentGrid;
    }

    public Ship[] getShips() {
        return ships;
    }
}
