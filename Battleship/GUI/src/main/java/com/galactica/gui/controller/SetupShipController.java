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
import javafx.stage.Window;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

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
    private Button continueButton;

    private Ship selectedShip;

    private final HashMap<Ship, ImageView> shipImages = new HashMap<>();

    public SetupShipController(Game gameModel) {
        this.gameModel = gameModel;

        for (Ship ship: gameModel.getCurrentPlayer().getShips()) {
            ship.setDirection(Direction.Vertical);
        }

        this.shipsToPlace = FXCollections.observableArrayList(gameModel.getCurrentPlayer().getShips());
    }

    public void initialize() {
        gridContainer.setGrid(gameModel.getCurrentPlayer().getOwnGrid());

        setupShipList();

        updateContinueButton();

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
    private void removeShipButtonAction(ActionEvent event) throws UnplacedShipException {
        ImageView selectedShipImage = shipImages.get(selectedShip);

        if (selectedShipImage != null) {
            ((AnchorPane) selectedShipImage.getParent()).getChildren().remove(selectedShipImage);
            shipImages.remove(selectedShip);

            shipsToPlace.add(selectedShip);
            gameModel.getCurrentPlayer().removeShip(selectedShip);
            selectedShip.setCoordinate(null);

            handleShipSelection(null);
            updateContinueButton();
        }
    }

    private void updateContinueButton() {
        continueButton.setDisable(!gameModel.getCurrentPlayer().hasAllShipsPlaced());
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
        boolean isValidShipPosition = gameModel.getCurrentPlayer().getOwnGrid().isValidShipPosition(ship, coordinate, ship.getDirection());
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
        boolean isValidShipPosition = gameModel.getCurrentPlayer().getOwnGrid().isValidShipPosition(ship, coordinate, ship.getDirection());

        if (isValidShipPosition) {
            shipImageView.setOpacity(1.0);
            shipImageView.setEffect(null);
            shipImageView.setMouseTransparent(false);
            shipImageView.setPickOnBounds(true);
            gridContainer.updateShipImagePosition(shipImageView, cell);

            shipsToPlace.remove(ship);

            shipImageView.setOnMouseClicked(event -> {
                handleShipSelection(ship);
            });

            gameModel.getCurrentPlayer().placeShip(ship, coordinate, ship.getDirection());

            updateContinueButton();
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

            if ((this.selectedShip != null) && this.selectedShip.isPlaced()) {
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
                    if (this.selectedShip != null && !this.selectedShip.isPlaced()) {
                        previewShipPlacement(this.selectedShip, tile, currentColumnIndex, currentRowIndex);
                    }
                });

                tile.setOnMouseExited(event -> {
                    if (this.selectedShip != null && !this.selectedShip.isPlaced()) {
                        previewShipPlacement(this.selectedShip, null, currentColumnIndex, currentRowIndex);
                    }
                });

                tile.setOnMouseClicked(event -> {
                    if (this.selectedShip != null && !this.selectedShip.isPlaced()) {
                        placeShip(this.selectedShip, currentColumnIndex, currentRowIndex, tile);
                    }
                });
            }
        }
    }

    @FXML
    public void continueButtonAction(ActionEvent event) throws IOException {
        Scene currentScene = ((Node) event.getSource()).getScene();

        if (gameModel.getSinglePlayerMode()) {
            AI p2 = (AI) gameModel.getP2();
            p2.placeShips();

            switchToGamePlayView(currentScene);
        } else {
            gameModel.nextPlayerTurn();
            if (gameModel.getPlayerTurn() == 2) {
                switchToGamePlayView(currentScene);
            } else {
                switchToNextSetupScene(currentScene);
            }

        }
    }

    // Navigation

    private void switchToGamePlayView(Scene currentScene) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("game-view.fxml"));

        fxmlLoader.setController(new GameplayController(gameModel));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) currentScene.getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    private void switchToNextSetupScene(Scene currentScene) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("switch-player-view.fxml"));

        Consumer<Window> switchToNextSetupShipBlock = (Window window) -> {
            FXMLLoader fxmlLoaderSetupShips = new FXMLLoader(getClass().getClassLoader().getResource("setup-ships-view.fxml"));

            // PASS CONFIG FOR GRID SIZE, PLAYER MODE, GRAVITY AND ASTEROIDS
            fxmlLoaderSetupShips.setController(new SetupShipController(this.gameModel));

            Parent root;
            try {
                root = fxmlLoaderSetupShips.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Stage stage = (Stage) window;
            Scene scene = new Scene(root);
            stage.setScene(scene);
        };
        fxmlLoader.setController(new SwitchPlayerController(switchToNextSetupShipBlock));

        Parent root = fxmlLoader.load();
        Stage stage = (Stage) currentScene.getWindow();
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
