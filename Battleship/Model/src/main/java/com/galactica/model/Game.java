package com.galactica.model;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;

// Game class representing the overall state of the game
public class Game {
    private int playerTurn;
    private boolean asteroidMode;
    private boolean singlePlayerMode;
    private boolean gravityMode;
    private int gridSize;

    private Human p1;
    private Player p2;


    private Grid grid1;
    private Grid grid2;


    // Constructor for initializing the game state
    public Game(int playerTurn, boolean asteroidMode, boolean singlePlayerMode, boolean gravityMode,
            int gridSize, Human p1, Player p2, Grid grid1, Grid grid2) {
        this.playerTurn = playerTurn;
        this.asteroidMode = asteroidMode;
        this.singlePlayerMode = singlePlayerMode;
        this.gravityMode = gravityMode;
        this.gridSize = gridSize;
        this.p1 = p1;
        this.p2 = p2;
        this.grid1 = grid1;
        this.grid2 = grid2;
    }
    
    public Game() {
    }

    // Method for setting up the grid based on game settings
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

    public void setPlayerTurn(int playerTurn) {
        this.playerTurn = playerTurn;
    }

    public int getPlayerTurn() {
        return playerTurn;
    }

    public void setAsteroidMode(boolean asteroidMode) {
        this.asteroidMode = asteroidMode;
    }

    public boolean getAsteroidMode() {
        return asteroidMode;
    }

    public void setSinglePlayerMode(boolean singlePlayerMode) {
        this.singlePlayerMode = singlePlayerMode;
    }

    public boolean getSinglePlayerMode() {
        return singlePlayerMode;
    }

    public void setGravityMode(boolean gravityMode) {
        this.gravityMode = gravityMode;
    }

    public boolean getGravityMode() {
        return gravityMode;
    }

    public void setGridSize(int gridSize) {
        this.gridSize = gridSize;
    }

    public int getGridSize() {
        return gridSize;
    }

    public void setP1(Human p1) {
        this.p1 = p1;
    }

    public Human getP1() {
        return p1;
    }

    public void setP2(Player p2) {
        this.p2 = p2;
    }

    public Player getP2() {
        return p2;
    }

    public void setGrid1(Grid grid1) {
        this.grid1 = grid1;
    }

    public void setGrid2(Grid grid2) {
        this.grid2 = grid2;
    }

    public Grid getGrid1() {
        return grid1;
    }

    public Grid getGrid2() {
        return grid2;
    }

    public Player getCurrentPlayer() {
        return (playerTurn == 1) ? p1 : p2;
    }

    public void nextPlayerTurn() {
        if (playerTurn == 1)
            playerTurn = 2;
        else
            playerTurn = 1;
    }

// Method for converting the game state to a JsonObject
    public JsonObject toJsonObject() {
        JsonObject jo = new JsonObject();
        jo.put("playerTurn", playerTurn);
        jo.put("asteroidMode", asteroidMode);
        jo.put("singlePlayerMode", singlePlayerMode);
        jo.put("gravityMode", gravityMode);
        jo.put("gridSize", gridSize);
        jo.put("p1", p1.toJsonObject());
        jo.put("p2", p2.toJsonObject());
        jo.put("grid1", grid1.toJsonObject());
        jo.put("grid2", grid2.toJsonObject());

        return jo;
    }

    // Method for creating a Game object from a JsonObject
    public Game fromJsonObject(JsonObject jo) {
        playerTurn = ((BigDecimal) jo.get("playerTurn")).intValue();
        asteroidMode = (boolean) jo.get("asteroidMode");
        singlePlayerMode = (boolean) jo.get("singlePlayerMode");
        gravityMode = (boolean) jo.get("gravityMode");
        gridSize = ((BigDecimal) jo.get("gridSize")).intValue();
        grid1 = Grid.fromJsonObject((JsonObject) jo.get("grid1"));
        grid2 = Grid.fromJsonObject((JsonObject) jo.get("grid2"));

        p1 = Human.fromJsonObject((JsonObject) jo.get("p1"), grid1, grid2);
        if (singlePlayerMode) {
            p2 = AI.fromJsonObject((JsonObject) jo.get("p2"), grid2, grid1);
        } else {
            p2 = Human.fromJsonObject((JsonObject) jo.get("p2"), grid2, grid1);
        }

        return this;
    }

    // Method for getting the default save path for the game
    public static Path getDefaultPath() {
        String home = System.getProperty("user.home");
        return Paths.get(home).resolve("battleship.json");
    }

    // Method for saving the game state using the default save path
    public void save() {
        save(getDefaultPath());
    }

    // Method for saving the game state to a specified path
    public void save(Path path) {
        JsonObject gameJSON = new JsonObject();
        gameJSON.put("game", this.toJsonObject());

        String gameJSONtring = Jsoner.serialize(gameJSON);
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new RuntimeException("Unable to delete old game file", e);
        }

        try {
            Files.write(path, gameJSONtring.getBytes(), StandardOpenOption.CREATE);
        } catch (IOException e) {
            throw new RuntimeException("Unable to save game", e);
        }
    }

    // Method for loading the game state from a specified path
    public Game load(Path path) {
        String jsonText = null;
        JsonObject gameJSON = null;

        try {
            jsonText = Files.readString(path);
        } catch (IOException e) {
            throw new RuntimeException("Unable to read game file", e);
        }

        try {
            gameJSON = (JsonObject) Jsoner.deserialize(jsonText);
        } catch (Exception e) {
            throw new RuntimeException("Unable to parse game file", e);
        }

        return fromJsonObject((JsonObject) gameJSON.get("game"));
    }
}
