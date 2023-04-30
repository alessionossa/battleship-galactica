package com.galactica.controller;

import com.galactica.cli.*;
import com.galactica.model.*;

public class BattleshipCLI {

    private final CLI cli = new CLI();

    private Game gameModel;

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
                Ship placedShip;
                Coordinate coordinate;
                Direction direction;
                do {
                    coordinate = CoordinateCLI.askCoordinateToPlaceShip(cli, player);
                    direction = DirectionCLI.askDirection(cli, player);

                    placedShip = player.placeShip(ship, coordinate, direction);
                    if (placedShip == null)
                        System.out.println("You cannot place a ship here.");

                } while (placedShip == null);
            }

            allShipsPlaced = player.hasAllShipsPlaced();

        }

    }

    void playGame() {

        boolean startNewGame = cli.getNewOrLoadResponse();
        if (startNewGame) {
            gameModel.singlePlayerMode = cli.getPlayerModeResponse();
            gameModel.gridSize = cli.getGridSizeResponse();
            if (gameModel.gridSize >= 10) {
                gameModel.gravityMode = cli.getGravityModeResponse();
            }
            gameModel.asteroidMode = cli.getAsteroidModeResponse();

            gameModel.grid1 = Game.setUpGrid(gameModel.gridSize, gameModel.singlePlayerMode, gameModel.asteroidMode, gameModel.gravityMode);
            gameModel.grid2 = Game.setUpGrid(gameModel.gridSize, gameModel.singlePlayerMode, gameModel.asteroidMode, gameModel.gravityMode);

            gameModel.p1 = new Human("Space Cowboy", gameModel.grid1, gameModel.grid2);
            placeShips(gameModel.p1);

            if (gameModel.singlePlayerMode) {
                gameModel.p2 = new AI("Megatron", gameModel.grid2, gameModel.grid1);
                placeShips((AI) gameModel.p2);
            } else {
                gameModel.p2 = new Human("Rocket Rancher", gameModel.grid2, gameModel.grid1);
                placeShips((Human) gameModel.p2);
            }
        } else {
            gameModel.load(gameModel.getDefaultPath());
        }

        Coordinate coordinateToShoot;
        char rowOrColumn;

        gameModel.playerTurn = 1;
        while (true) {

            if (gameModel.playerTurn == 1) {

                Weapon weaponsToShoot = WeaponCLI.askWeaponToShoot(this.cli, gameModel.p1);

                if (weaponsToShoot instanceof Laser) {
                    rowOrColumn = CoordinateCLI.askRowOrColumnToShoot(this.cli, gameModel.p1, gameModel.grid2);
                    coordinateToShoot = CoordinateCLI.askLaserCoordinateToShoot(this.cli, gameModel.p1, gameModel.grid2, rowOrColumn);
                    gameModel.p1.shootLaser(coordinateToShoot, rowOrColumn, (Laser) weaponsToShoot);

                } else {
                    coordinateToShoot = CoordinateCLI.askCoordinateToShoot(this.cli, gameModel.p1, gameModel.grid2);
                    gameModel.p1.shoot(coordinateToShoot, weaponsToShoot, gameModel.gravityMode, false);
                }

                if (gameModel.p2.areAllShipsSunk(gameModel.p2.getShips())) {
                    endGame(gameModel.p1);
                    return;
                }
                gameModel.save();

            } else {

                if (gameModel.singlePlayerMode) {
                    gameModel.p2.shoot(null, null, gameModel.gravityMode, false);
                } else {
                    Weapon weaponToShoot = WeaponCLI.askWeaponToShoot(this.cli, gameModel.p2);

                    if (weaponToShoot instanceof Laser) {
                        rowOrColumn = CoordinateCLI.askRowOrColumnToShoot(this.cli, gameModel.p2, gameModel.grid1);
                        coordinateToShoot = CoordinateCLI.askLaserCoordinateToShoot(this.cli, gameModel.p2, gameModel.grid1, rowOrColumn);
                        gameModel.p2.shootLaser(coordinateToShoot, rowOrColumn, (Laser) weaponToShoot);

                    } else {
                        coordinateToShoot = CoordinateCLI.askCoordinateToShoot(this.cli, gameModel.p2, gameModel.grid1);
                        gameModel.p2.shoot(coordinateToShoot, weaponToShoot, gameModel.gravityMode, false);
                    }
                }

                if (gameModel.p1.areAllShipsSunk(gameModel.p1.getShips())) {
                    endGame(gameModel.p2);
                    return;
                }
            }
            if (gameModel.playerTurn == 1)
                gameModel.playerTurn = 2;
            else
                gameModel.playerTurn = 1;
        }
    }

    public static void main(String[] args) {
        BattleshipCLI cli = new BattleshipCLI();
        cli.gameModel = new Game();
        cli.playGame();
    }

    void endGame(Player winner) {
        System.out.println("Congratulations " + winner.getName() + "! 🍾🎉");

        this.cli.scanner.close();
    }

}