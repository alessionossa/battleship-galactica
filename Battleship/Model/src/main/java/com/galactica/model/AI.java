package com.galactica.model;

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
                shootCannon(coordinate, gravityMode, gravityUsed);
            } else {
                printShootingTurn(coordinate, weapon, ' ');
                shootGrenade(coordinate, (Grenade) weapon);
            }
        }
    }

    public void updateTracking(Coordinate coordinate) {
        lastCoordinate = coordinate;
        followTargetMode = true;
        Right = true;
    }

    void updateCoordinatesHit(List<Coordinate> coordinateList) {
        for (Coordinate coordinate : coordinateList) {
            CoordinatesHit.add(coordinate);
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
        char x0 = (char) (random.nextInt(ownGrid.getGridSize()) + 'a');
        int y0 = random.nextInt(ownGrid.getGridSize() + 1);
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

    public char getRandomWeapon() {
        int probability = random.nextInt(100);
        if (probability < 5) { // 5% chance to choose laser
            return 'l';
        } else if (probability < 65) { // 80% chance to choose cannon
            return 'c';
        } else { // 15% chance to choose grenade
            return 'g';
        }
    }

    public Weapon getWeaponToShoot() {
        for (;;) {
            char resp = getRandomWeapon();

            if (resp == 'c') {
                return cannon;
            } else if (resp == 'g' && grenade.getAmountOfUses() != 0) {
                grenade.decrementAmountOfUses();
                return grenade;
            } else if (resp == 'l' && laser.getAmountOfUses() != 0) {
                laser.decrementAmountOfUses();
                return laser;
            }
        }

    }
}
