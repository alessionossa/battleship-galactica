package com.galactica.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Grid {
    private Tile[][] tiles;
    private List<Planet> planets = new ArrayList<Planet>();

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

    public boolean isValidCoordinate(Coordinate coordinate) {
        return coordinate.getX() >= 'a' && coordinate.getX() < 'a' + gridSize
                && coordinate.getY() >= 0 && coordinate.getY() < gridSize;
    }

    /**
     * This method checks if the position is a valid position
     */
    public boolean isValidShipPosition(Ship ship, Coordinate coordinate, Direction direction) {

        if (direction == Direction.Horizontal) {
            if ((convertXToMatrixIndex(coordinate.getX()) + ship.getLength()) > gridSize) {
                return false;
            }

            for (int i = 0; i < ship.getLength(); i++) {
                char newX = (char) (coordinate.getX() + i);
                Coordinate tileCoordinate = new Coordinate(newX, coordinate.getY());
                if (getTile(tileCoordinate).getShip() != null || getTile(tileCoordinate).getAsteroid() != null) {
                    return false;
                }
            }
        } else {
            if ((coordinate.getY() + ship.getLength()) > gridSize) {
                return false;
            }

            for (int i = 0; i < ship.getLength(); i++) {
                int newY = coordinate.getY() + i;
                Coordinate tileCoordinate = new Coordinate(coordinate.getX(), newY);
                if (getTile(tileCoordinate).getShip() != null || getTile(tileCoordinate).getAsteroid() != null) {
                    return false;
                }
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
            setTile(new Coordinate((char) ('a' + asteroidCoordinates[i]), asteroidCoordinates[i + 1]), new Asteroid());
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
            Coordinate planetCoordinate = planet.getCoordinate();

            for (int i = 0; i < planet.getSize(); i++) {
                for (int j = 0; j < planet.getSize(); j++) {
                    setTile(new Coordinate((char) (planetCoordinate.getX() + i), planetCoordinate.getY() + j),
                            planet);
                }
            }
        }
    }

}
