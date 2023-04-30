package com.galactica.gui.controller;

import com.galactica.gui.view.GridContainer;
import com.galactica.gui.view.WeaponListCell;
import com.galactica.model.*;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.fxml.FXML;
import javafx.scene.layout.*;

import java.util.HashMap;

import javafx.event.*;

public class GameplayController {

    private Game gameModel;

    @FXML
    private GridContainer currentPlayerGridContainer;

    @FXML
    private GridContainer opponentGridContainer;

    @FXML
    private ListView<Weapon> weaponListView;

//    @FXML
//    private Button shootButton;

    private Weapon selectedWeapon;

    private final HashMap<Ship, ImageView> ownShipsImages = new HashMap<>();


    public GameplayController(Game gameModel) {
        this.gameModel = gameModel;

        for (Ship ship: gameModel.getCurrentPlayer().getShips()) {
            ship.setDirection(Direction.Vertical);
        }
    }

    public void initialize() {
        currentPlayerGridContainer.setGrid(gameModel.getCurrentPlayer().getOwnGrid(), false);
        opponentGridContainer.setGrid(gameModel.getCurrentPlayer().getOpponentGrid(), true);

        setupWeaponList();

        updateShootButton();

        addTileEventHandlers();

        Platform.runLater(this::displayShips);
    }

    private void displayShips() {
        for (Ship ship: gameModel.getCurrentPlayer().getShips()) {
            ImageView shipImageView = currentPlayerGridContainer.getShipImageView(ship);
            this.ownShipsImages.put(ship, shipImageView);

            Coordinate shipOriginCoordinate = ship.getCoordinate();
            int xIndex = gameModel.getCurrentPlayer().getOwnGrid().convertXToMatrixIndex(shipOriginCoordinate.getX());
            int yIndex = shipOriginCoordinate.getY();
            StackPane tile = (StackPane) currentPlayerGridContainer.getTiles()[yIndex][xIndex];
            currentPlayerGridContainer.updateShipImagePosition(shipImageView, tile);
            currentPlayerGridContainer.updateImageDirection(ship, shipImageView);
            currentPlayerGridContainer.getChildren().add(shipImageView);
        }

        for (Ship ship: gameModel.getP2().getShips()) {
            ImageView shipImageView = opponentGridContainer.getShipImageView(ship);
            this.ownShipsImages.put(ship, shipImageView);

            Coordinate shipOriginCoordinate = ship.getCoordinate();
            int xIndex = gameModel.getCurrentPlayer().getOwnGrid().convertXToMatrixIndex(shipOriginCoordinate.getX());
            int yIndex = shipOriginCoordinate.getY();
            StackPane tile = (StackPane) opponentGridContainer.getTiles()[yIndex][xIndex];
            opponentGridContainer.updateShipImagePosition(shipImageView, tile);
            opponentGridContainer.updateImageDirection(ship, shipImageView);
            opponentGridContainer.getChildren().add(shipImageView);
        }
    }

    private void setupWeaponList() {
        weaponListView.getItems().addAll(gameModel.getCurrentPlayer().getWeapons());
        weaponListView.setCellFactory(listView -> new WeaponListCell());

        weaponListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Weapon>() {
            @Override
            public void changed(ObservableValue<? extends Weapon> observableValue, Weapon oldValue, Weapon newValue) {
                if (newValue != null) {
                    handleWeaponSelection(newValue);
                }
            }
        });

        weaponListView.getSelectionModel().selectFirst();
    }

    private void updateShootButton() {
//        shootButton.setDisable(this.selectedWeapon != null); // TODO
    }

    private void previewWeaponShoot(Weapon weapon, StackPane cell, int columnIndex, int rowIndex) {

    }

    private void shoot(Weapon weapon, int columnIndex, int rowIndex, StackPane cell) {

        char charIndex = (char) ('a' + (columnIndex - 1));
        Coordinate coordinate = new Coordinate(charIndex, rowIndex - 1);
    }

    private void handleWeaponSelection(Weapon weapon) {
        this.selectedWeapon = weapon;
    }

    private void addTileEventHandlers() {
        int tableSize = gameModel.getGridSize() + 1;
        for (int rowIndex = 1; rowIndex < tableSize; rowIndex++) {
            for (int columnIndex = 1; columnIndex < tableSize; columnIndex++) {
                StackPane tile = (StackPane) opponentGridContainer.getTiles()[rowIndex][columnIndex];

                final int currentRowIndex = rowIndex;
                final int currentColumnIndex = columnIndex;
                tile.setOnMouseEntered(event -> {
                    // System.out.printf("Mouse entered cell [%d, %d]%n", currentColumnIndex,
                    // currentRowIndex);
                    if (this.selectedWeapon != null) {
                        previewWeaponShoot(this.selectedWeapon, tile, currentColumnIndex, currentRowIndex);
                    }
                });

                tile.setOnMouseExited(event -> {
                    if (this.selectedWeapon != null) {
                        previewWeaponShoot(this.selectedWeapon, null, currentColumnIndex, currentRowIndex);
                    }
                });

                tile.setOnMouseClicked(event -> {
                    if (this.selectedWeapon != null) {
                        shoot(this.selectedWeapon, currentColumnIndex, currentRowIndex, tile);
                    }
                });
            }
        }
    }

//    @FXML
//    public void shootButtonAction(ActionEvent event) {
//
//    }
//
//    @FXML
//    public void exitGameButtonAction(ActionEvent event) {
//
//    }

}
