package com.galactica.controller;


import com.galactica.cli.*;
import com.galactica.model.*;

public class BattleshipCLI extends Game {

    private final CLI cli = new CLI();


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
            singlePlayerMode = cli.getPlayerModeResponse();
            gridSize = cli.getGridSizeResponse();
            if (gridSize >= 10) {
                gravityMode = cli.getGravityModeResponse();
            }
            asteroidMode = cli.getAsteroidModeResponse();
    
            grid1 = Game.setUpGrid(gridSize, singlePlayerMode, asteroidMode, gravityMode);
            grid2 = Game.setUpGrid(gridSize, singlePlayerMode, asteroidMode, gravityMode);
    
    
    
            p1 = new Human("Space Cowboy", grid1, grid2);
            placeShips(p1);
    
            if (singlePlayerMode) {
                p2 = new AI("Megatron", grid2, grid1);
                placeShips((AI) p2);
            } else {
                p2 = new Human("Rocket Rancher",grid2, grid1);
                placeShips((Human) p2);
            }
        } else {
            load(getDefaultPath());
        }
       

        Coordinate coordinateToShoot;
        char rowOrColumn;

        playerTurn = 1;
        while (true) {

            if (playerTurn == 1) {

                Weapon weaponsToShoot = WeaponCLI.askWeaponToShoot(this.cli, p1);

                if (weaponsToShoot instanceof Laser) {
                    rowOrColumn = CoordinateCLI.askRowOrColumnToShoot(this.cli, p1, grid2);
                    coordinateToShoot = CoordinateCLI.askLaserCoordinateToShoot(this.cli, p1, grid2, rowOrColumn);
                    p1.shootLaser(coordinateToShoot, rowOrColumn, (Laser) weaponsToShoot);

                } else {
                    coordinateToShoot = CoordinateCLI.askCoordinateToShoot(this.cli, p1, grid2);
                    p1.shoot(coordinateToShoot, weaponsToShoot, gravityMode, false);
                }

                if (p2.areAllShipsSunk(p2.getShips())) {
                    endGame(p1);
                    return;
                }
                save();

            } else {

                if (singlePlayerMode) {
                    p2.shoot(null, null, gravityMode, false);
                } else {
                    Weapon weaponToShoot = WeaponCLI.askWeaponToShoot(this.cli, p2);

                    if (weaponToShoot instanceof Laser) {
                        rowOrColumn = CoordinateCLI.askRowOrColumnToShoot(this.cli, p2, grid1);
                        coordinateToShoot = CoordinateCLI.askLaserCoordinateToShoot(this.cli, p2, grid1, rowOrColumn);
                        p2.shootLaser(coordinateToShoot, rowOrColumn, (Laser) weaponToShoot);

                    } else {
                        coordinateToShoot = CoordinateCLI.askCoordinateToShoot(this.cli, p2, grid1);
                        p2.shoot(coordinateToShoot, weaponToShoot, gravityMode, false);
                    }
                }

                if (p1.areAllShipsSunk(p1.getShips())) {
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
        BattleshipCLI game = new BattleshipCLI();
        game.playGame();
    }

    void endGame(Player winner) {
        System.out.println("Congratulations " + winner.getName() + "! 🍾🎉");

        this.cli.scanner.close();
    }

}
