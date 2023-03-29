package com.galactica.model;

import java.util.HashSet;
import java.util.Random;

public class AI extends Player {
    private Random random = new Random();
    private final char[] sequence = { 'v', 'h' };
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
        System.out.println("--------------------------------------------- ");
        System.out.println(name + " is placing the ships... \n");
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

    public void shoot() {
        Coordinate coordinate;
        boolean isValidCoordinate;

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

            do {
                coordinate = getNewRandomCoordinate();
                isValidCoordinate = opponentGrid.isValidCoordinate(coordinate);
            } while (!isValidCoordinate || CoordinatesHit.contains(coordinate));

            printShootingTurn(coordinate);

            CoordinatesHit.add(coordinate);
            lastCoordinate = coordinate;

            opponentGrid.setTile(coordinate, true);

            Ship shipAtCoordinate = opponentGrid.getShipAtCoordinate(coordinate);

            if (shipAtCoordinate != null) {
                System.out.println("The AI has hit a ship!");

                followTargetMode = true;
                Right = true;

                boolean isShipSunk = opponentGrid.checkIfShipIsSunk(shipAtCoordinate);
                sunkCheck(coordinate, shipAtCoordinate, isShipSunk);

            } else
                System.out.println("The AI has missed...");
        }
    }

    public void printShootingTurn(Coordinate coordinate) {
        System.out.println("\n-------------\nAI's turn to shoot:");
        System.out.println(name + ", is shooting...");
        String message = "The " + name + " has shot in: " + coordinate.getX() + "-" + coordinate.getY();
        System.out.println(message);
    }

    public Coordinate getNewRandomCoordinate() {
        char x0 = (char) (random.nextInt(10) + 'a');
        int y0 = random.nextInt(11);
        return new Coordinate(x0, y0);
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
        } else {
            // lastHitWasSuccesful = true;
        }
    }

    public void automaticShooting(Coordinate newCoordinate, char direction, char nextDirection) {
        System.out.println(newCoordinate.getX() + " " + newCoordinate.getY());
        if (opponentGrid.isValidCoordinate(newCoordinate) && !CoordinatesHit.contains(newCoordinate)) {

            printShootingTurn(newCoordinate);

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
                System.out.println("Interne " + direction + " " + nextDirection);
                System.out.println("Valori prima " + Right + " " + Left + " " + Up + " " + Down);
                nextDirection(direction, nextDirection);
                System.out.println("Valori dopo " + Right + " " + Left + " " + Up + " " + Down);

            }

        } else {
            nextDirection(direction, nextDirection);
        }
    }

}
