package com.galactica.model;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import com.galactica.cli.AICLI;

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
                    automaticShooting(newCoordinate, 'R', 'L', gravityMode, gravityUsed); // Shoots if possible
                                                                                          // otherwise goes to the next
                    // direction

                } else if (Left) {
                    directionToMove('h', '-');
                    Coordinate newCoordinate = getNewCoordinate(lastCoordinate);
                    automaticShooting(newCoordinate, 'L', 'U', gravityMode, gravityUsed);

                } else if (Up) {
                    directionToMove('v', '-');
                    Coordinate newCoordinate = getNewCoordinate(lastCoordinate);
                    automaticShooting(newCoordinate, 'U', 'D', gravityMode, gravityUsed);

                } else if (Down) {
                    directionToMove('v', '+');
                    Coordinate newCoordinate = getNewCoordinate(lastCoordinate);
                    automaticShooting(newCoordinate, 'D', 'R', gravityMode, gravityUsed);
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
                AICLI.printShootingTurn(this,coordinate, weapon, rowOrColumn);
                shootLaser(coordinate, rowOrColumn, (Laser) weapon);
            } else if (weapon instanceof Cannon) {
                AICLI.printShootingTurn(this,coordinate, weapon, ' ');
                shootCannon(coordinate, gravityMode, gravityUsed);
            } else {
                AICLI.printShootingTurn(this,coordinate, weapon, ' ');
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

    public void resetTracking() {
        followTargetMode = false;
        Right = false;
        Left = false;
        Up = false;
        Down = false;
        Moves[0] = 0;
        Moves[1] = 0;

    }

    public void automaticShooting(Coordinate newCoordinate, char direction, char nextDirection, boolean gravityMode,
            boolean gravityUsed) {
        if (opponentGrid.isValidCoordinate(newCoordinate) && !CoordinatesHit.contains(newCoordinate)) {
            boolean hitSomething = shootCannon(newCoordinate, gravityMode, gravityUsed);
            hasShot = true;
            if (hitSomething == false) {
                if (direction == 'D') {
                    resetTracking();
                } else {
                    Moves[0] = 0;
                    Moves[1] = 0;
                    nextDirection(direction, nextDirection);
                }
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
                return grenade;
            } else if (resp == 'l' && laser.getAmountOfUses() != 0) {
                return laser;
            }
        }

    }
}
