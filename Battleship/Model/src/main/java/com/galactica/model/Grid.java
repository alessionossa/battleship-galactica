package com.galactica.model;

import java.util.Random;

public class Grid {
    private Tile[][] tiles = new Tile[gridSize][gridSize];

    private static int gridSize = 10;

    public Grid() {
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                tiles[i][j] = new Tile();
            }
        }
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

    public void placeShip(Ship ship, Coordinate originCoordinate, Direction direction) throws OutOfBoundsException{

        if (direction == Direction.Horizontal) {
            for (int i = 0; i < ship.getLength(); i++) {
                char newX = (char) (originCoordinate.getX() + i);
                Coordinate tileCoordinate = new Coordinate(newX, originCoordinate.getY());
                if (!isValidCoordinate(tileCoordinate)){
                    throw new OutOfBoundsException("Ship out of bounds");
                }
                else {
                    setTile(tileCoordinate, ship);
                }
            }
        } else {
            for (int i = 0; i < ship.getLength(); i++) {
                int newY = originCoordinate.getY() + i;
                Coordinate tileCoordinate = new Coordinate(originCoordinate.getX(), newY);
                if (!isValidCoordinate(tileCoordinate)){
                    throw new OutOfBoundsException("Ship out of bounds");
                }
                else {
                    setTile(tileCoordinate, ship);
                }
            }
        }

    }

    void removeShip(Ship ship) {
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

    Tile getTile(Coordinate coordinate) {
        int xIndex = convertXToMatrixIndex(coordinate.getX());

        return tiles[coordinate.getY()][xIndex];
    }

    public Ship getShipAtCoordinate(Coordinate coordinate) {
        return getTile(coordinate).getShip();
    }

    Asteroid getAsteroidAtCoordinate(Coordinate coordinate) {
        return getTile(coordinate).getAsteroid();
    }

    boolean checkIfShipIsSunk(Ship ship) {
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
        int[] asteroidCoordinates = random.ints(10, 0, 10).toArray();
        for (int i = 0; i < 10; i += 2) {
            setTile(new Coordinate((char) (97 + asteroidCoordinates[i]), asteroidCoordinates[i + 1]), new Asteroid());
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
}