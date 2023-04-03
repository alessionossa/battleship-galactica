package com.galactica.controller;

import com.galactica.cli.*;
import com.galactica.model.*;

public class BattleshipCLI {
    private int playerTurn;
    private boolean asteroidMode;
    private boolean singlePlayerMode;

    private Player p1;
    private Player p2;

    private final CLI cli;

    public BattleshipCLI(CLI cli) {
        this.cli = cli;
    }

    void playGame() {
        Grid grid1 = new Grid();
        Grid grid2 = new Grid();

        asteroidMode = cli.getAsteroidModeResponse();
        if (asteroidMode) {
            grid1.placeAsteroids();
            grid2.placeAsteroids();
        }

        singlePlayerMode = cli.getPlayerModeResponse();
        p1 = new Human(grid1, grid2);
        placeShips(p1);

        if (singlePlayerMode) {
            // p2 = new AI("CPU", grid2, grid1);

        } else {
            p2 = new Human(grid2, grid1);
        }
        placeShips(p2);

        playerTurn = 1;
        while (true) {

            if (playerTurn == 1) {
                Coordinate coordinateToShoot = CoordinateCLI.askCoordinateToShoot(this.cli, p2, grid1);
                p1.shoot(coordinateToShoot);
                if (p2.areAllShipsSunk()) {
                    endGame(p1);
                    return;
                }

            } else {
                Coordinate coordinateToShoot = CoordinateCLI.askCoordinateToShoot(this.cli, p2, grid1);
                p2.shoot(coordinateToShoot);
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

    public void placeShips(Player player) {
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
                player.removeShip(ship);
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

    void endGame(Player winner) {
        System.out.println("Congratulations " + winner.getName() + "! 🍾🎉");

        this.cli.scanner.close();
    }
}
