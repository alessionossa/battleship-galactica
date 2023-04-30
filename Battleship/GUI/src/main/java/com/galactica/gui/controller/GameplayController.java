package com.galactica.gui.controller;

import com.galactica.gui.view.GridContainer;
import com.galactica.gui.view.WeaponListCell;
import com.galactica.model.*;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.fxml.FXML;
import javafx.scene.layout.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.event.*;
import javafx.stage.Stage;
import javafx.stage.Window;

public class GameplayController {

    private Game gameModel;

    @FXML
    private GridContainer currentPlayerGridContainer;

    @FXML
    private GridContainer opponentGridContainer;

    @FXML
    private ListView<Weapon> weaponListView;

    @FXML
    private Button continueButton;

    private Weapon selectedWeapon;

    private boolean canShoot;

    private final HashMap<Ship, ImageView> ownShipsImages = new HashMap<>();

    public GameplayController(Game gameModel) {
        this.gameModel = gameModel;

        for (Ship ship : gameModel.getCurrentPlayer().getShips()) {
            ship.setDirection(Direction.Vertical);
        }

        canShoot = true;
    }

    public void initialize() {
        currentPlayerGridContainer.setGrid(gameModel.getCurrentPlayer().getOwnGrid(), false);
        opponentGridContainer.setGrid(gameModel.getCurrentPlayer().getOpponentGrid(), true);

        setupWeaponList();

        addTileEventHandlers();

        continueButton.setDisable(true);

        Platform.runLater(this::displayShips);
        Platform.runLater(this.currentPlayerGridContainer::updateShots);
        Platform.runLater(this.opponentGridContainer::updateShots);
    }

    private void displayShips() {
        System.out.println("Player turn " + gameModel.getPlayerTurn());
        for (Ship ship: gameModel.getCurrentPlayer().getShips()) {
            currentPlayerGridContainer.showShipImageView(ship);
        }

        for (Ship ship: gameModel.getOpponentPlayer().getShips()) {
            ImageView shipImageView = opponentGridContainer.showShipImageView(ship);

            shipImageView.setVisible(false);
//            shipImageView.setOpacity(0.5);
            shipImageView.setMouseTransparent(true);
            shipImageView.setPickOnBounds(false);
        }
    }

    private void setupWeaponList() {
        weaponListView.setCellFactory(listView -> new WeaponListCell());

        weaponListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Weapon>() {
            @Override
            public void changed(ObservableValue<? extends Weapon> observableValue, Weapon oldValue, Weapon newValue) {
                handleWeaponSelection(newValue);
            }
        });

        updateWeapons();
        weaponListView.getSelectionModel().selectFirst();
    }

    private void updateContinueButton() {
        continueButton.setDisable(canShoot);
    }

    private void previewWeaponShoot(Weapon weapon, StackPane cell) {
        if (weapon == null) {
            cell.getStyleClass().remove("highlighted-tile");
        } else {
            cell.getStyleClass().add("highlighted-tile");
        }
    }

    private void shoot(Weapon weapon, int columnIndex, int rowIndex, StackPane cell) {
        char charIndex = (char) ('a' + (columnIndex - 1));
        Coordinate coordinate = new Coordinate(charIndex, rowIndex - 1);

        if (weapon instanceof Laser) {
            char direction;
            if (columnIndex == 0)
                direction = 'r';
            else
                direction = 'c';
            gameModel.getCurrentPlayer().shootLaser(coordinate, direction, (Laser) weapon);
        } else {
            gameModel.getCurrentPlayer().shoot(coordinate, weapon, gameModel.getGravityMode(), false);
        }

        canShoot = false;
        updateWeapons();
        updateContinueButton();
        opponentGridContainer.updateShots();
        gameModel.save();
    }

    private void handleWeaponSelection(Weapon weapon) {
        this.selectedWeapon = weapon;
        System.out.println("Selected a " + weapon);
    }

    private boolean checkWin() {
        return gameModel.getOpponentPlayer().areAllShipsSunk(gameModel.getOpponentPlayer().getShips());
    }

    private void updateWeapons() {
        weaponListView.getSelectionModel().clearSelection();

        List<Weapon> weapons = gameModel.getCurrentPlayer().getWeapons().stream()
                .filter((weapon -> weapon.getAmountOfUses() > 0))
                .toList();
        weaponListView.getItems().clear();
        weaponListView.getItems().addAll(weapons);
    }

    private boolean isValidCoordinateForShooting(Weapon weapon, int columnIndex, int rowIndex) {
        if (!canShoot) {
            return false;
        } else if (selectedWeapon instanceof Laser &&
                (rowIndex == 0 || columnIndex == 0) &&
                !(rowIndex == 0 && columnIndex == 0)
        ) {
            return true;
        } else if (!(selectedWeapon instanceof Laser) && rowIndex != 0 && columnIndex != 0) {
            return true;
        } else {
            return false;
        }
    }

    private void addTileEventHandlers() {
        int tableSize = gameModel.getGridSize() + 1;
        for (int rowIndex = 0; rowIndex < tableSize; rowIndex++) {
            for (int columnIndex = 0; columnIndex < tableSize; columnIndex++) {
                StackPane tile = (StackPane) opponentGridContainer.getTiles()[rowIndex][columnIndex];

                final int currentRowIndex = rowIndex;
                final int currentColumnIndex = columnIndex;
                tile.setOnMouseEntered(event -> {
                    // System.out.printf("Mouse entered cell [%d, %d]%n", currentColumnIndex,
                    // currentRowIndex);
                    if (isValidCoordinateForShooting(selectedWeapon,currentColumnIndex, currentRowIndex)) {
                        previewWeaponShoot(this.selectedWeapon, tile);
                    }
                });

                tile.setOnMouseExited(event -> {
                    previewWeaponShoot(null, tile);
                });

                tile.setOnMouseClicked(event -> {
                    if (isValidCoordinateForShooting(selectedWeapon,currentColumnIndex, currentRowIndex)) {
                        shoot(this.selectedWeapon, currentColumnIndex, currentRowIndex, tile);
                    }
                });
            }
        }
    }

    @FXML
    public void continueButtonAction(ActionEvent event) throws IOException {
        Scene currentScene = ((Node) event.getSource()).getScene();

        if (checkWin()) {
            switchToWinView(currentScene, gameModel.getCurrentPlayer().getName() + " LASSO'D THE GALAXY AND SAVED THE CAT");
        } else if (gameModel.getSinglePlayerMode()) {
            gameModel.nextPlayerTurn();
            gameModel.getCurrentPlayer().shoot(null, null, gameModel.getGravityMode(), false);


            canShoot = true;
            updateWeapons();
            updateContinueButton();
            currentPlayerGridContainer.updateShots();

            if (checkWin())
                switchToWinView(currentScene, gameModel.getCurrentPlayer().getName() + " LASSO'D THE GALAXY AND SAVED THE CAT");
            else
                gameModel.nextPlayerTurn();
        } else {
            gameModel.nextPlayerTurn();
            switchToNextGamePlayScene(currentScene);
        }
    }

    private void switchToNextGamePlayScene(Scene currentScene) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("switch-player-view.fxml"));

        Consumer<Window> switchToNextGameTurnBlock = (Window window) -> {
            FXMLLoader fxmlLoaderNextShot = new FXMLLoader(getClass().getClassLoader().getResource("game-view.fxml"));

            fxmlLoaderNextShot.setController(new GameplayController(this.gameModel));

            Parent root;
            try {
                root = fxmlLoaderNextShot.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Stage stage = (Stage) window;
            Scene scene = new Scene(root);
            stage.setScene(scene);
        };
        fxmlLoader.setController(new SwitchPlayerController(switchToNextGameTurnBlock));

        Parent root = fxmlLoader.load();
        Stage stage = (Stage) currentScene.getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    private void switchToWinView(Scene currentScene, String message) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("win-view.fxml"));

        fxmlLoader.setController(new WinController(message));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) currentScene.getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

}
