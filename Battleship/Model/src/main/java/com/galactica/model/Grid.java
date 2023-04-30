package com.galactica.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;

public class Grid {
    private Tile[][] tiles;
    private List<Planet> planets = new ArrayList<Planet>();
    private List<Asteroid> asteroids = new ArrayList<Asteroid>();

    private static int gridSize;

    public Grid(int gridSize) {
        Grid.gridSize = gridSize;
        tiles = new Tile[Grid.gridSize][Grid.gridSize];
        for (int i = 0; i < Grid.gridSize; i++) {
            for (int j = 0; j < Grid.gridSize; j++) {
                tiles[i][j] = new Tile();
            }
        }
    }

    public Grid(int gridSize, List<Asteroid> asteroids, List<Planet> planets, Tile[][] tiles) {
        Grid.gridSize = gridSize;
        this.asteroids = asteroids;
        this.planets = planets;
        this.tiles = tiles;
    }

    public int getGridSize() {
        return gridSize;
    }

    public List<Planet> getPlanets() {
        return planets;
    }

    public List<Asteroid> getAsteroids() {
        return asteroids;
    }
    // Method to check if a given coordinate is valid
    public boolean isValidCoordinate(Coordinate coordinate) {
        return coordinate.getX() >= 'a' && coordinate.getX() < 'a' + gridSize
                && coordinate.getY() >= 0 && coordinate.getY() < gridSize;
    }
    // Method to check if a given coordinate is valid and not on a planet
    public boolean isValidCoordinateAndNotOnPlanet(Coordinate coordinate) {
        boolean foundAPlanet = false;
        List<Coordinate> PlanetsCoordinates = new ArrayList<Coordinate>();
        Coordinate AsteroidCoordinate;

        if (getTile(coordinate).getPlanet() != null) {
            PlanetsCoordinates = getTile(coordinate).getPlanet().getPlanetCoordinates();

        } else if (getTile(coordinate).getAsteroid() != null) {
            AsteroidCoordinate = getTile(coordinate).getAsteroid().getCoordinate();
            
            if (coordinate.equals(AsteroidCoordinate))
                return false; 
        }

        for (Coordinate planetCoordinate : PlanetsCoordinates) {
            if (planetCoordinate.equals(coordinate)) {
                foundAPlanet = true;
            }
        }

        return isValidCoordinate(coordinate) && !foundAPlanet;
    }
    // Method to check if a ship can be placed at a given position
    public boolean isValidShipPosition(Ship ship, Coordinate coordinate, Direction direction) {
        char x = coordinate.getX();
        int y = coordinate.getY();
        if (direction == Direction.Horizontal) {
            if ((convertXToMatrixIndex(coordinate.getX()) + ship.getLength()) > gridSize) {
                return false;
            }
        } else {
            if ((coordinate.getY() + ship.getLength()) > gridSize) {
                return false;
            }
        }

        for (int i = 0; i < ship.getLength(); i++) {
            if (direction == Direction.Horizontal) {
                x = (char) (coordinate.getX() + i);
            } else {
                y = coordinate.getY() + i;
            }
            Coordinate tileCoordinate = new Coordinate(x, y);
            if (getTile(tileCoordinate).getShip() != null || getTile(tileCoordinate).getAsteroid() != null
                    || getTile(tileCoordinate).getPlanet() != null) {
                return false;
            }
        }

        return true;
    }
    
    // Method to place a ship on the grid
    public void placeShip(Ship ship, Coordinate originCoordinate, Direction direction) {
        Coordinate tileCoordinate;

        if (direction == Direction.Horizontal) {
            for (int i = 0; i < ship.getLength(); i++) {
                char newX = (char) (originCoordinate.getX() + i);
                tileCoordinate = new Coordinate(newX, originCoordinate.getY());
                setTile(tileCoordinate, ship);

            }
        } else {
            for (int i = 0; i < ship.getLength(); i++) {
                int newY = originCoordinate.getY() + i;
                tileCoordinate = new Coordinate(originCoordinate.getX(), newY);
                setTile(tileCoordinate, ship);

            }
        }

    }
    // Method to place a ship on the grid
    void removeShip(Ship ship) throws UnplacedShipException {
        if (ship == null)
            throw new UnplacedShipException("Ship has not been placed in the grid before");
        Coordinate startCoordinate = ship.getCoordinate();
        switch (ship.getDirection()) {
            case Vertical:
                for (int i = startCoordinate.getY(); i < startCoordinate.getY() + ship.getLength(); i++) {
                    Tile currentTile = tiles[i][convertXToMatrixIndex(startCoordinate.getX())];

                    currentTile.setShip(null);
                }
                break;
            case Horizontal:
                for (int i = convertXToMatrixIndex(startCoordinate.getX()); i < convertXToMatrixIndex(
                        startCoordinate.getX()) + ship.getLength(); i++) {
                    Tile currentTile = tiles[startCoordinate.getY()][i];
                    currentTile.setShip(null);
                }
                break;
        }

    }
    // Method to place a ship on the grid
    public int convertXToMatrixIndex(char x) {
        return x - 'a';
    }
    // Method to set the ship on a tile
    void setTile(Coordinate coordinate, Ship ship) {
        int xIndex = convertXToMatrixIndex(coordinate.getX());

        tiles[coordinate.getY()][xIndex].setShip(ship);
    }
    // Method to set a hit on a tile
    void setTile(Coordinate coordinate, boolean hit) {
        int xIndex = convertXToMatrixIndex(coordinate.getX());

        tiles[coordinate.getY()][xIndex].setHit(hit);
    }
    // Method to set an asteroid on a tile
    void setTile(Coordinate coordinate, Asteroid asteroid) {
        int xIndex = convertXToMatrixIndex(coordinate.getX());
        tiles[coordinate.getY()][xIndex].setAsteroid(asteroid);
    }
    // Method to set a planet on a tile
    void setTile(Coordinate coordinate, Planet planet) {
        int xIndex = convertXToMatrixIndex(coordinate.getX());
        tiles[coordinate.getY()][xIndex].setPlanet(planet);
    }
    // Method to get the tile at a given coordinate
    public Tile getTile(Coordinate coordinate) {
        int xIndex = convertXToMatrixIndex(coordinate.getX());

        return tiles[coordinate.getY()][xIndex];
    }
    // Method to get the ship at a given coordinate
    public Ship getShipAtCoordinate(Coordinate coordinate) {
        return getTile(coordinate).getShip();
    }
    // Method to get the asteroid at a given coordinate
    public Asteroid getAsteroidAtCoordinate(Coordinate coordinate) {
        return getTile(coordinate).getAsteroid();
    }
    // Method to get the planet at a given coordinate
    public Planet getPlanetAtCoordinate(Coordinate coordinate) {
        return getTile(coordinate).getPlanet();
    }
    // Method to check if a ship is sunk
    public boolean checkIfShipIsSunk(Ship ship) {
        Coordinate startCoordinate = ship.getCoordinate();

        switch (ship.getDirection()) {
            case Vertical:
                for (int i = startCoordinate.getY(); i < startCoordinate.getY() + ship.getLength(); i++) {
                    Tile currentTile = tiles[i][convertXToMatrixIndex(startCoordinate.getX())];
                    if (!currentTile.isHit())
                        return false;
                }

                break;
            case Horizontal:
                for (int i = convertXToMatrixIndex(startCoordinate.getX()); i < convertXToMatrixIndex(
                        startCoordinate.getX()) + ship.getLength(); i++) {
                    Tile currentTile = tiles[startCoordinate.getY()][i];
                    if (!currentTile.isHit())
                        return false;
                }

                break;
        }

        return true;
    }
    // Method to place asteroids on the grid
    public void placeAsteroids() {
        Coordinate coordinate;

        for (int i = 0; i < (int) (gridSize * gridSize * 0.05); i++) {
            coordinate = getNewValidCoordinate();
            Asteroid asteroid = new Asteroid(coordinate);
            asteroids.add(asteroid);
            setTile(coordinate, asteroid);
        }
    }
    // Method to get a new valid coordinate for placing asteroids
    private Coordinate getNewValidCoordinate() {
        Random random = new Random();
        Coordinate coordinate;

        do {
            char x0 = (char) (random.nextInt(gridSize) + 'a');
            int y0 = random.nextInt(gridSize);
            coordinate = new Coordinate(x0, y0);
        } while (!isValidCoordinateAndNotOnPlanet(coordinate));

        return coordinate;
    }

    // Method to check if any ships are placed on the grid
    public boolean anyShipsPlaced() {
        for (Tile[] row : tiles) {
            for (Tile tile : row) {
                if (tile.getShip() != null)
                    return true;
            }
        }
        return false;
    }

    public Tile[][] getTiles() {
        return tiles;
    }
    // Method to place planets on the grid
    public void placePlanets(List<Planet> planets) {
        for (Planet planet : planets) {
            this.planets.add(planet);
            for (Coordinate coordinate : planet.getPlanetCoordinates()) {
                setTile(coordinate, planet);
            }
        }
    }
    // Method to convert a tile matrix to a JsonArray
    public JsonArray toJsonArrayformMatrix(Tile[][] tiles) {
        JsonArray ja = new JsonArray();
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                ja.add(tiles[i][j].toJsonObject());
            }
        }
        return ja;
    }
    // Method to convert a List of Planets or Asteroids to a JsonArray
    public JsonArray toJsonArray(List<?> list) {
        JsonArray ja = new JsonArray();
        for (Object object : list) {
            if (object instanceof Planet)
                ja.add(((Planet) object).toJsonObject());
            else if (object instanceof Asteroid)
                ja.add(((Asteroid) object).toJsonObject());
        }
        return ja;
    }
    // Method to convert the Grid object to a JsonObject
    public JsonObject toJsonObject() {
        JsonObject jo = new JsonObject();
        jo.put("tiles", toJsonArrayformMatrix(tiles));
        jo.put("asteroids", toJsonArray(asteroids));
        jo.put("planets", toJsonArray(planets));
        jo.put("gridSize", gridSize);

        return jo;
    }
    // Method to convert a JsonArray to a List of Planets
    public static List<Planet> fromJsonArrayToPlanetList(JsonArray ja) {
        List<Planet> list = new ArrayList<>();
        for (Object object : ja) {
            Planet planet = Planet.fromJsonObject((JsonObject) object);
            list.add(planet);
        }
        return list;
    }
    // Method to convert a JsonArray to a List of Asteroids
    public static List<Asteroid> fromJsonArrayToAsteroidList(JsonArray ja) {
        List<Asteroid> list = new ArrayList<>();
        for (Object object : ja) {
            Asteroid asteroid = Asteroid.fromJsonObject((JsonObject) object);
            list.add(asteroid);
        }

        return list;
    }
    
    // Method to convert a JsonArray to a Tile matrix
    public static Tile[][] fromJsonArrayToMatrix(JsonArray ja, int gridSize) {
        Tile[][] tiles = new Tile[gridSize][gridSize];
        int index = 0;
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                tiles[i][j] = Tile.fromJsonObject((JsonObject) ja.get(index));
                index++;
            }
        }
        return tiles;
    }
    // Method to create a Grid object from a JsonObject
    public static Grid fromJsonObject(JsonObject jsonObject) {
        int GridSize = ((BigDecimal) jsonObject.get("gridSize")).intValue();
        List<Asteroid> asteroids = fromJsonArrayToAsteroidList((JsonArray) jsonObject.get("asteroids"));
        List<Planet> planets = fromJsonArrayToPlanetList((JsonArray) jsonObject.get("planets"));
        Tile[][] tiles = fromJsonArrayToMatrix((JsonArray) jsonObject.get("tiles"), GridSize);

        return new Grid(GridSize, asteroids, planets, tiles);
    }
}
