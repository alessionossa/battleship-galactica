package com.galactica.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;

public class Planet {
    // Instance variables
    private int identifier;
    private Coordinate planetCoordinate;
    private List<Coordinate> planetCoordinates = new ArrayList<Coordinate>();
    private List<CoordinateDepthPair> abovePlanet = new ArrayList<CoordinateDepthPair>();
    private List<CoordinateDepthPair> belowPlanet = new ArrayList<CoordinateDepthPair>();
    private List<CoordinateDepthPair> rightOfPlanet = new ArrayList<CoordinateDepthPair>();
    private List<CoordinateDepthPair> leftOfPlanet = new ArrayList<CoordinateDepthPair>();
    private List<CoordinateDepthPair> rightAbovePlanet = new ArrayList<CoordinateDepthPair>();
    private List<CoordinateDepthPair> leftAbovePlanet = new ArrayList<CoordinateDepthPair>();
    private List<CoordinateDepthPair> rightBelowPlanet = new ArrayList<CoordinateDepthPair>();
    private List<CoordinateDepthPair> leftBelowPlanet = new ArrayList<CoordinateDepthPair>();
    private int size;
    private static int maxPlanetLength;
    
    public int getSize() {
        return size;
    }


    public Planet(Coordinate planetCoordinate, List<Coordinate> planetCoordinates, int size, int maxPlanetLength,
            List<CoordinateDepthPair> abovePlanet, List<CoordinateDepthPair> belowPlanet,
            List<CoordinateDepthPair> rightOfPlanet,
            List<CoordinateDepthPair> leftOfPlanet, List<CoordinateDepthPair> rightAbovePlanet,
            List<CoordinateDepthPair> leftAbovePlanet, List<CoordinateDepthPair> rightBelowPlanet,
            List<CoordinateDepthPair> leftBelowPlanet) {
        this.planetCoordinate = planetCoordinate;
        this.size = size;
        Planet.maxPlanetLength = maxPlanetLength;
        this.abovePlanet = abovePlanet;
        this.belowPlanet = belowPlanet;
        this.rightOfPlanet = rightOfPlanet;
        this.leftOfPlanet = leftOfPlanet;
        this.rightAbovePlanet = rightAbovePlanet;
        this.leftAbovePlanet = leftAbovePlanet;
        this.rightBelowPlanet = rightBelowPlanet;
        this.leftBelowPlanet = leftBelowPlanet;
        this.identifier = IdGenerator.get();
    }

    // Constructor with size and gridSize parameters
    public Planet(int size, int gridSize) {
        this.identifier = IdGenerator.get();
        this.size = size;

        // Generate random coordinates for planet position
        Random random = new Random();
        int[] planetRandomCoordinates = random.ints(2, 1, gridSize - maxPlanetLength).toArray();
        planetCoordinate = new Coordinate((char) ('a' + planetRandomCoordinates[0]), planetRandomCoordinates[1]);
        Coordinate borderCoordinate = planetCoordinate;

        // Fill planetCoordinates list with planet's grid cells
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                planetCoordinates
                        .add(new Coordinate((char) (planetCoordinate.getX() + r), planetCoordinate.getY() + c));
            }
        }

        // Calculate gravitational field for each side of the planet
        for (int depth = 0; depth < (size - 1); depth++) {
            try {
                // Above planet
                borderCoordinate = borderCoordinate.up(1);
                for (int i = 0; i < size + depth * 2; i++) {
                    abovePlanet.add(new CoordinateDepthPair(borderCoordinate, depth));
                    borderCoordinate = borderCoordinate.right(1);
                }
                // Right above planet
                rightAbovePlanet.add(new CoordinateDepthPair(borderCoordinate, depth));
                borderCoordinate = borderCoordinate.down(1);
                for (int i = 0; i < size + depth * 2; i++) {
                    rightOfPlanet.add(new CoordinateDepthPair(borderCoordinate, depth));
                    borderCoordinate = borderCoordinate.down(1);
                }

                // Right below planet
                rightBelowPlanet.add(new CoordinateDepthPair(borderCoordinate, depth));
                borderCoordinate = borderCoordinate.left(1);
                for (int i = 0; i < size + depth * 2; i++) {
                    belowPlanet.add(new CoordinateDepthPair(borderCoordinate, depth));
                    borderCoordinate = borderCoordinate.left(1);
                }
                // Left below planet
                leftBelowPlanet.add(new CoordinateDepthPair(borderCoordinate, depth));
                borderCoordinate = borderCoordinate.up(1);
                for (int i = 0; i < size + depth * 2; i++) {
                    leftOfPlanet.add(new CoordinateDepthPair(borderCoordinate, depth));
                    borderCoordinate = borderCoordinate.up(1);
                }
                // Left above planet
                leftAbovePlanet.add(new CoordinateDepthPair(borderCoordinate, depth));

            } catch (OutOfBoundsException e) {
            }
        }
    }

    public int getIdentifier() {
        return identifier;
    }

    // Set the maximum planet length
    public static final void setMaxPlanetLength(int maxPlanetLength) {
        Planet.maxPlanetLength = maxPlanetLength;
    }

    // Getter for the planet coordinate
    public Coordinate getCoordinate() {
        return planetCoordinate;
    }

    // Getter for the list of planet coordinates
    public List<Coordinate> getPlanetCoordinates() {
        return planetCoordinates;
    }

// Check if the planet's position is valid
    boolean isValidPlanetPosition(List<Planet> planets) {
        for (Planet planet : planets) {
            if (planet.planetCoordinate.distance(this.planetCoordinate) <= 2 * Math.max(planet.size, this.size)) {
                return false;
            }
        }
        return true;
    }

    // Generate planets for the given grid size
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

    // Get the new coordinate after rebounding off the planet
    public Coordinate getPlanetRebound(Coordinate coordinate) {
        try {
            for (int depth = 0; depth < size; depth++) {
                if (abovePlanet
                        .contains(new CoordinateDepthPair(coordinate, depth))) {
                    coordinate = coordinate.down(size + 2 * depth + 1);

                } else if (belowPlanet
                        .contains(new CoordinateDepthPair(coordinate, depth))) {
                    coordinate = coordinate.up(size + 2 * depth + 1);

                } else if (leftOfPlanet
                        .contains(new CoordinateDepthPair(coordinate, depth))) {
                    coordinate = coordinate.right(size + 2 * depth + 1);

                } else if (rightOfPlanet
                        .contains(new CoordinateDepthPair(coordinate, depth))) {
                    coordinate = coordinate.left(size + 2 * depth + 1);

                } else if (rightAbovePlanet
                        .contains(new CoordinateDepthPair(coordinate, depth))) {
                    coordinate = coordinate.left(size + 2 * depth + 1)
                            .down(size + 2 * depth + 1);

                } else if (rightBelowPlanet
                        .contains(new CoordinateDepthPair(coordinate, depth))) {
                    coordinate = coordinate.left(size + 2 * depth + 1)
                            .up(size + 2 * depth + 1);

                } else if (leftAbovePlanet
                        .contains(new CoordinateDepthPair(coordinate, depth))) {
                    coordinate = coordinate.right(size + 2 * depth + 1)
                            .down(size + 2 * depth + 1);
                    
                } else if (leftBelowPlanet
                        .contains(new CoordinateDepthPair(coordinate, depth))) {
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
    
    // Convert a list of Coordinates or CoordinateDepthPairs to a JsonArray
    public JsonArray toJsonArray(List<?> list) {
        JsonArray ja = new JsonArray();

        for (Object object : list) {
            if (object instanceof Coordinate)
                ja.add(((Coordinate) object).toJsonObject());
            else if (object instanceof CoordinateDepthPair)
                ja.add(((CoordinateDepthPair) object).toJsonObject());
        }
        return ja;
    }

    // Convert the Planet object to a JsonObject
    public JsonObject toJsonObject() {
        JsonObject jo = new JsonObject();
        jo.put("planetCoordinate", planetCoordinate.toJsonObject());
        jo.put("planetCoordinates", toJsonArray(planetCoordinates));
        jo.put("abovePlanet", toJsonArray(abovePlanet));
        jo.put("belowPlanet", toJsonArray(belowPlanet));
        jo.put("leftOfPlanet", toJsonArray(leftOfPlanet));
        jo.put("rightOfPlanet", toJsonArray(rightOfPlanet));
        jo.put("rightAbovePlanet", toJsonArray(rightAbovePlanet));
        jo.put("rightBelowPlanet", toJsonArray(rightBelowPlanet));
        jo.put("leftAbovePlanet", toJsonArray(leftAbovePlanet));
        jo.put("leftBelowPlanet", toJsonArray(leftBelowPlanet));
        jo.put("size", size);
        jo.put("maxPlanetLength", maxPlanetLength);

        return jo;
    }

    // Convert a JsonArray to a List of Coordinates
    public static List<Coordinate> fromJsonArrayToCoordinateList(JsonArray ja) {
        List<Coordinate> planetCoordinates = new ArrayList<Coordinate>();
        for (Object object : ja) {
            Coordinate coordinate = Coordinate.fromJsonObject((JsonObject) object);
            planetCoordinates.add(coordinate);
        }

        return planetCoordinates;
    }

    // Convert a JsonArray to a List of CoordinateDepthPairs
    public static List<CoordinateDepthPair> fromJsonArrayToCoordinateDepthPairList(JsonArray ja) {
        List<CoordinateDepthPair> list = new ArrayList<CoordinateDepthPair>();
        for (Object object : ja) {
            CoordinateDepthPair coordinateDepthPair = CoordinateDepthPair.fromJsonObject((JsonObject) object);
            list.add(coordinateDepthPair);
        }

        return list;
    }

    // Create a Planet object from a JsonObject
    public static Planet fromJsonObject(JsonObject jo) {
        int size = ((BigDecimal) jo.get("size")).intValue();
        int maxPlanetLength = ((BigDecimal) jo.get("maxPlanetLength")).intValue();
        List<Coordinate> planetCoordinates = fromJsonArrayToCoordinateList((JsonArray) jo.get("planetCoordinates"));
        Coordinate planetCoordinate = Coordinate.fromJsonObject((JsonObject) jo.get("planetCoordinate"));
        List<CoordinateDepthPair> abovePlanet = fromJsonArrayToCoordinateDepthPairList(
                (JsonArray) jo.get("abovePlanet"));
        List<CoordinateDepthPair> belowPlanet = fromJsonArrayToCoordinateDepthPairList(
                (JsonArray) jo.get("belowPlanet"));
        List<CoordinateDepthPair> leftOfPlanet = fromJsonArrayToCoordinateDepthPairList(
                (JsonArray) jo.get("leftOfPlanet"));
        List<CoordinateDepthPair> rightOfPlanet = fromJsonArrayToCoordinateDepthPairList(
                (JsonArray) jo.get("rightOfPlanet"));
        List<CoordinateDepthPair> rightAbovePlanet = fromJsonArrayToCoordinateDepthPairList(
                (JsonArray) jo.get("rightAbovePlanet"));
        List<CoordinateDepthPair> rightBelowPlanet = fromJsonArrayToCoordinateDepthPairList(
                (JsonArray) jo.get("rightBelowPlanet"));
        List<CoordinateDepthPair> leftAbovePlanet = fromJsonArrayToCoordinateDepthPairList(
                (JsonArray) jo.get("leftAbovePlanet"));
        List<CoordinateDepthPair> leftBelowPlanet = fromJsonArrayToCoordinateDepthPairList(
                (JsonArray) jo.get("leftBelowPlanet"));

        return new Planet(planetCoordinate, planetCoordinates, size, maxPlanetLength, abovePlanet, belowPlanet,
                leftOfPlanet, rightOfPlanet, rightAbovePlanet, rightBelowPlanet, leftAbovePlanet, leftBelowPlanet);
    }
}
