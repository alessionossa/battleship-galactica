package com.galactica.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import com.galactica.cli.AICLI;
import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;

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
    private int switchDirection;

    public AI(String name, Grid ownGrid, Grid opponentGrid) {
        super(ownGrid, opponentGrid);
        this.name = name;
    }

    public AI(String name, Grid ownGrid, Grid opponentGrid, List<Ship> ships, Laser laser, Grenade grenade,
            boolean followTargetMode, int[] Moves, boolean hasShot, boolean Right, boolean Left, boolean Up,
            boolean Down, Coordinate lastCoordinate, int switchDirection, HashSet<Coordinate> CoordinatesHit) {
        super(ownGrid, opponentGrid);
        this.name = name;
        this.ships = ships;
        this.laser = laser;
        this.grenade = grenade;
        this.cannon = new Cannon();
        this.followTargetMode = followTargetMode;
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
            boolean hitSomething = shootCannonAS(newCoordinate, gravityMode, gravityUsed);
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

    public boolean shootCannonAS(Coordinate coordinate, boolean gravityMode, boolean gravityUsed) {
        boolean hit = checkOutcomeOfShotAS(coordinate);
        return hit;
    }

    public boolean checkOutcomeOfShotAS(Coordinate coordinate) {
        int asteroidsHit = 0;
        int shipsHit = 0;
        int shipsSunk = 0;
        int successfulHits = 0;
        List<Planet> planetsHit = new ArrayList<Planet>();
        List<Coordinate> coordinatesWithPlanets = new ArrayList<Coordinate>();

        if (opponentGrid.getTile(coordinate).isHit())
            return false;

        opponentGrid.setTile(coordinate, true);
        updateCoordinateHit(coordinate);

        Asteroid asteroidAtCoordinate = opponentGrid.getAsteroidAtCoordinate(coordinate);
        Ship shipAtCoordinate = opponentGrid.getShipAtCoordinate(coordinate);
        Planet planet = opponentGrid.getPlanetAtCoordinate(coordinate);

        if (asteroidAtCoordinate != null) {
            asteroidsHit++;
        } else if (planet != null) {
            planetsHit.add(planet);
        } else if (shipAtCoordinate != null) {
            shipsHit++;
            boolean isShipSunk = opponentGrid.checkIfShipIsSunk(shipAtCoordinate);
            if (isShipSunk) {
                shipAtCoordinate.setSunk(true);
                shipsSunk++;
                resetTracking();
            }
        }

        for (Planet Singleplanet : planetsHit) {
            for (Coordinate c : Singleplanet.getPlanetCoordinates()) {
                opponentGrid.setTile(c, true);
                coordinatesWithPlanets.add(c);
            }
        }

        successfulHits = asteroidsHit + shipsHit;

        if (successfulHits + planetsHit.size() == 0) {
            System.out.println("Unlucky :( \n" + name + " didn't hit anything");

        } else {
            if (successfulHits == 1) {
                System.out.println(name + " hit something!");
            } else if (successfulHits > 1)
                System.out.println(name + " made " + successfulHits + " successful shots!");
            if (shipsSunk == 1) {
                System.out.println(name + " sunk a ship! 💥🚢");
            } else if (shipsSunk > 1) {
                System.out.println(name + " sunk " + shipsSunk + " ships! 💥🚢");
            }
        }
        if (planetsHit.size() == 1) {
            System.out.println(name + " hit a planet! 🌎");
        } else if (planetsHit.size() > 1) {
            System.out.println(name + " hit " + planetsHit.size() + " planets! 🌎");
        }

        return successfulHits > 0 && planetsHit.size() == 0;
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

    public boolean getFollowTragetMode() {
        return followTargetMode;
    }

    public JsonArray toJsonArray(HashSet<Coordinate> CoordinatesHit) {
        JsonArray ja = new JsonArray();
        for (Coordinate coordinate : CoordinatesHit) {
            ja.add(coordinate.toJsonObject());
        }
        return ja;
    }

    public JsonArray toJsonArray(int[] Moves) {
        JsonArray ja = new JsonArray();
        for (int i : Moves) {
            ja.add(i);
        }
        return ja;
    }

    public JsonObject toJsonObject() {
        JsonObject jo = super.toJsonObject();

        jo.put("CoordinatesHit", toJsonArray(CoordinatesHit));
        jo.put("hasShot", hasShot);
        jo.put("followTargetMode", followTargetMode);
        jo.put("Right", Right);
        jo.put("Left", Left);
        jo.put("Up", Up);
        jo.put("Down", Down);
        jo.put("switchDirection", switchDirection);
        jo.put("Moves", toJsonArray(Moves));

        if (lastCoordinate != null)
            jo.put("lastCoordinate", lastCoordinate.toJsonObject());
        else
            jo.put("lastCoordinate", null);

        return jo;
    }

    public static HashSet<Coordinate> fromJsonArrayToHashSetOfCoordinates(JsonArray ja) {
        HashSet<Coordinate> CoordinatesHit = new HashSet<Coordinate>();
        for (Object object : ja) {
            Coordinate coordinate = Coordinate.fromJsonObject((JsonObject) object);
            CoordinatesHit.add(coordinate);
        }
        return CoordinatesHit;
    }

    public static int[] fromJsonArrayToIntArray(JsonArray ja) {
        int[] Moves = new int[2];
        for (int i = 0; i < ja.size(); i++) {
            Moves[i] = ((BigDecimal) ja.get(i)).intValue();
        }

        return Moves;
    }

    public static AI fromJsonObject(JsonObject jo, Grid ownGrid, Grid opponentGrid) {
        String name = (String) jo.get("name");
        List<Ship> ships = Player.fromJsonArraytoShipList((JsonArray) jo.get("ships"));
        Laser laser = Laser.fromJsonObject((JsonObject) jo.get("laser"));
        Grenade grenade = Grenade.fromJsonObject((JsonObject) jo.get("grenade"));

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
