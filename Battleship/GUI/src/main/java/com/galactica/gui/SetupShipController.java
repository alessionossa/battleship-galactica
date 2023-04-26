package com.galactica.gui;

import com.galactica.model.Direction;
import com.galactica.model.Ship;
import com.galactica.model.ships.*;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.image.ImageView;
import javafx.geometry.Pos;

import java.util.HashMap;

public class SetupShipController {

    int gridSize;

    Ship[] ships;

    @FXML
    private BorderPane borderPane;

    @FXML
    private GridPane grid;

    @FXML
    private StackPane gridContainer;

    @FXML
    private ImageView backgroundImageView;

    @FXML
    private ListView<Ship> shipsListView;

    @FXML
    private Button rotateButton;

    private Ship selectedShip;

    private final HashMap<Ship, ImageView> shipImages = new HashMap<>();

    public SetupShipController(int gridSize) {
        this.gridSize = gridSize;
        this.ships = new Ship[] {new Cruiser(1), new DeathStar(2), new Scout(3)};

    }

    public void initialize() {// Set the desired grid size here
        backgroundImageView.fitWidthProperty().bind(grid.widthProperty());
        backgroundImageView.fitHeightProperty().bind(grid.heightProperty());

        grid.getStyleClass().add("grid");

        createGrid();

        // Bind the column and row constraints to maintain square tiles
        NumberBinding tileSize = Bindings.min(borderPane.widthProperty().divide(gridSize + 2), borderPane.heightProperty().divide(gridSize + 2));
        tileSize.addListener((obs, oldSize, newSize) -> {
            for (ColumnConstraints column : grid.getColumnConstraints()) {
                column.setPrefWidth(newSize.doubleValue());
                column.setMaxWidth(newSize.doubleValue());
            }
            for (RowConstraints row : grid.getRowConstraints()) {
                row.setPrefHeight(newSize.doubleValue());
                row.setMaxHeight(newSize.doubleValue());
            }
        });

        setupShipList();

        setupButton();

    }

    private void setupShipList() {
        shipsListView.getItems().addAll(ships);
        shipsListView.setCellFactory(listView -> new ShipListCell());

        shipsListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Ship>() {
            @Override
            public void changed(ObservableValue<? extends Ship> observableValue, Ship ship, Ship t1) {
                selectedShip = t1;
                rotateButton.setDisable(false);
            }
        });

        shipsListView.getSelectionModel().selectFirst();
    }

    private void setupButton() {
        rotateButton.setOnAction(event -> {
            Direction direction = selectedShip.getDirection();
            if (direction == Direction.Horizontal) {
                direction = Direction.Vertical;
            } else {
                direction = Direction.Horizontal;
            }
            selectedShip.setDirection(direction);
        });
    }

    private void previewShipPlacement() {

    }

    private void createGrid() {
        int tableSize = gridSize + 1;
        // Create the grid
        for (int rowIndex = 0; rowIndex < tableSize; rowIndex++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setHgrow(Priority.NEVER);
            grid.getColumnConstraints().add(column);

            RowConstraints row = new RowConstraints();
            row.setVgrow(Priority.NEVER);
            grid.getRowConstraints().add(row);

            for (int columnIndex = 0; columnIndex < tableSize; columnIndex++) {
                StackPane tile = new StackPane();
                tile.getStyleClass().add("tile");
                grid.add(tile, rowIndex, columnIndex);

                if (rowIndex == 0 || columnIndex == 0) {
                    String coordinate = (rowIndex == 0 && columnIndex > 0) ? String.valueOf(columnIndex) : (columnIndex == 0 && rowIndex > 0) ? String.valueOf((char) ('A' + rowIndex - 1)) : "";
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
