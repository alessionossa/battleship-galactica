package com.galactica.model;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import com.galactica.cli.AICLI;
import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;

// AI class representing the Artificial Intelligence player in the game
public class AI extends Player {
    private Random random = new Random();
    // Fields for AI player's state
    private HashSet<Coordinate> CoordinatesHit = new HashSet<Coordinate>();
    private static boolean followTargetMode = false;
    private int[] Moves = { 0, 0 };
    private boolean hasShot;
    private boolean Right = false;
    private boolean Left = false;
    private boolean Up = false;
    private boolean Down = false;
    private Coordinate lastCoordinate;
    private int switchDirection;

    // Constructor for AI player
    public AI(String name, Grid ownGrid, Grid opponentGrid) {
        super(ownGrid, opponentGrid);
        this.name = name;
    }

    // Full constructor for AI player with all fields
    public AI(String name, Grid ownGrid, Grid opponentGrid, List<Ship> ships, Laser laser, Grenade grenade,
            boolean followTargetMode, int[] Moves, boolean hasShot, boolean Right, boolean Left, boolean Up,
            boolean Down, Coordinate lastCoordinate, int switchDirection, HashSet<Coordinate> CoordinatesHit) {
        super(ownGrid, opponentGrid);
        this.name = name;
        this.ships = ships;
        this.laser = laser;
        this.grenade = grenade;
        this.cannon = new Cannon();
        AI.followTargetMode = followTargetMode;
        this.Moves = Moves;
        this.hasShot = hasShot;
        this.Right = Right;
        this.Left = Left;
        this.Up = Up;
        this.Down = Down;
        this.lastCoordinate = lastCoordinate;
        this.switchDirection = switchDirection;
        this.CoordinatesHit = CoordinatesHit;

    }
    // Method to randomly place ships for the AI player
    public void placeShips() {
        final char[] sequence = { 'v', 'h' };
        Random random = new Random();

        for (Ship ship : ships) {
            boolean isValidShipPosition;

            do {
                Coordinate coordinate;
                boolean isValidCoordinate = false;
                do {
                    char x0 = (char) (random.nextInt(this.getOwnGrid().getGridSize()) + 'a');
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
   
    // AI shooting method
    public void shoot(Coordinate c, Weapon weaponToShoot, boolean gravityMode, boolean gravityUsed) {
        Coordinate coordinate;
        boolean isValidCoordinate;
        Weapon weapon;
        // Implementation of the AI shooting logic
        if (followTargetMode) {
            hasShot = false;
            do {
                if (Right) {
                    directionToMove('h', '+'); // Updates the Moves vector
                    Coordinate newCoordinate = getNewCoordinate(lastCoordinate); // Creates the new set of coordinate
                    boolean result = automaticShooting(newCoordinate, 'R', 'L', gravityMode, gravityUsed); // Shoots if
                                                                                                           // possible
                                                                                                           // otherwise
                                                                                                           // goes to
                                                                                                           // the next
                                                                                                           // direction
                    if (result)
                        break;

                } else if (Left) {
                    directionToMove('h', '-');
                    Coordinate newCoordinate = getNewCoordinate(lastCoordinate);
                    boolean result = automaticShooting(newCoordinate, 'L', 'U', gravityMode, gravityUsed);
                    if (result)
                        break;

                } else if (Up) {
                    directionToMove('v', '-');
                    Coordinate newCoordinate = getNewCoordinate(lastCoordinate);
                    boolean result = automaticShooting(newCoordinate, 'U', 'D', gravityMode, gravityUsed);
                    if (result)
                        break;

                } else if (Down) {
                    directionToMove('v', '+');
                    Coordinate newCoordinate = getNewCoordinate(lastCoordinate);
                    boolean result = automaticShooting(newCoordinate, 'D', 'R', gravityMode, gravityUsed);
                    if (result)
                        break;
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
                AICLI.printShootingTurn(this, coordinate, weapon, rowOrColumn);
                shootLaser(coordinate, rowOrColumn, (Laser) weapon);
            } else if (weapon instanceof Cannon) {
                AICLI.printShootingTurn(this, coordinate, weapon, ' ');
                shootCannon(coordinate, gravityMode, gravityUsed);
            } else {
                AICLI.printShootingTurn(this, coordinate, weapon, ' ');
                shootGrenade(coordinate, (Grenade) weapon);
            }
        }
    }
    
    // Other methods to help AI make decisions and keep track of its state
    public void updateTracking(Coordinate coordinate) {
        lastCoordinate = coordinate;
        followTargetMode = true;
        Right = true;
        Left = false;
        Up = false;
        Down = false;
        switchDirection = 0;
        Moves[0] = 0;
        Moves[1] = 0;
    }

    void updateCoordinateHit(Coordinate coordinate) {
        CoordinatesHit.add(coordinate);
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
        switchDirection++;
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
        switchDirection = 0;
        Moves[0] = 0;
        Moves[1] = 0;
    }

    public boolean automaticShooting(Coordinate newCoordinate, char direction, char nextDirection, boolean gravityMode,
            boolean gravityUsed) {
        if (opponentGrid.isValidCoordinate(newCoordinate) && !CoordinatesHit.contains(newCoordinate)) {
            AICLI.printShootingTurn(this, newCoordinate, cannon, ' ');
            boolean hitSomething = shootCannon(newCoordinate, gravityMode, gravityUsed);
            hasShot = true;
            if (hitSomething == false) {
                if (switchDirection == 3) {
                    resetTracking();
                } else {
                    Moves[0] = 0;
                    Moves[1] = 0;
                    nextDirection(direction, nextDirection);
                }
            }

        } else {
            if (switchDirection == 3) {
                // hasShot = true
                resetTracking();
                return true;
            } else {
                Moves[0] = 0;
                Moves[1] = 0;
                nextDirection(direction, nextDirection);
            }
        }
        return false;
    }

    public char getRandomWeapon() {
        int probability = random.nextInt(100);
        if (probability < 5) { // 5% chance to choose laser
            return 'l';
        } else if (probability < 75) { // 70% chance to choose cannon
            return 'c';
        } else { // 25% chance to choose grenade
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

    public static boolean getFollowTragetMode() {
        return followTargetMode;
    }

    // Method to convert HashSet of Coordinates to JsonArray
    public JsonArray toJsonArray(HashSet<Coordinate> CoordinatesHit) {
        JsonArray ja = new JsonArray();
        for (Coordinate coordinate : CoordinatesHit) {
            ja.add(coordinate.toJsonObject());
        }
        return ja;
    }

    // Method to convert int array to JsonArray
    public JsonArray toJsonArray(int[] Moves) {
        JsonArray ja = new JsonArray();
        for (int i : Moves) {
            ja.add(i);
        }
        return ja;
    }
    // Create a JsonObject and initialize with the superclass's toJson method
    public JsonObject toJsonObject() {
        JsonObject jo = super.toJsonObject();
        // Add CoordinatesHit as a JsonArray to the JsonObject
        jo.put("CoordinatesHit", toJsonArray(CoordinatesHit));
        // Add hasShot as a boolean to the JsonObject
        jo.put("hasShot", hasShot);
        // Add followTargetMode as a boolean to the JsonObject
        jo.put("followTargetMode", followTargetMode);
        // Add Right as a boolean to the JsonObject
        jo.put("Right", Right);
        // Add Left as a boolean to the JsonObject
        jo.put("Left", Left);
        // Add Up as a boolean to the JsonObject
        jo.put("Up", Up);
        // Add Down as a boolean to the JsonObject
        jo.put("Down", Down);
        // Add switchDirection as an int to the JsonObject
        jo.put("switchDirection", switchDirection);
        // Add Moves as a JsonArray to the JsonObject
        jo.put("Moves", toJsonArray(Moves));
        // If lastCoordinate is not null, add it as a JsonObject, otherwise add a null value
        if (lastCoordinate != null)
            jo.put("lastCoordinate", lastCoordinate.toJsonObject());
        else
            jo.put("lastCoordinate", null);
        // Return the JsonObject
        return jo;
    }

    public static HashSet<Coordinate> fromJsonArrayToHashSetOfCoordinates(JsonArray ja) {
        // Create a HashSet to store Coordinate objects
        HashSet<Coordinate> CoordinatesHit = new HashSet<Coordinate>();
        // Iterate through the JsonArray, convert each JsonObject to a Coordinate object, and add to the HashSet
        for (Object object : ja) {
            Coordinate coordinate = Coordinate.fromJsonObject((JsonObject) object);
            CoordinatesHit.add(coordinate);
        }
        // Return the HashSet of Coordinate objects
        return CoordinatesHit;
    }
    // Create an int array of size 2
    public static int[] fromJsonArrayToIntArray(JsonArray ja) {
        int[] Moves = new int[2];
        // Iterate through the JsonArray, convert each value to an int, and store it in the int array
        for (int i = 0; i < ja.size(); i++) {
            Moves[i] = ((BigDecimal) ja.get(i)).intValue();
        }
        // Return the int array
        return Moves;
    }

    public static AI fromJsonObject(JsonObject jo, Grid ownGrid, Grid opponentGrid) {
        // Extract various fields from the JsonObject
        String name = (String) jo.get("name");
        List<Ship> ships = Player.fromJsonArraytoShipList((JsonArray) jo.get("ships"));
        Laser laser = Laser.fromJsonObject((JsonObject) jo.get("laser"));
        Grenade grenade = Grenade.fromJsonObject((JsonObject) jo.get("grenade"));
        
        // Extract and convert additional fields from the JsonObject
        HashSet<Coordinate> CoordinatesHit = fromJsonArrayToHashSetOfCoordinates((JsonArray) jo.get("CoordinatesHit"));
        boolean hasShot = (boolean) jo.get("hasShot");
        boolean followTargetMode = (boolean) jo.get("followTargetMode");
        boolean Right = (boolean) jo.get("Right");
        boolean Left = (boolean) jo.get("Left");
        boolean Up = (boolean) jo.get("Up");
        boolean Down = (boolean) jo.get("Down");
        int switchDirection = ((BigDecimal) jo.get("switchDirection")).intValue();
        int[] Moves = fromJsonArrayToIntArray((JsonArray) jo.get("Moves"));
        
        
        
        Coordinate lastCoordinate = (Coordinate) jo.get("lastCoordinate");

        return new AI(name, ownGrid, opponentGrid, ships, laser, grenade, followTargetMode, Moves, hasShot, Right, Left,
                Up, Down, lastCoordinate, switchDirection, CoordinatesHit);
    }
}
