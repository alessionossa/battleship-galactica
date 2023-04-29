package com.galactica.gui;

import javafx.scene.image.ImageView;
import javafx.fxml.FXML;
import javafx.scene.layout.*;
import java.util.HashMap;
import javafx.scene.control.Label;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.stage.Stage;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;

import com.galactica.model.Tile;
import com.galactica.model.Ship;

public class GameController {

    private Stage stage;
    private Scene scene;

    private int gridSize;
    private int playerTurn;
    private boolean singlePlayer;
    private boolean asteroids;
    private boolean gravity;

    @FXML
    private BorderPane borderPane;

    @FXML
    private GridPane PlayerGrid;
    @FXML
    private GridPane OpponentGrid;

    @FXML
    private StackPane PlayerGridContainer;

    @FXML
    private StackPane OpponentGridContainer;

    @FXML
    private ImageView PlayerBackgroundImageView;
    @FXML
    private ImageView OpponentBackgroundImageView;

    private Tile selectedTile;

    private final HashMap<Ship, ImageView> shipImages = new HashMap<>();

    public GameController(int gridSize, boolean singlePlayer, boolean asteroids, boolean gravity) {
        /*
         * this.gridSize = gridSize;
         * this.singlePlayer = singlePlayer;
         * this.asteroids = asteroids;
         * this.gravity = gravity;
         */

        this.gridSize = 15;
        this.singlePlayer = false;
        this.asteroids = false;
        this.gravity = false;

    }

    public void initialize() {
        // TODO: Set up the player mode, asteroids and gravity if needed here

        PlayerBackgroundImageView.fitWidthProperty().bind(PlayerGrid.widthProperty());
        PlayerBackgroundImageView.fitHeightProperty().bind(PlayerGrid.heightProperty());

        PlayerGrid.getStyleClass().add("grid");

        OpponentBackgroundImageView.fitWidthProperty().bind(OpponentGrid.widthProperty());
        OpponentBackgroundImageView.fitHeightProperty().bind(OpponentGrid.heightProperty());

        OpponentGrid.getStyleClass().add("grid");

        createPlayerGrid();
        createOpponentGrid();

        // Bind the column and row constraints to maintain square tiles
        NumberBinding PlayerTileSize = Bindings.min(borderPane.widthProperty().divide(gridSize + 2),
                borderPane.heightProperty().divide(gridSize + 2));
        PlayerTileSize.addListener((obs, oldSize, newSize) -> {
            for (ColumnConstraints column : PlayerGrid.getColumnConstraints()) {
                column.setPrefWidth(newSize.doubleValue());
                column.setMaxWidth(newSize.doubleValue());
            }
            for (RowConstraints row : PlayerGrid.getRowConstraints()) {
                row.setPrefHeight(newSize.doubleValue());
                row.setMaxHeight(newSize.doubleValue());
            }
        });

        // Bind the column and row constraints to maintain square tiles
        NumberBinding OpponentTileSize = Bindings.min(borderPane.widthProperty().divide(gridSize + 2),
                borderPane.heightProperty().divide(gridSize + 2));
        OpponentTileSize.addListener((obs, oldSize, newSize) -> {
            for (ColumnConstraints column : OpponentGrid.getColumnConstraints()) {
                column.setPrefWidth(newSize.doubleValue());
                column.setMaxWidth(newSize.doubleValue());
            }
            for (RowConstraints row : OpponentGrid.getRowConstraints()) {
                row.setPrefHeight(newSize.doubleValue());
                row.setMaxHeight(newSize.doubleValue());
            }
        });
    }

    private void createPlayerGrid() {
        int tableSize = gridSize + 1;
        // Create the grid
        for (int rowIndex = 0; rowIndex < tableSize; rowIndex++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setHgrow(Priority.NEVER);
            PlayerGrid.getColumnConstraints().add(column);

            RowConstraints row = new RowConstraints();
            row.setVgrow(Priority.NEVER);
            PlayerGrid.getRowConstraints().add(row);

            for (int columnIndex = 0; columnIndex < tableSize; columnIndex++) {
                StackPane tile = new StackPane();
                tile.getStyleClass().add("tile");
                PlayerGrid.add(tile, rowIndex, columnIndex);

                if (rowIndex == 0 || columnIndex == 0) {
                    String coordinate = (rowIndex == 0 && columnIndex > 0) ? String.valueOf(columnIndex)
                            : (columnIndex == 0 && rowIndex > 0) ? String.valueOf((char) ('A' + rowIndex - 1)) : "";
                    Label label = new Label(coordinate);
                    tile.setAlignment(Pos.CENTER);
                    tile.getChildren().add(label);
                }
            }
        }
    }

    private void createOpponentGrid() {
        int tableSize = gridSize + 1;
        // Create the grid
        for (int rowIndex = 0; rowIndex < tableSize; rowIndex++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setHgrow(Priority.NEVER);
            OpponentGrid.getColumnConstraints().add(column);

            RowConstraints row = new RowConstraints();
            row.setVgrow(Priority.NEVER);
            OpponentGrid.getRowConstraints().add(row);

            for (int columnIndex = 0; columnIndex < tableSize; columnIndex++) {
                StackPane tile = new StackPane();
                tile.getStyleClass().add("tile");
                OpponentGrid.add(tile, rowIndex, columnIndex);

                if (rowIndex == 0 || columnIndex == 0) {
                    String coordinate = (rowIndex == 0 && columnIndex > 0) ? String.valueOf(columnIndex)
                            : (columnIndex == 0 && rowIndex > 0) ? String.valueOf((char) ('A' + rowIndex - 1)) : "";
                    Label label = new Label(coordinate);
                    tile.setAlignment(Pos.CENTER);
                    tile.getChildren().add(label);
                } else {
                    final int currentRowIndex = rowIndex;
                    final int currentColumnIndex = columnIndex;
                    tile.setOnMouseEntered(event -> {
                        tile.setStyle("-fx-background-color:#FFFF00;");
                        System.out.printf("Mouse enetered cell [%d, %d]%n", currentColumnIndex, currentRowIndex);
                    });

                    tile.setOnMouseExited(event -> {
                        tile.setStyle("-fx-background-color:none;");
                    });
                }
            }
        }
    }

}
