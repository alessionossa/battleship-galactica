package com.galactica.controller;

import java.util.List;
import java.util.Random;

import com.galactica.cli.*;
import com.galactica.model.*;

public class BattleshipCLI {
    private int playerTurn;
    private boolean asteroidMode;
    private boolean singlePlayerMode;
    private boolean gravityMode = false;
    private int gridSize;

    private Human p1;
    private Player p2;

    private final CLI cli;

    public BattleshipCLI(CLI cli) {
        this.cli = cli;
    }

    void playGame() {

        singlePlayerMode = cli.getPlayerModeResponse(); // TODO refactor singlplayerMode from cli to model
        gridSize = cli.getGridSizeResponse();
        if (gridSize >= 10) {
            gravityMode = cli.getGravityModeResponse();
        }
        asteroidMode = cli.getAsteroidModeResponse();

        Coordinate.setMaxValue(gridSize);
        Planet.setMaxPlanetLength((int) (Math.floor(Math.abs(Math.min(gridSize, 20) / 5))));
        Grid grid1 = new Grid(gridSize);
        Grid grid2 = new Grid(gridSize);
        Coordinate coordinateToShoot;
        char rowOrColumn;

        if (gravityMode) {
            List<Planet> planets1 = Planet.generatePlanets(gridSize);
            List<Planet> planets2 = Planet.generatePlanets(gridSize);

            grid1.placePlanets(planets1);
            grid2.placePlanets(planets2);
        }

        if (asteroidMode) {
            grid1.placeAsteroids();
            grid2.placeAsteroids();
        }

        p1 = new Human(grid1, grid2);
        placeShips(p1);

        if (singlePlayerMode) {
            p2 = new AI("CPU", grid2, grid1);
            placeShips((AI) p2);
        } else {
            p2 = new Human(grid2, grid1);
            placeShips((Human) p2);
        }

        boolean startShooting = true;
        // if (p1.hasAllShipsPlaced() || p2.hasAllShipsPlaced()) {
        // startShooting = true;
        // } else {
        // startShooting = false;
        // }

        playerTurn = 1;
        while (startShooting) {

            if (playerTurn == 1) {

                Weapon weaponsToShoot = WeaponCLI.askWeaponToShoot(this.cli, p1);

                if (weaponsToShoot.getAreaOfEffect() != 3) {
                    coordinateToShoot = CoordinateCLI.askCoordinateToShoot(this.cli, p1, grid2);
                    p1.shoot(coordinateToShoot, weaponsToShoot);

                } else {
                    rowOrColumn = CoordinateCLI.askRowOrColumnToShoot(this.cli, p1, grid2);
                    coordinateToShoot = CoordinateCLI.askLaserCoordinateToShoot(this.cli, p1, grid2, rowOrColumn);
                    p1.shootLaser(coordinateToShoot, rowOrColumn);
                }

                if (p2.areAllShipsSunk()) {
                    endGame(p1);
                    return;
                }

            } else {

                if (singlePlayerMode) {
                    p2.shoot(null, null);
                } else {
                    Weapon weaponsToShoot = WeaponCLI.askWeaponToShoot(this.cli, p2);

                    if (weaponsToShoot.getAreaOfEffect() != 3) {
                        coordinateToShoot = CoordinateCLI.askCoordinateToShoot(this.cli, p2, grid1);
                        p2.shoot(coordinateToShoot, weaponsToShoot);

                    } else {
                        rowOrColumn = CoordinateCLI.askRowOrColumnToShoot(this.cli, p2, grid1);
                        coordinateToShoot = CoordinateCLI.askLaserCoordinateToShoot(this.cli, p2, grid1, rowOrColumn);
                        p2.shootLaser(coordinateToShoot, rowOrColumn);
                    }
                }

                if (p1.areAllShipsSunk()) {
                    endGame(p2);
                    return;
                }
            }
            if (playerTurn == 1)
                playerTurn = 2;
            else
                playerTurn = 1;
        }
    }

    public static void main(String[] args) {

        CLI cli = new CLI();

        BattleshipCLI game = new BattleshipCLI(cli);

        game.playGame();
    }

    public void placeShips(AI player) {
        Random random = new Random();
        final char[] sequence = { 'v', 'h' };
        Grid ownGrid = player.getOwnGrid();
        Ship[] ships = player.getShips();
        System.out.println("--------------------------------------------- ");
        System.out.println(player.getName() + " is placing the ships... \n");
        for (Ship ship : ships) {
            boolean isValidShipPosition;

            do {
                Coordinate coordinate;
                boolean isValidCoordinate = false;
                do {
                    char x0 = (char) (random.nextInt(10) + 'a');
                    int y0 = random.nextInt(11);

                    coordinate = new Coordinate(x0, y0);
                    isValidCoordinate = ownGrid.isValidCoordinate(coordinate);
                } while (!isValidCoordinate);

                Direction direction = null;
                do {
                    char directionChar = sequence[random.nextInt(sequence.length)];
                    direction = Direction.get(directionChar);
                } while (direction == null);

                ship.setCoordinate(coordinate);
                ship.setDirection(direction);

                isValidShipPosition = ownGrid.isValidShipPosition(ship, coordinate, direction);
                if (isValidShipPosition)
                    try {
                        ownGrid.placeShip(ship, coordinate, direction);
                    } catch (OutOfBoundsException e) {

                    }
            } while (!isValidShipPosition);
        }
        GridCLI.printGrid(ownGrid, true);
    }

    public void placeShips(Human player) {
        boolean allShipsPlaced = false;
        while (!allShipsPlaced) {
            GridCLI.printGrid(player.getOwnGrid(), true);
            boolean placeOrRemove = cli.getPlaceOrRemoveResponse(player);

            Ship ship = ShipCLI.askShip(cli, player);
            if (!placeOrRemove && !ship.isPlaced()) {
                System.out.println("Cannot remove a ship that hasn't been placed");
            } else if (placeOrRemove && ship.isPlaced()) {
                System.out.println("Cannot place a ship that is already on the grid");
            } else if (!placeOrRemove && ship.isPlaced()) {
                try {
                    player.removeShip(ship);
                } catch (UnplacedShipException e) {

                }

            } else if (placeOrRemove && !ship.isPlaced()) {
                boolean isValidShipPosition;
                Coordinate coordinate;
                Direction direction;
                do {
                    coordinate = CoordinateCLI.askCoordinateToPlaceShip(cli, player);
                    direction = DirectionCLI.askDirection(cli, player);

                    isValidShipPosition = player.getOwnGrid().isValidShipPosition(ship, coordinate, direction);
                    if (isValidShipPosition) {
                        try {
                            player.placeShip(ship, coordinate, direction);
                        } catch (OutOfBoundsException e) {

                        }
                    } else {
                        System.out.println("You cannot place a ship here.");
                    }

                } while (!isValidShipPosition);
            }

            allShipsPlaced = player.hasAllShipsPlaced();

        }

    }

    void endGame(Player winner) {
        System.out.println("Congratulations " + winner.getName() + "! 🍾🎉");

        this.cli.scanner.close();
    }

}
