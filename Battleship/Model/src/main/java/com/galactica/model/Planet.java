package com.galactica.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Planet {
    private Coordinate planetCoordinate;
    private List<Coordinate> abovePlanet = new ArrayList<Coordinate>();;
    private List<Coordinate> belowPlanet = new ArrayList<Coordinate>();;
    private List<Coordinate> rightOfPlanet = new ArrayList<Coordinate>();;
    private List<Coordinate> leftOfPlanet = new ArrayList<Coordinate>();;
    private List<Coordinate> rightAbovePlanet = new ArrayList<Coordinate>();;
    private List<Coordinate> leftAbovePlanet = new ArrayList<Coordinate>();;
    private List<Coordinate> rightBelowPlanet = new ArrayList<Coordinate>();;
    private List<Coordinate> leftBelowPlanet = new ArrayList<Coordinate>();;
    private int size;

    public Coordinate getCoordinates() {
        return planetCoordinate;
    }

    public List<Coordinate> getAbovePlanet() {
        return abovePlanet;
    }

    public List<Coordinate> getBelowPlanet() {
        return belowPlanet;
    }

    public List<Coordinate> getRightOfPlanet() {
        return rightOfPlanet;
    }

    public List<Coordinate> getLeftOfPlanet() {
        return leftOfPlanet;
    }

    public List<Coordinate> getRightAbovePlanet() {
        return rightAbovePlanet;
    }

    public List<Coordinate> getLeftAbovePlanet() {
        return leftAbovePlanet;
    }

    public List<Coordinate> getRightBelowPlanet() {
        return rightBelowPlanet;
    }

    public List<Coordinate> getLeftBelowPlanet() {
        return leftBelowPlanet;
    }

    public int getSize() {
        return size;
    }

    public Planet(int size) {
        this.size = size;

        Random random = new Random();
        int[] planetCoordinates = random.ints(2, 1, 8).toArray();
        planetCoordinate = new Coordinate((char) (97 + planetCoordinates[0]), planetCoordinates[1]);

        try {
            abovePlanet.add(planetCoordinate.up());
            abovePlanet.add(planetCoordinate.up().right());
        } catch (OutOfBoundsException e) {
        }
        try {
            belowPlanet.add(planetCoordinate.down().down());
            belowPlanet.add(planetCoordinate.down().down().right());
        } catch (OutOfBoundsException e) {
        }
        try {
            leftOfPlanet.add(planetCoordinate.left());
            leftOfPlanet.add(planetCoordinate.left().down());
        } catch (OutOfBoundsException e) {
        }
        try {
            rightOfPlanet.add(planetCoordinate.right().right());
            rightOfPlanet.add(planetCoordinate.right().right().down());
        } catch (OutOfBoundsException e) {
        }
        try {
            rightAbovePlanet.add(planetCoordinate.right().right().up());
        } catch (OutOfBoundsException e) {
        }
        try {
            leftAbovePlanet.add(planetCoordinate.left().up());
        } catch (OutOfBoundsException e) {
        }
        try {
            rightBelowPlanet.add(planetCoordinate.right().right().down().down());
        } catch (OutOfBoundsException e) {
        }
        try {
            leftBelowPlanet.add(planetCoordinate.left().down().down());
        } catch (OutOfBoundsException e) {
        }

    }

    boolean isValidPlanetPosition(List<Planet> planets) {
        for (Planet planet : planets) {
            if (planet.planetCoordinate.distance(this.planetCoordinate) <= this.size) { // TODO max size
                return false;
            }
        }
        return true;
    }

    public static List<Planet> generatePlanets(int numberOfPlanets) {
        List<Planet> planets = new ArrayList<Planet>();
        for (int i = 0; i < numberOfPlanets; i++) {
            Planet planet = new Planet(4);
            boolean isValidPlanetPosition = planet.isValidPlanetPosition(planets);
            while (!isValidPlanetPosition) {
                planet = new Planet(4);
                isValidPlanetPosition = planet.isValidPlanetPosition(planets);
            }
            planets.add(planet);

        }
        return planets;
    }

}
