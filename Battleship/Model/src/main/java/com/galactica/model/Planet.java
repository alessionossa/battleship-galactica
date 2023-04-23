package com.galactica.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Planet {
    private Coordinate planetCoordinate;
    private List<CoordinateDepthPair<Coordinate, Integer>> abovePlanet = new ArrayList<CoordinateDepthPair<Coordinate, Integer>>();;
    private List<CoordinateDepthPair<Coordinate, Integer>> belowPlanet = new ArrayList<CoordinateDepthPair<Coordinate, Integer>>();;
    private List<CoordinateDepthPair<Coordinate, Integer>> rightOfPlanet = new ArrayList<CoordinateDepthPair<Coordinate, Integer>>();;
    private List<CoordinateDepthPair<Coordinate, Integer>> leftOfPlanet = new ArrayList<CoordinateDepthPair<Coordinate, Integer>>();;
    private List<CoordinateDepthPair<Coordinate, Integer>> rightAbovePlanet = new ArrayList<CoordinateDepthPair<Coordinate, Integer>>();;
    private List<CoordinateDepthPair<Coordinate, Integer>> leftAbovePlanet = new ArrayList<CoordinateDepthPair<Coordinate, Integer>>();;
    private List<CoordinateDepthPair<Coordinate, Integer>> rightBelowPlanet = new ArrayList<CoordinateDepthPair<Coordinate, Integer>>();;
    private List<CoordinateDepthPair<Coordinate, Integer>> leftBelowPlanet = new ArrayList<CoordinateDepthPair<Coordinate, Integer>>();;
    private int size;
    private static int maxPlanetLength;

    public static final void setMaxPlanetLength(int maxPlanetLength) {
        Planet.maxPlanetLength = maxPlanetLength;
    }

    public Coordinate getCoordinate() {
        return planetCoordinate;
    }

    public List<CoordinateDepthPair<Coordinate, Integer>> getAbovePlanet() {
        return abovePlanet;
    }

    public List<CoordinateDepthPair<Coordinate, Integer>> getBelowPlanet() {
        return belowPlanet;
    }

    public List<CoordinateDepthPair<Coordinate, Integer>> getRightOfPlanet() {
        return rightOfPlanet;
    }

    public List<CoordinateDepthPair<Coordinate, Integer>> getLeftOfPlanet() {
        return leftOfPlanet;
    }

    public List<CoordinateDepthPair<Coordinate, Integer>> getRightAbovePlanet() {
        return rightAbovePlanet;
    }

    public List<CoordinateDepthPair<Coordinate, Integer>> getLeftAbovePlanet() {
        return leftAbovePlanet;
    }

    public List<CoordinateDepthPair<Coordinate, Integer>> getRightBelowPlanet() {
        return rightBelowPlanet;
    }

    public List<CoordinateDepthPair<Coordinate, Integer>> getLeftBelowPlanet() {
        return leftBelowPlanet;
    }

    public int getSize() {
        return size;
    }

    public Planet(int size, int gridSize) {
        this.size = size;

        Random random = new Random();
        int[] planetCoordinates = random.ints(2, 1, gridSize - maxPlanetLength).toArray();
        planetCoordinate = new Coordinate((char) ('a' + planetCoordinates[0]), planetCoordinates[1]);
        Coordinate borderCoordinate = planetCoordinate;

        for (int depth = 0; depth < (size - 1); depth++) {
            try {
                borderCoordinate = borderCoordinate.up(1);
                for (int i = 0; i < size + depth * 2; i++) {
                    abovePlanet.add(new CoordinateDepthPair<Coordinate, Integer>(borderCoordinate, depth));
                    borderCoordinate = borderCoordinate.right(1);
                }
                rightAbovePlanet.add(new CoordinateDepthPair<Coordinate, Integer>(borderCoordinate, depth));
                borderCoordinate = borderCoordinate.down(1);
                for (int i = 0; i < size + depth * 2; i++) {
                    rightOfPlanet.add(new CoordinateDepthPair<Coordinate, Integer>(borderCoordinate, depth));
                    borderCoordinate = borderCoordinate.down(1);

                }
                rightBelowPlanet.add(new CoordinateDepthPair<Coordinate, Integer>(borderCoordinate, depth));
                borderCoordinate = borderCoordinate.left(1);
                for (int i = 0; i < size + depth * 2; i++) {
                    belowPlanet.add(new CoordinateDepthPair<Coordinate, Integer>(borderCoordinate, depth));
                    borderCoordinate = borderCoordinate.left(1);
                }
                leftBelowPlanet.add(new CoordinateDepthPair<Coordinate, Integer>(borderCoordinate, depth));
                borderCoordinate = borderCoordinate.up(1);
                for (int i = 0; i < size + depth * 2; i++) {
                    leftOfPlanet.add(new CoordinateDepthPair<Coordinate, Integer>(borderCoordinate, depth));
                    borderCoordinate = borderCoordinate.up(1);
                }
                leftAbovePlanet.add(new CoordinateDepthPair<Coordinate, Integer>(borderCoordinate, depth));

            } catch (OutOfBoundsException e) {
            }
        }
    }

    boolean isValidPlanetPosition(List<Planet> planets) {
        for (Planet planet : planets) {
            if (planet.planetCoordinate.distance(this.planetCoordinate) <= 2 * Math.max(planet.size, this.size)) {
                return false;
            }
        }
        return true;
    }

    public static List<Planet> generatePlanets(int gridSize) {
        List<Planet> planets = new ArrayList<Planet>();

        if (gridSize >= 20) {
            Planet planet = new Planet(4, gridSize);
            boolean isValidPlanetPosition = planet.isValidPlanetPosition(planets);
            while (!isValidPlanetPosition) {
                planet = new Planet(4, gridSize);
                isValidPlanetPosition = planet.isValidPlanetPosition(planets);
            }
            planets.add(planet);

        }
        if (gridSize >= 15) {
            Planet planet = new Planet(3, gridSize);
            boolean isValidPlanetPosition = planet.isValidPlanetPosition(planets);
            while (!isValidPlanetPosition) {
                planet = new Planet(3, gridSize);
                isValidPlanetPosition = planet.isValidPlanetPosition(planets);
            }
            planets.add(planet);

        }
        for (int i = 0; i < 2; i++) {
            Planet planet = new Planet(2, gridSize);
            boolean isValidPlanetPosition = planet.isValidPlanetPosition(planets);
            while (!isValidPlanetPosition) {
                planet = new Planet(2, gridSize);
                isValidPlanetPosition = planet.isValidPlanetPosition(planets);
            }
            planets.add(planet);

        }

        return planets;

    }

    public Coordinate getPlanetRebound(Coordinate coordinate) {
        try {
            for (int depth = 0; depth < size; depth++) {
                if (abovePlanet
                        .contains(new CoordinateDepthPair<Coordinate, Integer>(coordinate, depth))) {
                    coordinate = coordinate.down(size + 2 * depth + 1);

                } else if (belowPlanet
                        .contains(new CoordinateDepthPair<Coordinate, Integer>(coordinate, depth))) {
                    coordinate = coordinate.up(size + 2 * depth + 1);

                } else if (leftOfPlanet
                        .contains(new CoordinateDepthPair<Coordinate, Integer>(coordinate, depth))) {
                    coordinate = coordinate.right(size + 2 * depth + 1);

                } else if (rightOfPlanet
                        .contains(new CoordinateDepthPair<Coordinate, Integer>(coordinate, depth))) {
                    coordinate = coordinate.left(size + 2 * depth + 1);

                } else if (rightAbovePlanet
                        .contains(new CoordinateDepthPair<Coordinate, Integer>(coordinate, depth))) {
                    coordinate = coordinate.left(size + 2 * depth + 1)
                            .down(size + 2 * depth + 1);

                } else if (rightBelowPlanet
                        .contains(new CoordinateDepthPair<Coordinate, Integer>(coordinate, depth))) {
                    coordinate = coordinate.left(size + 2 * depth + 1)
                            .up(size + 2 * depth + 1);

                } else if (leftAbovePlanet
                        .contains(new CoordinateDepthPair<Coordinate, Integer>(coordinate, depth))) {
                    coordinate = coordinate.right(size + 2 * depth + 1)
                            .down(size + 2 * depth + 1);

                } else if (leftBelowPlanet
                        .contains(new CoordinateDepthPair<Coordinate, Integer>(coordinate, depth))) {
                    coordinate = coordinate.right(size + 2 * depth + 1)
                            .up(size + 2 * depth + 1);

                } else
                    continue;
                System.out.println(
                        "A nearby planet's gravitational field has pulled the cannon ball around!");
                return coordinate;

            }

        } catch (OutOfBoundsException e) {

        }
        return null;

    }

}
