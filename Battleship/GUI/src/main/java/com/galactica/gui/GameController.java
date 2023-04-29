package com.galactica.gui;

import javafx.scene.image.ImageView;
import javafx.fxml.FXML;
import javafx.scene.layout.*;
import javafx.scene.control.Label;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.stage.Stage;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.scene.layout.BorderPane;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.event.*;

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
    private BorderPane root;

    @FXML
    private GridPane PlayerGrid;

    @FXML
    private GridPane OpponentGrid;

    @FXML
    private StackPane PlayerGridContainer = (StackPane) root.lookup("#PlayereGridContainer");;

    @FXML
    private StackPane OpponentGridContainer = (StackPane) root.lookup("#OpponentGridContainer");

    @FXML
    private ImageView PlayerBackgroundImageView;

    @FXML
    private ImageView OpponentBackgroundImageView;

    private Tile selectedTile;

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

        // Add the grids to the StackPanes
        PlayerGridContainer.getChildren().add(PlayerGrid);
        OpponentGridContainer.getChildren().add(OpponentGrid);

        // Bind the column and row constraints to maintain square tiles
        NumberBinding PlayerTileSize = Bindings.min(root.widthProperty().divide(gridSize + 2),
                root.heightProperty().divide(gridSize + 2));
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
        NumberBinding OpponentTileSize = Bindings.min(root.widthProperty().divide(gridSize + 2),
                root.heightProperty().divide(gridSize + 2));
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

    @FXML
    public void switchToPlaceShips(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("setup-ships-view.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        final boolean resizable = stage.isResizable();
        stage.setScene(scene);

        stage.setResizable(!resizable);
        stage.setResizable(resizable);
    }

    @FXML
    public void shoot(ActionEvent event) throws IOException {
    }

    @FXML
    public void nextTurn(ActionEvent event) throws IOException {
    }

}
