package com.galactica.gui.view;

import com.galactica.model.Direction;
import com.galactica.model.ships.Cruiser;
import com.galactica.model.ships.DeathStar;
import com.galactica.model.ships.Scout;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.fxml.FXML;
import javafx.scene.layout.*;
import javafx.scene.layout.BorderPane;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javafx.event.*;

import com.galactica.model.Ship;

public class GameplayController {

    private int gridSize;

    ObservableList<Ship> shipsToPlace;

    Set<Ship> placedShips = new HashSet<>();

    @FXML
    private BorderPane borderPane;

    @FXML
    private GridContainer gridContainer;

    @FXML
    private ListView<Ship> shipsListView;

    @FXML
    private Button rotateButton;

    @FXML
    private Button removeShipButton;

    @FXML
    private Button startGameButton;

    private Ship selectedShip;

    private final HashMap<Ship, ImageView> shipImages = new HashMap<>();

    public GameplayController(int gridSize) {
        this.gridSize = gridSize;

        Ship[] ships = new Ship[] { new Cruiser(1), new DeathStar(2), new Scout(3) };

        this.shipsToPlace = FXCollections.observableArrayList(ships);
    }

    public void initialize() {
        // TODO: Set up the player mode, asteroids and gravity if needed here

        gridContainer.setGridSize(gridSize);

        setupShipList();

        startGameButton.setDisable(false);

        addTileEventHandlers();
    }

    private void setupShipList() {
        shipsListView.setItems(shipsToPlace);
        shipsListView.setCellFactory(listView -> new ShipListCell());

        shipsListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Ship>() {
            @Override
            public void changed(ObservableValue<? extends Ship> observableValue, Ship oldValue, Ship newValue) {
                if (newValue != null) {
                    handleShipSelection(newValue);
                }
            }
        });

        shipsListView.getSelectionModel().selectFirst();
    }

    private void previewShipPlacement(Ship ship, StackPane cell) {
        ImageView shipImageView = this.shipImages.get(ship);

        if (shipImageView == null) {
            shipImageView =gridContainer.getShipImageView(ship);

            shipImageView.setOpacity(0.5);
            shipImageView.setPickOnBounds(false);
            shipImageView.setMouseTransparent(true);

            this.shipImages.put(ship, shipImageView);

            gridContainer.updateShipImagePosition(shipImageView, cell);
            gridContainer.updateImageDirection(ship, shipImageView);
            gridContainer.getChildren().add(shipImageView);
        } else {
            gridContainer.updateShipImagePosition(shipImageView, cell);
        }

        if (true) { // TODO: if position is not valid
            ColorAdjust colorAdjust = new ColorAdjust();
            colorAdjust.setSaturation(-0.9);
            shipImageView.setEffect(colorAdjust);
        }
    }

    private void placeShip(Ship ship, int columnIndex, int rowIndex, StackPane cell) {
        ImageView shipImageView = this.shipImages.get(ship);

        shipImageView.setOpacity(1.0);
        shipImageView.setEffect(null);
        shipImageView.setMouseTransparent(false);
        shipImageView.setPickOnBounds(true);
        gridContainer.updateShipImagePosition(shipImageView, cell);

        placedShips.add(ship);
        shipsToPlace.remove(ship);

        shipImageView.setOnMouseClicked(event -> {
            handleShipSelection(ship);
        });
    }

    private void handleShipSelection(Ship ship) {
        Platform.runLater(() -> {
            ImageView previouslySelectedImageView = this.shipImages.get(this.selectedShip);
            if (previouslySelectedImageView != null) {
                previouslySelectedImageView.getStyleClass().remove("bordered-view");
            }

            if (this.selectedShip == ship) {
                this.selectedShip = null;
            } else {
                this.selectedShip = ship;
            }

            if (placedShips.contains(this.selectedShip)) {
                shipsListView.getSelectionModel().clearSelection();
                rotateButton.setDisable(true);
                removeShipButton.setDisable(false);

                ImageView shipImageView = this.shipImages.get(ship);
                if (shipImageView != null) {
                    shipImageView.getStyleClass().add("bordered-view");
                }
            } else if (shipsToPlace.contains(this.selectedShip)) {
                rotateButton.setDisable(false);
                removeShipButton.setDisable(true);
                shipsListView.getSelectionModel().select(ship);
            } else {
                shipsListView.getSelectionModel().clearSelection();
                rotateButton.setDisable(true);
                removeShipButton.setDisable(true);
            }
        });
    }

    private void addTileEventHandlers() {
        int tableSize = gridSize + 1;
        for (int rowIndex = 1; rowIndex < tableSize; rowIndex++) {
            for (int columnIndex = 1; columnIndex < tableSize; columnIndex++) {
                StackPane tile = (StackPane) gridContainer.getTiles()[rowIndex][columnIndex];

                final int currentRowIndex = rowIndex;
                final int currentColumnIndex = columnIndex;
                tile.setOnMouseEntered(event -> {
                    // System.out.printf("Mouse entered cell [%d, %d]%n", currentColumnIndex, currentRowIndex);
                    if (this.selectedShip != null && !placedShips.contains(this.selectedShip)) {
                        previewShipPlacement(this.selectedShip, tile);
                    }
                });

                tile.setOnMouseExited(event -> {
                    if (this.selectedShip != null && !placedShips.contains(this.selectedShip)) {
                        previewShipPlacement(this.selectedShip, null);
                    }
                });

                tile.setOnMouseClicked(event -> {
                    if (this.selectedShip != null && !placedShips.contains(this.selectedShip)) {
                        placeShip(this.selectedShip, currentColumnIndex, currentRowIndex, tile);
                    }
                });
            }
        }
    }

    @FXML
    public void shootButtonAction(ActionEvent event) {

    }

    @FXML
    public void exitGameButtonAction(ActionEvent event) {

    }

}
