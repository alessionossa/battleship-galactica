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

    private Grid grid1;
    private Grid grid2;

    private final CLI cli;

    public BattleshipCLI(CLI cli) {
        this.cli = cli;
    }

    public void placeShips(AI player) {
        Grid ownGrid = player.getOwnGrid();

        System.out.println("--------------------------------------------- ");
        System.out.println(player.getName() + " is placing the ships... \n");
        player.placeShips();

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
                        player.placeShip(ship, coordinate, direction);

                    } else {
                        System.out.println("You cannot place a ship here.");
                    }

                } while (!isValidShipPosition);
            }

            allShipsPlaced = player.hasAllShipsPlaced();

        }

    }

    void playGame() {

        singlePlayerMode = cli.getPlayerModeResponse();
        gridSize = cli.getGridSizeResponse();
        if (gridSize >= 10) {
            gravityMode = cli.getGravityModeResponse();
        }
        asteroidMode = cli.getAsteroidModeResponse();

        grid1 = Game.setUpGrid(gridSize, singlePlayerMode, asteroidMode, gravityMode);
        grid2 = Game.setUpGrid(gridSize, singlePlayerMode, asteroidMode, gravityMode);

        p1 = new Human(grid1, grid2);
        placeShips(p1);

        if (singlePlayerMode) {
            p2 = new AI("CPU", grid2, grid1);
            placeShips((AI) p2);
        } else {
            p2 = new Human(grid2, grid1);
            placeShips((Human) p2);
        }

        Coordinate coordinateToShoot;
        char rowOrColumn;

        playerTurn = 1;
        while (true) {

            if (playerTurn == 1) {

                Weapon weaponsToShoot = WeaponCLI.askWeaponToShoot(this.cli, p1);

                if (weaponsToShoot.getAreaOfEffect() != 3) {
                    coordinateToShoot = CoordinateCLI.askCoordinateToShoot(this.cli, p1, grid2);
                    p1.shoot(coordinateToShoot, weaponsToShoot, gravityMode, false);

                } else {
                    rowOrColumn = CoordinateCLI.askRowOrColumnToShoot(this.cli, p1, grid2);
                    coordinateToShoot = CoordinateCLI.askLaserCoordinateToShoot(this.cli, p1, grid2, rowOrColumn);
                    p1.shootLaser(coordinateToShoot, rowOrColumn, (Laser) weaponsToShoot);
                }

                if (p2.areAllShipsSunk()) {
                    endGame(p1);
                    return;
                }

            } else {

                if (singlePlayerMode) {
                    p2.shoot(null, null, gravityMode, false);
                } else {
                    Weapon weaponToShoot = WeaponCLI.askWeaponToShoot(this.cli, p2);

                    if (weaponToShoot.getAreaOfEffect() != 3) {
                        coordinateToShoot = CoordinateCLI.askCoordinateToShoot(this.cli, p2, grid1);
                        p2.shoot(coordinateToShoot, weaponToShoot, gravityMode, false);

                    } else {
                        rowOrColumn = CoordinateCLI.askRowOrColumnToShoot(this.cli, p2, grid1);
                        coordinateToShoot = CoordinateCLI.askLaserCoordinateToShoot(this.cli, p2, grid1, rowOrColumn);
                        p2.shootLaser(coordinateToShoot, rowOrColumn, (Laser) weaponToShoot);
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

    void endGame(Player winner) {
        System.out.println("Congratulations " + winner.getName() + "! 🍾🎉");

        this.cli.scanner.close();
    }

}
