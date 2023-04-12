package com.galactica.model;

import java.util.ArrayList;
import java.util.List;

public class Human extends Player{
    public static char intToChar(int num) {
        return (char) num;
    }

    public Human(Grid ownGrid, Grid opponentGrid) {
        super(ownGrid, opponentGrid);
        System.out.println("Enter player name: ");
        this.name = Player.sc.nextLine();
        //this.name = "Joe"; // TODO make compatible with cucumber tests
    }

    public void shoot(Coordinate coordinate, Weapon weaponToShoot) {

        int damageArea = weaponToShoot.getAreaOfEffect();

        if (damageArea == 1) {
            opponentGrid.setTile(coordinate, true);

            Asteroid asteroidAtCoordinate = opponentGrid.getAsteroidAtCoordinate(coordinate);
            Ship shipAtCoordinate = opponentGrid.getShipAtCoordinate(coordinate);

            if ((shipAtCoordinate != null || asteroidAtCoordinate != null)) { //cannon
                if (shipAtCoordinate != null) {
                    boolean isShipSunk = opponentGrid.checkIfShipIsSunk(shipAtCoordinate);
                    if (isShipSunk) {
                        shipAtCoordinate.setSunk(true);
                        System.out.println("You sunk a ship! 💥🚢");
                    } else
                        System.out.println("You hit something!");
                }
            } else
                System.out.println("You missed :(");
        } else if (damageArea == 2) {

            List<Coordinate> coordinateList = new ArrayList<Coordinate>();
            boolean hitAtLeastOneShip = false;

            for (int i = -1; i < 2; i = i + 1) { //x coordinate
                for (int j = -1; j < 2; j = j + 1) { //y coordinate

                    char newX = (char) (coordinate.getX() + j);
                    int newY = coordinate.getY() + i;
                    System.out.println(newX);
                    System.out.println(newY);

                    System.out.println("Problem is here");
                    Coordinate newCoordinate = new Coordinate(newX, newY);
                    if (opponentGrid.isValidCoordinate(newCoordinate)) {// make into a seperate method

                        coordinateList.add(newCoordinate);
                    }
                }
            }

            for (int i = 0; i < coordinateList.size(); i++) {
                opponentGrid.setTile(coordinateList.get(i), true);

                Asteroid asteroidAtCoordinate = opponentGrid.getAsteroidAtCoordinate(coordinateList.get(i));
                Ship shipAtCoordinate = opponentGrid.getShipAtCoordinate(coordinateList.get(i));
                System.out.println(coordinateList.get(i).getX());
                System.out.println(coordinateList.get(i).getY());

                if ((shipAtCoordinate != null || asteroidAtCoordinate != null)) { //grenade
                    hitAtLeastOneShip = true;
                    if (shipAtCoordinate != null) {
                        boolean isShipSunk = opponentGrid.checkIfShipIsSunk(shipAtCoordinate);
                        if (isShipSunk) {
                            shipAtCoordinate.setSunk(true);
                            System.out.println("You sunk a ship! 💥🚢");
                        }
                    }
                }
            }
            if (hitAtLeastOneShip) {
                System.out.println("You hit something!");
            } else {
                System.out.println("You missed all shots:(");
            }

        }
    }
}

