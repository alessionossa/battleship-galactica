package com.galactica.gui;

import javafx.scene.image.ImageView;
import javafx.fxml.FXML;
import javafx.scene.layout.*;
import com.galactica.model.Tile;

public class GameController {

    private int gridSize;
    private int player1;
    private int player2;

    @FXML
    private BorderPane borderPane;

    @FXML
    private GridPane grid;

    @FXML
    private StackPane gridContainer;

    @FXML
    private ImageView backgroundImageView;

    private Tile selectedTile;
}
