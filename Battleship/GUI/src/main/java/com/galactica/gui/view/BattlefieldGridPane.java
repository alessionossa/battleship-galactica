package com.galactica.gui.view;

import javafx.beans.binding.Bindings;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

public class BattlefieldGridPane extends GridPane {

    private Node[][] tiles = null;

    private int gridSize;

    public BattlefieldGridPane() {
        this.setMinSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        this.setMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);

//        this.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
//        this.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        this.getStyleClass().add("grid");
    }

    public void initializeGrid(int gridSize) {
        this.gridSize = gridSize;

        createGrid();
    }

    private void createGrid() {
        int tableSize = gridSize + 1;
        tiles = new Node[tableSize][tableSize];
        // Create the grid
        for (int columnIndex = 0; columnIndex < tableSize; columnIndex++) {
            ColumnConstraints column = new ColumnConstraints();
//            column.setHgrow(Priority.NEVER);
            column.setPercentWidth(100.0 / tableSize);
            this.getColumnConstraints().add(column);

            RowConstraints row = new RowConstraints();
//            row.setVgrow(Priority.NEVER);
            column.setPercentWidth(100.0 / tableSize);
            this.getRowConstraints().add(row);

            for (int rowIndex = 0; rowIndex < tableSize; rowIndex++) {
                StackPane tile = new StackPane();
//                tile.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
//                tile.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
//                tile.prefWidthProperty().bind(Bindings.min(this.widthProperty().divide(tableSize), this.heightProperty().divide(tableSize)));
//                tile.prefHeightProperty().bind(tile.widthProperty());
                GridPane.setHgrow(tile, Priority.ALWAYS);
                GridPane.setVgrow(tile, Priority.ALWAYS);
                tile.getStyleClass().add("tile");
                this.add(tile, columnIndex, rowIndex);

                if (columnIndex == 0 || rowIndex == 0) {
                    tile.getStyleClass().add("border-tile");
                    String coordinate = (columnIndex == 0 && rowIndex > 0) ? String.valueOf(rowIndex)
                            : (rowIndex == 0 && columnIndex > 0) ? String.valueOf((char) ('A' + columnIndex - 1)) : "";
                    Label label = new Label(coordinate);

                    tile.getChildren().add(label);
                }

                tiles[rowIndex][columnIndex] = tile;
            }
        }
    }

    public Node[][] getTiles() {
        return tiles;
    }
}
