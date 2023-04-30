package com.galactica.model;

import com.galactica.model.ships.Cruiser;
import com.galactica.model.ships.DeathStar;
import com.galactica.model.ships.Scout;
import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public abstract class Player {
    // Class members
    protected String name;
    protected List<Ship> deathstars = new ArrayList<Ship>();
    protected List<Ship> cruisers = new ArrayList<Ship>();
    protected List<Ship> scouts = new ArrayList<Ship>();
    protected List<Ship> ships = new ArrayList<Ship>();
    protected Grid ownGrid;
    protected Grid opponentGrid;

    // Weapons
    protected Cannon cannon = new Cannon();
    protected Grenade grenade = new Grenade();
    protected Laser laser = new Laser();

    // Scanner for user input
    protected static Scanner sc = new Scanner(System.in);

    // Constructor
    public Player(Grid ownGrid, Grid opponentGrid) {
        this.ownGrid = ownGrid;
        this.opponentGrid = opponentGrid;

        initializeShips();
    }

    // Overloaded constructor
    public Player(String name, Grid ownGrid, Grid opponentGrid, List<Ship> ships, Laser laser, Grenade grenade,
            Cannon cannon) {
        this.name = name;
        this.ownGrid = ownGrid;
        this.opponentGrid = opponentGrid;
        this.ships = ships;
        this.laser = laser;
        this.grenade = grenade;
        this.cannon = cannon;
    }

    // Abstract method for shooting
    public abstract void shoot(Coordinate coordinate, Weapon weaponToShoot, boolean gravityMode, boolean gravityUsed);

    // Shoot grenade at the specified coordinate
    public List<Coordinate> shootGrenade(Coordinate coordinate, Grenade grenade) {
        List<Coordinate> coordinateList = grenade.getScatterCoordinates(coordinate, opponentGrid);
        grenade.decrementAmountOfUses();
        checkOutcomeOfShot(coordinateList);
        return coordinateList;
    }

    // Shoot laser at the specified coordinate and row or column
    public void shootLaser(Coordinate coordinate, char rowOrColumn, Laser laser) {
        List<Coordinate> coordinateList = laser.getLaserCoordinates(coordinate, opponentGrid, rowOrColumn);
        laser.decrementAmountOfUses();
        checkOutcomeOfShot(coordinateList);
    }

    // Shoot cannon at the specified coordinate with gravity mode settings
    public boolean shootCannon(Coordinate coordinate, boolean gravityMode, boolean gravityUsed) {
        List<Coordinate> coordinateList = new ArrayList<Coordinate>();
        coordinateList.add(coordinate);

        boolean hit = checkOutcomeOfShot(coordinateList);

        if (!hit && gravityMode && !gravityUsed) {
            List<Planet> opponentPlanets = opponentGrid.getPlanets();
            for (Planet planet : opponentPlanets) {
                if (!gravityUsed) {
                    Coordinate rebound = planet.getPlanetRebound(coordinate);
                    if (rebound != null) {
                        shootCannon(rebound, true, true);
                    }
                }
            }
        }
        return hit;
    }

    // Initialize ships based on grid size
    private void initializeShips() {
        deathstars.add(new DeathStar(IdGenerator.get()));
        for (int i = 0; i < 2; i++) {
            cruisers.add(new Cruiser(IdGenerator.get()));
        }
        for (int i = 0; i < 3; i++) {
            scouts.add(new Scout(IdGenerator.get()));
        }
        if (ownGrid.getGridSize() == 15) {
            deathstars.add(new DeathStar(IdGenerator.get()));
            for (int i = 0; i < 2; i++) {
                cruisers.add(new Cruiser(IdGenerator.get()));
            }
            for (int i = 0; i < 2; i++) {
                scouts.add(new Scout(IdGenerator.get()));
            }
        }
        if (ownGrid.getGridSize() == 20) {
            for (int i = 0; i < 2; i++) {
                deathstars.add(new DeathStar(IdGenerator.get()));
            }
            for (int i = 0; i < 2; i++) {
                cruisers.add(new Cruiser(IdGenerator.get()));
            }
            for (int i = 0; i < 2; i++) {
                scouts.add(new Scout(IdGenerator.get()));
            }
        }
        ships.addAll(deathstars);
        ships.addAll(cruisers);
        ships.addAll(scouts);
    }

    // Check if all ships are sunk
    public boolean areAllShipsSunk(List<Ship> ships) {
        for (Ship ship : ships) {
            if (!ship.isSunk()) {
                return false;
            }
        }
        return true;
    }

    // Check if all ships are placed
    public boolean hasAllShipsPlaced() {
        for (Ship ship : ships) {
            if (!ship.isPlaced()) {
                return false;
            }
        }
        return true;
    }

    // Place a ship on the grid if the position is valid
    public Ship placeShip(Ship ship, Coordinate coordinate, Direction direction) {
        if (ownGrid.isValidShipPosition(ship, coordinate, direction)) {
            ship.setCoordinate(coordinate);
            ship.setDirection(direction);
            ownGrid.placeShip(ship, coordinate, direction);
            return ship;
        }
        return null;
    }

    // Remove a ship from the grid
    public void removeShip(Ship ship) throws UnplacedShipException {
        ownGrid.removeShip(ship);
        ship.setCoordinate(null);
        ship.setDirection(null);
    }

    // Getter methods
    public String getName() {
        return name;
    }

    public Grid getOwnGrid() {
        return ownGrid;
    }

    public Grid getOpponentGrid() {
        return opponentGrid;
    }

    public List<Ship> getShips() {
        return ships;
    }

    public List<Ship> getDeathstars() {
        return deathstars;
    }

    public List<Ship> getCruisers() {
        return cruisers;
    }

    public List<Ship> getScouts() {
        return scouts;
    }

    public Cannon getCannon() {
        return cannon;
    }

    public Grenade getGrenade() {
        return grenade;
    }

    public Laser getLaser() {
        return laser;
    }

    // Check the outcome of a shot and display results
    public boolean checkOutcomeOfShot(List<Coordinate> coordinateList) {
        int asteroidsHit = 0;
        int shipsHit = 0;
        int shipsSunk = 0;
        int successfulHits = 0;
        Set<Planet> planetsHit = new HashSet<Planet>();

        // If this player is an AI and in follow target mode, check if the first
        // coordinate in the list is already hit
        if (this instanceof AI && AI.getFollowTragetMode()) {
            if (opponentGrid.getTile(coordinateList.get(0)).isHit())
                return false;
        }

        // Loop through each coordinate in the coordinate list
        for (Coordinate coordinate : coordinateList) {

            // Check if there's an asteroid, ship, or planet at the current coordinate
            Asteroid asteroidAtCoordinate = opponentGrid.getAsteroidAtCoordinate(coordinate);
            Ship shipAtCoordinate = opponentGrid.getShipAtCoordinate(coordinate);
            Planet planet = opponentGrid.getPlanetAtCoordinate(coordinate);

            // If there's an asteroid, increment asteroidsHit and update grid and AI
            // tracking if applicable
            if (asteroidAtCoordinate != null) {
                asteroidsHit++;
                opponentGrid.setTile(coordinate, true);
                if (this instanceof AI && !AI.getFollowTragetMode()) {
                    AI ai = (AI) this;
                    ai.updateCoordinateHit(coordinate);
                    ai.updateTracking(coordinate);
                } else if (this instanceof AI && AI.getFollowTragetMode()) {
                    AI ai = (AI) this;
                    ai.updateCoordinateHit(coordinate);
                }
            }
            // If there's a planet, add the planet to the set of hit planets
            else if (planet != null) {
                planetsHit.add(planet);
            }
            // If there's a ship, increment shipsHit and update grid and AI tracking if
            // applicable, and check if the ship is sunk
            else if (shipAtCoordinate != null) {
                opponentGrid.setTile(coordinate, true);
                shipsHit++;
                if (this instanceof AI && !AI.getFollowTragetMode()) {
                    AI ai = (AI) this;
                    ai.updateCoordinateHit(coordinate);
                    ai.updateTracking(coordinate);
                } else if (this instanceof AI && AI.getFollowTragetMode()) {
                    AI ai = (AI) this;
                    ai.updateCoordinateHit(coordinate);
                }
                boolean isShipSunk = opponentGrid.checkIfShipIsSunk(shipAtCoordinate);
                if (isShipSunk) {
                    shipAtCoordinate.setSunk();
                    shipsSunk++;
                    if (this instanceof AI && AI.getFollowTragetMode()) {
                        AI ai = (AI) this;
                        ai.resetTracking();
                    }
                }
            }
            // If there's nothing at the coordinate, update the grid and AI tracking if
            // applicable
            else {
                opponentGrid.setTile(coordinate, true);
                if (this instanceof AI) {
                    AI ai = (AI) this;
                    ai.updateCoordinateHit(coordinate);
                }
            }
        }

        // Loop through the hit planets and update the grid and AI tracking if
        // applicable
        for (Planet planet : planetsHit) {
            for (Coordinate coordinate : planet.getPlanetCoordinates()) {
                opponentGrid.setTile(coordinate, true);
                if (this instanceof AI) {
                    AI ai = (AI) this;
                    ai.updateCoordinateHit(coordinate);
                }
            }
        }

        // Calculate the total successful hits
        successfulHits = asteroidsHit + shipsHit;

        // Print the results
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
        // Return boolean
        return successfulHits > 0 && planetsHit.size() == 0;
    }

    // Convert a list of ships to a JsonArray
    public JsonArray toJsonArray(List<Ship> ships) {
        JsonArray ja = new JsonArray();
        for (Ship ship : ships)
            ja.add(ship.toJsonObject());

        return ja;
    }

    // Convert player object to JsonObject
    public JsonObject toJsonObject() {
        JsonObject jo = new JsonObject();
        jo.put("name", name);
        jo.put("ships", toJsonArray(ships));
        jo.put("cannon", cannon.toJsonObject());
        jo.put("grenade", grenade.toJsonObject());
        jo.put("laser", laser.toJsonObject());

        return jo;
    }

    public static List<Ship> fromJsonArraytoShipList(JsonArray ja) {
        List<Ship> ships = new ArrayList<Ship>();
        for (int i = 0; i < ja.size(); i++) {
            ships.add(Ship.fromJsonObject((JsonObject) ja.get(i)));
        }
        return ships;
    }
}
