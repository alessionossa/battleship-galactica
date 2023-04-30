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
    protected String name;
    protected List<Ship> deathstars = new ArrayList<Ship>();
    protected List<Ship> cruisers = new ArrayList<Ship>();
    protected List<Ship> scouts = new ArrayList<Ship>();
    protected List<Ship> ships = new ArrayList<Ship>();
    protected Grid ownGrid;
    protected Grid opponentGrid;

    protected Cannon cannon = new Cannon();
    protected Grenade grenade = new Grenade();
    protected Laser laser = new Laser();

    protected static Scanner sc = new Scanner(System.in);

    public Player(Grid ownGrid, Grid opponentGrid) {
        this.ownGrid = ownGrid;
        this.opponentGrid = opponentGrid;

        initializeShips();
    }

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

    public abstract void shoot(Coordinate coordinate, Weapon weaponToShoot, boolean gravityMode, boolean gravityUsed);

    public List<Coordinate> shootGrenade(Coordinate coordinate, Grenade grenade) {
        List<Coordinate> coordinateList = grenade.getScatterCoordinates(coordinate, opponentGrid);
        grenade.decrementAmountOfUses();
        checkOutcomeOfShot(coordinateList);
        return coordinateList;
    }

    public void shootLaser(Coordinate coordinate, char rowOrColumn, Laser laser) {
        List<Coordinate> coordinateList = laser.getLaserCoordinates(coordinate, opponentGrid, rowOrColumn);
        laser.decrementAmountOfUses();
        checkOutcomeOfShot(coordinateList);
    }

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

    private void initializeShips() {
        deathstars.add(new DeathStar(IdGenerator.get()));
        cruisers.add(new Cruiser(IdGenerator.get()));
        for (int i = 0; i < 2; i++) {
            scouts.add(new Scout(IdGenerator.get()));
        }
        if (ownGrid.getGridSize() == 15) {
            cruisers.add(new Cruiser(IdGenerator.get()));
            scouts.add(new Scout(IdGenerator.get()));

        }
        if (ownGrid.getGridSize() == 20) {
            deathstars.add(new DeathStar(IdGenerator.get()));
            cruisers.add(new Cruiser(IdGenerator.get()));
        }
        ships.addAll(deathstars);
        ships.addAll(cruisers);
        ships.addAll(scouts);
    }

    public boolean areAllShipsSunk(List<Ship> ships) {
        for (Ship ship : ships) {
            if (!ship.isSunk()) {
                return false;
            }
        }
        return true;
    }

    public boolean hasAllShipsPlaced() {
        for (Ship ship : ships) {
            if (!ship.isPlaced()) {
                return false;
            }
        }
        return true;
    }

    public Ship placeShip(Ship ship, Coordinate coordinate, Direction direction) {
        if (ownGrid.isValidShipPosition(ship, coordinate, direction)) {
            ship.setCoordinate(coordinate);
            ship.setDirection(direction);
            ownGrid.placeShip(ship, coordinate, direction);
            return ship;
        }
        return null;
    }

    public void removeShip(Ship ship) throws UnplacedShipException {
        ownGrid.removeShip(ship);
        ship.setCoordinate(null);
        ship.setDirection(null);
    }

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

    public boolean checkOutcomeOfShot(List<Coordinate> coordinateList) {
        int asteroidsHit = 0;
        int shipsHit = 0;
        int shipsSunk = 0;
        int successfulHits = 0;
        Set<Planet> planetsHit = new HashSet<Planet>();

        if (this instanceof AI && AI.getFollowTragetMode()) {
            if (opponentGrid.getTile(coordinateList.get(0)).isHit())
                return false;
        }

        for (Coordinate coordinate : coordinateList) {

            Asteroid asteroidAtCoordinate = opponentGrid.getAsteroidAtCoordinate(coordinate);
            Ship shipAtCoordinate = opponentGrid.getShipAtCoordinate(coordinate);
            Planet planet = opponentGrid.getPlanetAtCoordinate(coordinate);

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
            } else if (planet != null) {
                planetsHit.add(planet);
            } else if (shipAtCoordinate != null) {
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
            } else {
                opponentGrid.setTile(coordinate, true);
                if (this instanceof AI) {
                    AI ai = (AI) this;
                    ai.updateCoordinateHit(coordinate);
                }
            }
        }

        for (Planet planet : planetsHit) {
            for (Coordinate coordinate : planet.getPlanetCoordinates()) {
                opponentGrid.setTile(coordinate, true);
                if (this instanceof AI) {
                    AI ai = (AI) this;
                    ai.updateCoordinateHit(coordinate);
                }
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

    public JsonArray toJsonArray(List<Ship> ships) {
        JsonArray ja = new JsonArray();
        for (Ship ship : ships)
            ja.add(ship.toJsonObject());

        return ja;
    }

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