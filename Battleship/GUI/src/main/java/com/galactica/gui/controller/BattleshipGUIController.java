package com.galactica.gui.controller;

import com.galactica.model.*;

public class BattleshipGUIController {

    private Grid ownGrid;
    private Grid opponentGrid;

    private Human p1;
    private Player p2;
    private int playerTurn;
    private int winner;

    public void startGame(int gridSize, boolean singlePlayer, boolean asteroids, boolean gravity) {

        // Setting up the grids with the selected functionalities
        ownGrid = Game.setUpGrid(gridSize, singlePlayer, asteroids, gravity);
        opponentGrid = Game.setUpGrid(gridSize, singlePlayer, asteroids, gravity);

        // Set up players
        p1 = new Human(ownGrid, opponentGrid);
        if (singlePlayer) {
            p2 = new AI("CPU", ownGrid, opponentGrid);
        } else {
            p2 = new Human(ownGrid, opponentGrid);
        }
        playerTurn = 1;
    }

    public String removeShip(Human player, Ship ship) {
        String result = null;
        if (!ship.isPlaced()) {
            result = "Error: Cannot remove a ship that hasn't been placed";
        } else {
            try {
                player.removeShip(ship);
                result = "Ship removed successfully";
            } catch (UnplacedShipException e) {

            }
        }
        return result;
    }

    public String placeShip(Human player, Ship ship, Coordinate coordinate, Direction direction) {
        String result = null;
        if (!ship.isPlaced()) {
            Ship placedShip;
            placedShip = player.placeShip(ship, coordinate, direction);
            if (placedShip == null)
                result = "Error: You cannot place a ship here.";
            else
                result = "Placed ship successfully";
            if (player.hasAllShipsPlaced())
                result = "All ships already placed";
        }
        return result;
    }

    public void placeShips(AI player) {
        player.placeShips();
    }

    public void shoot(Coordinate coordinateToShoot, char rowOrColumn, Weapon weapon, boolean gravityMode,
            boolean singlePlayer) {
        if (playerTurn == 1) {
            if (weapon.getAreaOfEffect() != 3) {
                p1.shoot(coordinateToShoot, weapon, gravityMode, false);
            } else {
                p1.shootLaser(coordinateToShoot, rowOrColumn, (Laser) weapon);
            }
        } else {
            if (singlePlayer) {
                p2.shoot(null, null, gravityMode, false);
            } else {
                if (weapon.getAreaOfEffect() != 3) {
                    p2.shoot(coordinateToShoot, weapon, gravityMode, false);
                } else {
                    p2.shootLaser(coordinateToShoot, rowOrColumn, (Laser) weapon);
                }
            }
        }
    }

    public boolean allOponnentsShipsSunkAndEndGame() {
        boolean endGame = false;
        if (playerTurn == 1) {
            if (p2.areAllShipsSunk()) {
                endGame = true;
                winner = 1;
            }
        } else {
            if (p1.areAllShipsSunk()) {
                endGame = true;
                winner = 2;
            }
        }
        return endGame;
    }

    public int getWinner() {
        return winner;
    }

    public void changeTurn() {
        if (this.playerTurn == 1)
            this.playerTurn = 2;
        else
            this.playerTurn = 1;
    }
}
