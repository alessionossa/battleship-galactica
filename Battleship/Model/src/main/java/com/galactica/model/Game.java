package com.galactica.model;

import java.util.List;

public class Game {
    public static Grid setUpGrid(int gridSize, boolean singlePlayerMode, boolean asteroidMode,
            boolean gravityMode) {
        Coordinate.setMaxValue(gridSize);
        Planet.setMaxPlanetLength((int) (Math.floor(Math.abs(Math.min(gridSize, 20) / 5))));
        Grid grid = new Grid(gridSize);

        if (gravityMode) {
            List<Planet> planets = Planet.generatePlanets(gridSize);

            grid.placePlanets(planets);
        }

        if (asteroidMode) {
            grid.placeAsteroids();
        }

        return grid;
    }
}
