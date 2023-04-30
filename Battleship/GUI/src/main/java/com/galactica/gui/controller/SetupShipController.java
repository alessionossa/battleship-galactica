package com.galactica.gui.controller;

import com.galactica.gui.view.GridContainer;
import com.galactica.gui.view.ShipListCell;
import com.galactica.model.*;
import com.galactica.model.Coordinate;
import com.galactica.model.Direction;
import com.galactica.model.Game;
import com.galactica.model.Ship;
import com.galactica.model.ships.*;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.*;
import javafx.scene.image.ImageView;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.stage.Stage;
import javafx.event.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class SetupShipController {

    private Game gameModel;

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

    public SetupShipController(Game gameModel) {
        this.gameModel = gameModel;

        Ship[] ships = new Ship[] { new Cruiser(1), new DeathStar(2), new Scout(3) };

        this.shipsToPlace = FXCollections.observableArrayList(ships);
    }

    public void initialize() {
        // TODO: Set up the player mode, asteroids and gravity if needed here

        gridContainer.setGrid(gameModel.getGrid1());

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

    @FXML
    private void rotateButtonAction(ActionEvent event) {
        Direction direction = selectedShip.getDirection();
        if (direction == Direction.Horizontal) {
            direction = Direction.Vertical;
        } else {
            direction = Direction.Horizontal;
        }
        selectedShip.setDirection(direction);
        shipsListView.refresh();

        ImageView selectedShipImage = shipImages.get(selectedShip);
        if (selectedShipImage != null) {
            gridContainer.updateImageDirection(selectedShip, selectedShipImage);
        }
    }

    @FXML
    private void removeShipButtonAction(ActionEvent event) {
        ImageView selectedShipImage = shipImages.get(selectedShip);

        if (selectedShipImage != null) {
            ((AnchorPane) selectedShipImage.getParent()).getChildren().remove(selectedShipImage);
            shipImages.remove(selectedShip);

            shipsToPlace.add(selectedShip);
            placedShips.remove(selectedShip);

            handleShipSelection(null);
        }
    }

    private void previewShipPlacement(Ship ship, StackPane cell, int columnIndex, int rowIndex) {
        ImageView shipImageView = this.shipImages.get(ship);

        if (shipImageView == null) {
            shipImageView = gridContainer.getShipImageView(ship);

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

        char charIndex = (char) ('a' + (columnIndex - 1));
        Coordinate coordinate = new Coordinate(charIndex, rowIndex - 1);
        boolean isValidShipPosition = gameModel.getGrid1().isValidShipPosition(ship, coordinate, ship.getDirection());
        if (isValidShipPosition) {
            shipImageView.setEffect(null);
        } else {
            ColorAdjust colorAdjust = new ColorAdjust();
            colorAdjust.setSaturation(-0.9);
            shipImageView.setEffect(colorAdjust);
        }
    }

    private void placeShip(Ship ship, int columnIndex, int rowIndex, StackPane cell) {
        ImageView shipImageView = this.shipImages.get(ship);

        char charIndex = (char) ('a' + (columnIndex - 1));
        Coordinate coordinate = new Coordinate(charIndex, rowIndex - 1);
        boolean isValidShipPosition = gameModel.getGrid1().isValidShipPosition(ship, coordinate, ship.getDirection());

        if (isValidShipPosition) {
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

            String resultP1;
            if (!ship.isPlaced()) {
                Ship placedShip;
                placedShip = gameModel.p1.placeShip(ship, coordinate, ship.getDirection());
                if (placedShip == null)
                    resultP1 = "Error: You cannot place a ship here.";
                else
                    resultP1 = "Placed ship successfully";
                if (gameModel.p1.hasAllShipsPlaced())
                    resultP1 = "All ships already placed";

                System.out.println(placedShip.getCoordinate());
            }

            if (gameModel.singlePlayerMode) {
                AI p2 = (AI) gameModel.p2;
                p2.placeShips();
            } else {
                String resultP2;
                Human p2 = (Human) gameModel.p2;
                if (!ship.isPlaced()) {
                    Ship placedShip;
                    placedShip = p2.placeShip(ship, coordinate, ship.getDirection());
                    if (placedShip == null)
                        resultP2 = "Error: You cannot place a ship here.";
                    else
                        resultP2 = "Placed ship successfully";
                    if (p2.hasAllShipsPlaced())
                        resultP2 = "All ships already placed";
                }
            }

            /*for (Ship sh : gameModel.p1.getShips()) {
                System.out.println(sh.getCoordinate().getX() + " " + sh.getCoordinate().getY());
            }*/

        }
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
        int tableSize = gameModel.getGridSize() + 1;
        for (int rowIndex = 1; rowIndex < tableSize; rowIndex++) {
            for (int columnIndex = 1; columnIndex < tableSize; columnIndex++) {
                StackPane tile = (StackPane) gridContainer.getTiles()[rowIndex][columnIndex];

                final int currentRowIndex = rowIndex;
                final int currentColumnIndex = columnIndex;
                tile.setOnMouseEntered(event -> {
                    // System.out.printf("Mouse entered cell [%d, %d]%n", currentColumnIndex,
                    // currentRowIndex);
                    if (this.selectedShip != null && !placedShips.contains(this.selectedShip)) {
                        previewShipPlacement(this.selectedShip, tile, currentColumnIndex, currentRowIndex);
                    }
                });

                tile.setOnMouseExited(event -> {
                    if (this.selectedShip != null && !placedShips.contains(this.selectedShip)) {
                        previewShipPlacement(this.selectedShip, null, currentColumnIndex, currentRowIndex);
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

    // Navigation

    @FXML
    public void switchToSceneGame(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("game-view.fxml"));

        fxmlLoader.setController(new GameplayController(gameModel.getGridSize()));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    @FXML
    public void switchToSettingsScene(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("settings-view.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
