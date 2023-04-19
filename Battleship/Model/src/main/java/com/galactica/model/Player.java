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

    protected Weapon[] weapons;
    static Scanner sc = new Scanner(System.in);

    public Player(Grid ownGrid, Grid opponentGrid) {
        ships = new Ship[3]; // Placeholder 5
        weapons = new Weapon[3];
        this.ownGrid = ownGrid;
        this.opponentGrid = opponentGrid;

        initializeWeapons();
        initializeShips();
    }

    public abstract void shoot(Coordinate coordinate, Weapon weaponToShoot);

    public abstract void shootLaser(Coordinate coordinate, char rowOrColumn, Laser laser);

    private void initializeShips() {
        ships[0] = new DeathStar(1);
        ships[1] = new Cruiser(2);
        ships[2] = new Scout(3);
    }

    private void initializeWeapons() {
        weapons[0] = new Cannon();
        weapons[1] = new Grenade();
        weapons[2] = new Laser();
    }

    public boolean areAllShipsSunk() {
        return Arrays.stream(ships).allMatch(ship -> ship.isSunk());
    }

    public boolean hasAllShipsPlaced() {
        return Arrays.stream(ships).allMatch(ship -> ship.isPlaced());
    }

    public void placeShip(Ship ship, Coordinate coordinate, Direction direction) {
        ship.setCoordinate(coordinate);
        ship.setDirection(direction);
        ownGrid.placeShip(ship, coordinate, direction);
    }

    public void removeShip(Ship ship) throws UnplacedShipException {
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

    public Weapon[] getWeapons() {
        return weapons;
    }

}
