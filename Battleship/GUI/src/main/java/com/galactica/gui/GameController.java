package com.galactica.gui;

import javafx.scene.image.ImageView;
import javafx.fxml.FXML;
import javafx.scene.layout.*;
import java.util.HashMap;

import com.galactica.model.Tile;
import com.galactica.model.Ship;

public class GameController {

    private int gridSize;
    private int playerTurn;
    private boolean singlePlayer;
    private boolean asteroids;
    private boolean gravity;

    @FXML
    private BorderPane borderPane;

    @FXML
    private GridPane grid;

    @FXML
    private StackPane gridContainer;

    @FXML
    private ImageView backgroundImageView;

    private Tile selectedTile;

    private final HashMap<Ship, ImageView> shipImages = new HashMap<>();

    private final HashMap<Integer, ImageView> gridImages = new HashMap<>();

    public GameController(int gridSize, boolean singlePlayer, boolean asteroids, boolean gravity) {
        this.gridSize = gridSize;
        this.singlePlayer = singlePlayer;
        this.asteroids = asteroids;
        this.gravity = gravity;
    }

    public void display() {
    }

    private void createPlayerGrid() {

    }

    private void createOpponentGrid() {

    }

}
