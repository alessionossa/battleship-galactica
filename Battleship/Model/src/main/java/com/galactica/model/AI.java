package com.galactica.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import com.galactica.cli.GridCLI;

public class AI extends Player {
    private Random random = new Random();
    private HashSet<Coordinate> CoordinatesHit = new HashSet<Coordinate>();
    private boolean followTargetMode = false;
    private int[] Moves = { 0, 0 };
    private boolean hasShot;
    private boolean Right = false;
    private boolean Left = false;
    private boolean Up = false;
    private boolean Down = false;
    private Coordinate lastCoordinate;
    private Grid referenceGrid;

    public AI(String name, Grid ownGrid, Grid opponentGrid) {
        super(ownGrid, opponentGrid);
        this.name = name;
    }

    public void placeShips() {
        final char[] sequence = { 'v', 'h' };
        Random random = new Random();

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
                    ownGrid.placeShip(ship, coordinate, direction);

            } while (!isValidShipPosition);
        }
    }

    public void shoot(Coordinate c, Weapon weaponToShoot, boolean gravityMode, boolean gravityUsed) {
        Coordinate coordinate;
        boolean isValidCoordinate;
        Weapon weapon;

        if (followTargetMode) {
            hasShot = false;
            do {
                if (Right) {
                    directionToMove('h', '+'); // Updates the Moves vector
                    Coordinate newCoordinate = getNewCoordinate(lastCoordinate); // Creates the new set of coordinate
                    automaticShooting(newCoordinate, 'R', 'L'); // Shoots if possible otherwise goes to the next
                    // direction

                } else if (Left) {
                    directionToMove('h', '-');
                    Coordinate newCoordinate = getNewCoordinate(lastCoordinate);
                    automaticShooting(newCoordinate, 'L', 'U');

                } else if (Up) {
                    directionToMove('v', '-');
                    Coordinate newCoordinate = getNewCoordinate(lastCoordinate);
                    automaticShooting(newCoordinate, 'U', 'D');

                } else if (Down) {
                    directionToMove('v', '+');
                    Coordinate newCoordinate = getNewCoordinate(lastCoordinate);
                    automaticShooting(newCoordinate, 'D', 'R');
                }

            } while (!hasShot);

        } else {
            weapon = getWeaponToShoot();

            do {
                coordinate = getNewRandomCoordinate();
                isValidCoordinate = opponentGrid.isValidCoordinate(coordinate);
            } while (!isValidCoordinate || CoordinatesHit.contains(coordinate));

            if (weapon instanceof Laser) {
                char rowOrColumn = decideRowOrColumn();
                printShootingTurn(coordinate, weapon, rowOrColumn);
                shootLaser(coordinate, rowOrColumn, (Laser) weapon);
            } else if (weapon instanceof Cannon) {
                printShootingTurn(coordinate, weapon, ' ');
                shootCannon(coordinate);
            } else {
                printShootingTurn(coordinate, weapon, ' ');
                shootGrenade(coordinate, (Grenade) weapon);
            }
        }
    }

    private void shootCannon(Coordinate coordinate) {
        CoordinatesHit.add(coordinate);
        lastCoordinate = coordinate;

        opponentGrid.setTile(coordinate, true);

        Ship shipAtCoordinate = opponentGrid.getShipAtCoordinate(coordinate);
        Asteroid asteroidAtCoordinate = opponentGrid.getAsteroidAtCoordinate(coordinate);

        if ((shipAtCoordinate != null || asteroidAtCoordinate != null)) {
            if (shipAtCoordinate != null) {
                System.out.println("The AI has hit a ship!");

                followTargetMode = true;
                Right = true;

                boolean isShipSunk = opponentGrid.checkIfShipIsSunk(shipAtCoordinate);
                sunkCheck(coordinate, shipAtCoordinate, isShipSunk);
            } else
                System.out.println("The AI has hit an asteroid!");
        } else
            System.out.println("The AI has missed...");
    }

    private void shootGrenade(Coordinate coordinate, Grenade grenade) {
        List<Coordinate> coordinateList = grenade.getScatterCoordinates(coordinate, opponentGrid);

        updateCoordinatesHit(coordinateList, CoordinatesHit);
        checkOutcomeOfShot(coordinateList);
    }

    public void shootLaser(Coordinate coordinate, char rowOrColumn, Laser laser) {
        List<Coordinate> coordinateList = laser.getLaserCoordinates(coordinate, opponentGrid, rowOrColumn);

        updateCoordinatesHit(coordinateList, CoordinatesHit);
        checkOutcomeOfShot(coordinateList);
    }

    private void checkOutcomeOfShot(List<Coordinate> coordinateList) {
        boolean hitAShip = false;
        boolean hitAnAsteroid = false;

        for (int i = 0; i < coordinateList.size(); i++) {
            opponentGrid.setTile(coordinateList.get(i), true);

            Asteroid asteroidAtCoordinate = opponentGrid.getAsteroidAtCoordinate(coordinateList.get(i));
            Ship shipAtCoordinate = opponentGrid.getShipAtCoordinate(coordinateList.get(i));

            if ((shipAtCoordinate != null || asteroidAtCoordinate != null)) {
                if (shipAtCoordinate != null) {
                    System.out.println("The AI has hit something!");
                    lastCoordinate = coordinateList.get(i);
                    hitAShip = true;
                    followTargetMode = true;
                    Right = true;
                    boolean isShipSunk = opponentGrid.checkIfShipIsSunk(shipAtCoordinate);
                    sunkCheck(coordinateList.get(i), shipAtCoordinate, isShipSunk);
                } else if (asteroidAtCoordinate != null)
                    hitAnAsteroid = true;
                System.out.println("The AI has hit something!");
            }
        }
        if (!(hitAShip || hitAnAsteroid))
            System.out.println("The AI has missed all the shots:(");
    }

    private void updateCoordinatesHit(List<Coordinate> coordinateList, HashSet<Coordinate> coordinatesHit) {
        for (Coordinate coordinate : coordinateList) {
            coordinatesHit.add(coordinate);
        }
    }

    public void printShootingTurn(Coordinate coordinate, Weapon weapon, char c) {
        System.out.println("\n-------------\nAI's turn to shoot:");
        if (weapon instanceof Laser)
            System.out.println(name + ", is shooting a laser...");
        if (c == 'r') {
            String message = "The " + name + " has shot in row: " + coordinate.getY();
            System.out.println(message);
        } else if (c == 'c') {
            String message = "The " + name + " has shot in column: " + coordinate.getX();
            System.out.println(message);
        }

        else if (weapon instanceof Cannon) {
            System.out.println(name + ", is shooting with the cannon...");
            String message = "The " + name + " has shot in: " + coordinate.getX() + "-" + coordinate.getY();
            System.out.println(message);
        } else {
            System.out.println(name + ", is shooting a grenade...");
            String message = "The " + name + " has shot in: " + coordinate.getX() + "-" + coordinate.getY();
            System.out.println(message);
        }
        GridCLI.printGrid(opponentGrid, false);
    }

    public Coordinate getNewRandomCoordinate() {
        char x0 = (char) (random.nextInt(referenceGrid.getGridSize()) + 'a');
        int y0 = random.nextInt(referenceGrid.getGridSize() + 1);
        return new Coordinate(x0, y0);
    }

    public char decideRowOrColumn() {
        int random = this.random.nextInt(2);
        if (random == 0)
            return 'r';
        else
            return 'c';
    }

    public Coordinate getNewCoordinate(Coordinate lastCoordinate) {
        char XNew = (char) (lastCoordinate.getX() + Moves[0]);
        int YNew = lastCoordinate.getY() + Moves[1];
        return new Coordinate(XNew, YNew);
    }

    public void directionToMove(char direction, char sign) {
        if (direction == 'v' && sign == '+')
            Moves[1] += 1;
        else if (direction == 'v' && sign == '-')
            Moves[1] -= 1;
        else if (direction == 'h' && sign == '+')
            Moves[0] += 1;
        else if (direction == 'h' && sign == '-')
            Moves[0] -= 1;
    }

    public void nextDirection(char direction, char nextDirection) {
        if (direction == 'R' && nextDirection == 'L') {
            this.Right = false;
            this.Left = true;
        } else if (direction == 'L' && nextDirection == 'U') {
            this.Left = false;
            this.Up = true;
        } else if (direction == 'U' && nextDirection == 'D') {
            this.Up = false;
            this.Down = true;
        } else if (direction == 'D' && nextDirection == 'R') {
            this.Down = false;
            this.Right = true;
        }
    }

    public void sunkCheck(Coordinate coordinate, Ship shipAtCoordinate, boolean isShipSunk) {
        if (isShipSunk) {
            shipAtCoordinate.setSunk(true);
            System.out.println("The AI has sunk a ship! 💥🚢");
            followTargetMode = false;
            Right = false;
            Left = false;
            Up = false;
            Down = false;
            Moves[0] = 0;
            Moves[1] = 0;
        }
    }

    public void automaticShooting(Coordinate newCoordinate, char direction, char nextDirection) {
        if (opponentGrid.isValidCoordinate(newCoordinate) && !CoordinatesHit.contains(newCoordinate)) {
            Weapon w = new Cannon();
            printShootingTurn(newCoordinate, w, ' ');

            CoordinatesHit.add(newCoordinate);
            hasShot = true;

            opponentGrid.setTile(newCoordinate, true);
            Ship shipAtCoordinate = opponentGrid.getShipAtCoordinate(newCoordinate);

            if (shipAtCoordinate != null) {
                System.out.println("The AI has hit a ship!");

                boolean isShipSunk = opponentGrid.checkIfShipIsSunk(shipAtCoordinate);
                sunkCheck(newCoordinate, shipAtCoordinate, isShipSunk);
            } else {
                System.out.println("The AI has missed...");
                Moves[0] = 0;
                Moves[1] = 0;
                nextDirection(direction, nextDirection);
            }

        } else {
            nextDirection(direction, nextDirection);
            Moves[0] = 0;
            Moves[1] = 0;
        }
    }

    public String getRandomWeapon() {
        int probability = random.nextInt(100);
        if (probability < 5) { // 5% chance to choose laser
            return "laser";
        } else if (probability < 65) { // 80% chance to choose cannon
            return "cannon";
        } else { // 15% chance to choose grenade
            return "grenade";
        }
    }

    public Weapon getWeaponToShoot() {
        boolean isValidWeaponToShoot;
        int whichWeaponToReturn = 0;
        do {
            String resp = getRandomWeapon();

            if (resp.equals("cannon")) {
                isValidWeaponToShoot = true;
                whichWeaponToReturn = 0;
            } else if (resp.equals("grenade") && getWeapons()[1].getAmountOfUses() != 0) {
                getWeapons()[1].setAmountOfUses();
                whichWeaponToReturn = 1;
                isValidWeaponToShoot = true;
            } else if (resp.equals("laser") && getWeapons()[2].getAmountOfUses() != 0) {
                getWeapons()[2].setAmountOfUses();
                System.out.println(getWeapons()[2].getAmountOfUses());
                whichWeaponToReturn = 2;
                isValidWeaponToShoot = true;
            } else {
                isValidWeaponToShoot = false;
            }
        } while (!isValidWeaponToShoot);

        return getWeapons()[whichWeaponToReturn];
    }

    private void addToCoordinateList(List<Coordinate> coordinateList, Coordinate newCoordinate) {
        if (opponentGrid.isValidCoordinate(newCoordinate)) {
            coordinateList.add(newCoordinate);
        }
    }
}
