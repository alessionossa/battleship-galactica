package com.galactica.gui;

import com.galactica.model.Ship;
import com.galactica.model.ships.Cruiser;
import com.galactica.model.ships.DeathStar;
import com.galactica.model.ships.Scout;
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

        for (Node node : grid.getChildren()) {
            node.setOnMouseEntered((MouseEvent t) -> {
                node.setStyle("-fx-background-color:#FFFF00;");
            });

            node.setOnMouseExited((MouseEvent t) -> {
                node.setStyle("-fx-background-color:#dae7f3;");
            });
        }
    }

    private void setupShipList() {
        shipsListView.getItems().addAll(ships);
        shipsListView.setCellFactory(listView -> new ShipListCell());

        shipsListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Ship>() {
            @Override
            public void changed(ObservableValue<? extends Ship> observableValue, Ship ship, Ship t1) {
                selectedShip = t1;
            }
        });
    }

    private void createGrid() {
        int tableSize = gridSize + 1;
        // Create the grid
        for (int i = 0; i < tableSize; i++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setHgrow(Priority.NEVER);
            grid.getColumnConstraints().add(column);

            RowConstraints row = new RowConstraints();
            row.setVgrow(Priority.NEVER);
            grid.getRowConstraints().add(row);

            for (int j = 0; j < tableSize; j++) {
                StackPane tile = new StackPane();
                tile.getStyleClass().add("tile");
                grid.add(tile, i, j);

                if (i == 0 || j == 0) {
                    String coordinate = (i == 0 && j > 0) ? String.valueOf(j) : (j == 0 && i > 0) ? String.valueOf((char) ('A' + i - 1)) : "";
                    Label label = new Label(coordinate);
                    tile.setAlignment(Pos.CENTER);
                    tile.getChildren().add(label);
                }
            }
        }
    }
}
