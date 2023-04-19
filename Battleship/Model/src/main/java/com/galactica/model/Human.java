package com.galactica.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Human extends Player {
    public static char intToChar(int num) {
        return (char) num;
    }

    public Human(Grid ownGrid, Grid opponentGrid) {
        super(ownGrid, opponentGrid);
        // System.out.println("Enter player name: ");
        // this.name = Player.sc.nextLine();
        this.name = "Joe"; // TODO make compatible with cucumber tests
    }

    public void shoot(Coordinate coordinate, Weapon weaponToShoot) {
        int damageArea = weaponToShoot.getAreaOfEffect();

        if (damageArea == 1) {
            shootCannon(coordinate);
        } else if (damageArea == 2) {
            shootGrenade(coordinate);
        }
    }

    public void shootLaser(Coordinate coordinate, char rowOrColumn) {
        List<Coordinate> coordinateList = new ArrayList<Coordinate>();
        boolean hitAtLeastOneShip = false;
        boolean hitPlanet = false;

        if (rowOrColumn == 'r') {
            for (int i = 0; i < opponentGrid.getGridSize(); i++) {
                char newX = (char) (coordinate.getX() + i);
                int newY = coordinate.getY();
                Coordinate newCoordinate = new Coordinate(newX, newY);
                if (hitPlanet == false)
                    addToCoordinateList(coordinateList, newCoordinate);
                if (opponentGrid.getPlanetAtCoordinate(newCoordinate) != null) {
                    hitPlanet = true;
                }

            }
        } else { // column
            for (int i = 0; i < opponentGrid.getGridSize(); i++) {
                char newX = (char) (coordinate.getX());
                int newY = coordinate.getY() + i;
                Coordinate newCoordinate = new Coordinate(newX, newY);
                if (hitPlanet == false)
                    addToCoordinateList(coordinateList, newCoordinate);
                if (opponentGrid.getPlanetAtCoordinate(newCoordinate) != null) {
                    hitPlanet = true;
                }

            }
        }
        checkOutcomeOfShot(coordinateList, hitAtLeastOneShip, hitPlanet);
    }

    private void shootGrenade(Coordinate coordinate) {
        Random random = new Random();
        List<Coordinate> coordinateList = new ArrayList<Coordinate>();
        boolean hitAtLeastOneShip = false;
        char newX = (char) (coordinate.getX());
        int newY = coordinate.getY();
        Coordinate newCoordinate = new Coordinate(newX, newY);
        addToCoordinateList(coordinateList, newCoordinate);

        for (int i = 0; i < 9; i++) {
            int randomInt1 = random.nextInt(3) - 1;
            int randomInt2 = random.nextInt(3) - 1;
            newX = (char) (coordinate.getX() + randomInt1);
            newY = coordinate.getY() + randomInt2;

            newCoordinate = new Coordinate(newX, newY);
            addToCoordinateList(coordinateList, newCoordinate);
        }
        checkOutcomeOfShot(coordinateList, hitAtLeastOneShip, false);
    }

    private void shootCannon(Coordinate coordinate) {
        opponentGrid.setTile(coordinate, true);

        Asteroid asteroidAtCoordinate = opponentGrid.getAsteroidAtCoordinate(coordinate);
        Ship shipAtCoordinate = opponentGrid.getShipAtCoordinate(coordinate);
        Planet planetAtCoordinate = opponentGrid.getPlanetAtCoordinate(coordinate);

        if (shipAtCoordinate != null) {
            boolean isShipSunk = opponentGrid.checkIfShipIsSunk(shipAtCoordinate);
            if (isShipSunk) {
                shipAtCoordinate.setSunk(true);
                System.out.println("You sunk a ship! 💥🚢");
            } else
                System.out.println("You hit something!");
        } else if (asteroidAtCoordinate != null) {
            System.out.println("You hit something!");
        } else if (planetAtCoordinate != null) {
            System.out.println("That was a planet!");
        } else {
            System.out.println("You missed :(");
        }

        // if (gravityMode && !gravityUsed) {
        // List<Planet> opponentPlanets = opponentGrid.getPlanets();
        // for (Planet planet : opponentPlanets) {
        // Coordinate rebound = planet.getPlanetRebound(coordinate);
        // if (rebound != null) {
        // shoot(rebound, true, true);
        // }
        // }
        // }
    }

    private void checkOutcomeOfShot(List<Coordinate> coordinateList, boolean hitAtLeastOneShip, boolean hitPlanet) {
        if (hitPlanet) {
            System.out.println("Your lasering was intercepted by a planet! 🌎");
        }
        for (int i = 0; i < coordinateList.size(); i++) {
            opponentGrid.setTile(coordinateList.get(i), true);

            Asteroid asteroidAtCoordinate = opponentGrid.getAsteroidAtCoordinate(coordinateList.get(i));
            Ship shipAtCoordinate = opponentGrid.getShipAtCoordinate(coordinateList.get(i));

            if ((shipAtCoordinate != null || asteroidAtCoordinate != null)) {
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

    private void addToCoordinateList(List<Coordinate> coordinateList, Coordinate newCoordinate) {
        if (opponentGrid.isValidCoordinate(newCoordinate)) {
            coordinateList.add(newCoordinate);
        }
    }

}
