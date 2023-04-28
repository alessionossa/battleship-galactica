package com.galactica.model;

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

    public int getGridSize() {
        return gridSize;
    }

    public List<Planet> getPlanets() {
        return planets;
    }

    public List<Asteroid> getAsteroids() {
        return asteroids;
    }

    public boolean isValidCoordinate(Coordinate coordinate) {
        return coordinate.getX() >= 'a' && coordinate.getX() < 'a' + gridSize
                && coordinate.getY() >= 0 && coordinate.getY() < gridSize;
    }

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

    private int convertXToMatrixIndex(char x) {
        return x - 'a';
    }

    void setTile(Coordinate coordinate, Ship ship) {
        int xIndex = convertXToMatrixIndex(coordinate.getX());

        tiles[coordinate.getY()][xIndex].setShip(ship);
    }

    void setTile(Coordinate coordinate, boolean hit) {
        int xIndex = convertXToMatrixIndex(coordinate.getX());

        tiles[coordinate.getY()][xIndex].setHit(hit);
    }

    void setTile(Coordinate coordinate, Asteroid asteroid) {
        int xIndex = convertXToMatrixIndex(coordinate.getX());
        tiles[coordinate.getY()][xIndex].setAsteroid(asteroid);
    }

    void setTile(Coordinate coordinate, Planet planet) {
        int xIndex = convertXToMatrixIndex(coordinate.getX());
        tiles[coordinate.getY()][xIndex].setPlanet(planet);
    }

    public Tile getTile(Coordinate coordinate) {
        int xIndex = convertXToMatrixIndex(coordinate.getX());

        return tiles[coordinate.getY()][xIndex];
    }

    public Ship getShipAtCoordinate(Coordinate coordinate) {
        return getTile(coordinate).getShip();
    }

    public Asteroid getAsteroidAtCoordinate(Coordinate coordinate) {
        return getTile(coordinate).getAsteroid();
    }

    public Planet getPlanetAtCoordinate(Coordinate coordinate) {
        return getTile(coordinate).getPlanet();
    }

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

    public void placeAsteroids() {
        Random random = new Random();
        int[] asteroidCoordinates = random.ints((int) (Grid.gridSize * Grid.gridSize * 0.1), 0, Grid.gridSize)
                .toArray();
        for (int i = 0; i < (int) (Grid.gridSize * Grid.gridSize * 0.1); i += 2) {
            Coordinate asteroidCoordinate = new Coordinate((char) ('a' + asteroidCoordinates[i]),
                    asteroidCoordinates[i + 1]);
            Asteroid asteroid = new Asteroid(asteroidCoordinate);
            asteroids.add(asteroid);
            setTile(asteroidCoordinate, asteroid);
        }
    }

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

    public void placePlanets(List<Planet> planets) {
        for (Planet planet : planets) {
            this.planets.add(planet);
            for (Coordinate coordinate : planet.getPlanetCoordinates()) {
                setTile(coordinate, planet);
            }
        }
    }




    public JsonArray toJsonArrayformMatrix(Tile[][] tiles) {
        JsonArray ja = new JsonArray();
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                ja.add(tiles[i][j].toJsonObject());
            }
        }
        return ja;
    }

    public JsonArray toJsonArray(List <?> list) {
        JsonArray ja = new JsonArray();
        for (Object object : list) {
            if (object instanceof Planet)
                ja.add(((Planet) object).toJsonObject());
            else if (object instanceof Asteroid)
                ja.add(((Asteroid) object).toJsonObject());
        }
        return ja;
    }

    public JsonObject toJsonObject() {
        JsonObject jo = new JsonObject();
        jo.put("tiles", toJsonArrayformMatrix(tiles));
        jo.put("asteroids", toJsonArray(asteroids));
        jo.put("planets", toJsonArray(planets));
        jo.put("gridSize", gridSize);
        
        return jo;
    }


}
