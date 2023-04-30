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
            gameModel.setSinglePlayerMode(cli.getPlayerModeResponse());
            gameModel.setGridSize(cli.getGridSizeResponse());
            if (gameModel.getGridSize() >= 10) {
                gameModel.setGravityMode(cli.getGravityModeResponse());
            }
            gameModel.setAsteroidMode(cli.getAsteroidModeResponse());

            gameModel.setGrid1(Game.setUpGrid(gameModel.getGridSize(), gameModel.getSinglePlayerMode(), gameModel.getAsteroidMode(), gameModel.getGravityMode()));
            gameModel.setGrid2(Game.setUpGrid(gameModel.getGridSize(), gameModel.getSinglePlayerMode(), gameModel.getAsteroidMode(), gameModel.getGravityMode()));

            gameModel.setP1(new Human("Space Cowboy", gameModel.getGrid1(), gameModel.getGrid2()));
            placeShips(gameModel.getP1());

            if (gameModel.getSinglePlayerMode()) {
                gameModel.setP2(new AI("Megatron", gameModel.getGrid2(), gameModel.getGrid1()));
                placeShips((AI) gameModel.getP2());
            } else {
                gameModel.setP2(new Human("Rocket Rancher", gameModel.getGrid2(), gameModel.getGrid1()));
                placeShips((Human) gameModel.getP2());
            }
        } else {
            gameModel.load(Game.getDefaultPath());
        }

        Coordinate coordinateToShoot;
        char rowOrColumn;

        gameModel.setPlayerTurn(1);
        while (true) {

            if (gameModel.getPlayerTurn() == 1) {

                Weapon weaponsToShoot = WeaponCLI.askWeaponToShoot(this.cli, gameModel.getP1());

                if (weaponsToShoot instanceof Laser) {
                    rowOrColumn = CoordinateCLI.askRowOrColumnToShoot(this.cli, gameModel.getP1(), gameModel.getGrid2());
                    coordinateToShoot = CoordinateCLI.askLaserCoordinateToShoot(this.cli, gameModel.getP1(), gameModel.getGrid2(), rowOrColumn);
                    gameModel.getP1().shootLaser(coordinateToShoot, rowOrColumn, (Laser) weaponsToShoot);

                } else {
                    coordinateToShoot = CoordinateCLI.askCoordinateToShoot(this.cli, gameModel.getP1(), gameModel.getGrid2());
                    gameModel.getP1().shoot(coordinateToShoot, weaponsToShoot, gameModel.getGravityMode(), false);
                }

                if (gameModel.getP2().areAllShipsSunk(gameModel.getP2().getShips())) {
                    endGame(gameModel.getP1());
                    return;
                }
                gameModel.save();

            } else {

                if (gameModel.getSinglePlayerMode()) {
                    gameModel.getP2().shoot(null, null, gameModel.getGravityMode(), false);
                } else {
                    Weapon weaponToShoot = WeaponCLI.askWeaponToShoot(this.cli, gameModel.getP2());

                    if (weaponToShoot instanceof Laser) {
                        rowOrColumn = CoordinateCLI.askRowOrColumnToShoot(this.cli, gameModel.getP2(), gameModel.getGrid1());
                        coordinateToShoot = CoordinateCLI.askLaserCoordinateToShoot(this.cli, gameModel.getP2(), gameModel.getGrid1(), rowOrColumn);
                        gameModel.getP2().shootLaser(coordinateToShoot, rowOrColumn, (Laser) weaponToShoot);

                    } else {
                        coordinateToShoot = CoordinateCLI.askCoordinateToShoot(this.cli, gameModel.getP2(), gameModel.getGrid1());
                        gameModel.getP2().shoot(coordinateToShoot, weaponToShoot, gameModel.getGravityMode(), false);
                    }
                }

                if (gameModel.getP1().areAllShipsSunk(gameModel.getP1().getShips())) {
                    endGame(gameModel.getP2());
                    return;
                }
            }
            if (gameModel.getPlayerTurn() == 1)
                gameModel.setPlayerTurn(2);
            else
                gameModel.setPlayerTurn(1);
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