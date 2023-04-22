package com.galactica.model;

import java.util.ArrayList;
import java.util.List;


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

    public void shoot(Coordinate coordinate, Weapon weaponToShoot, boolean gravityMode, boolean gravityUsed) {
        int damageArea = weaponToShoot.getAreaOfEffect();

        if (damageArea == 1) {
            shootCannon(coordinate, gravityMode, gravityUsed);
        } else if (damageArea == 2) {
            shootGrenade(coordinate, (Grenade) weaponToShoot);
        }

    }

    public void shootLaser(Coordinate coordinate, char rowOrColumn, Laser laser) {
        List<Coordinate> coordinateList = laser.getLaserCoordinates(coordinate, opponentGrid, rowOrColumn);
        boolean hitPlanet = false;
        if (coordinateList.size() < ownGrid.getGridSize())
            hitPlanet = true;
        checkOutcomeOfShot(coordinateList, hitPlanet);
    }

    private void shootGrenade(Coordinate coordinate, Grenade grenade) {
        List<Coordinate> coordinateList = grenade.getScatterCoordinates(coordinate, opponentGrid);
        checkOutcomeOfShot(coordinateList, false);
    }

    private void shootCannon(Coordinate coordinate, boolean gravityMode, boolean gravityUsed) {
        opponentGrid.setTile(coordinate, true);
        List<Coordinate> coordinateList = new ArrayList<Coordinate>();
        coordinateList.add(coordinate);

        boolean hit = checkOutcomeOfShot(coordinateList, false);

        if (!hit && gravityMode && !gravityUsed) {
            List<Planet> opponentPlanets = opponentGrid.getPlanets();
            for (Planet planet : opponentPlanets) {
                if (!gravityUsed) {
                    Coordinate rebound = planet.getPlanetRebound(coordinate);
                    if (rebound != null) {
                        shoot(rebound, new Cannon(), true, true);
                    }
                }
            }
        }
    }

    private boolean checkOutcomeOfShot(List<Coordinate> coordinateList, boolean hitPlanet) {
        boolean hitAtLeastOneShip = false;
        boolean hitSomething = true;
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
                        hitAtLeastOneShip = false;
                    }
                }
            }
        }
        if (hitAtLeastOneShip) {
            System.out.println("You hit something!");
        } else {
            hitSomething = false;
            System.out.println("You missed all shots:(");
        }
        return hitSomething;
    }

}
