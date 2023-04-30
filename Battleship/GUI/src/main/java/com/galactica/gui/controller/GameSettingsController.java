package com.galactica.gui.controller;

import com.galactica.model.*;
import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.event.*;

import java.io.IOException;

public class GameSettingsController {

    @FXML
    ToggleGroup gridSizeGroup;

    @FXML
    RadioButton smallSizeGridRadioButton;

    @FXML
    RadioButton mediumSizeGridRadioButton;

    @FXML
    RadioButton largeSizeGridRadioButton;

    @FXML
    ToggleGroup playerModeGroup;

    @FXML
    RadioButton multiPlayerRadioButton;

    @FXML
    RadioButton singlePlayerRadioButton;

    @FXML
    Button startGameButton;

    private final Game gameModel;

    public GameSettingsController() {
        gameModel = new Game();
    }

    public void initialize() {
        // Gride size selection
        gridSizeGroup.selectedToggleProperty().addListener((observable, oldVal, newVal) -> {
            handleGridSizeRadioButtonSelection((RadioButton) newVal);
        });

        // Execute the code for the initial value
        RadioButton initiallySelectedGrideSizeRadioButton = (RadioButton) gridSizeGroup.getSelectedToggle();
        handleGridSizeRadioButtonSelection(initiallySelectedGrideSizeRadioButton);

        // Player mode selection
        playerModeGroup.selectedToggleProperty().addListener((observable, oldVal, newVal) -> {
            handlePlayerModeRadioButtonSelection((RadioButton) newVal);
        });

        // Execute the code for the initial value
        RadioButton initiallySelectedPlayerModeRadioButton = (RadioButton) playerModeGroup.getSelectedToggle();
        handlePlayerModeRadioButtonSelection(initiallySelectedPlayerModeRadioButton);
    }

    private void handleGridSizeRadioButtonSelection(RadioButton selectedRadioButton) {
        // Your code to handle the selected RadioButton based on its ID or any other property
        if (selectedRadioButton == smallSizeGridRadioButton) {
            gameModel.setGridSize(10);
            System.out.println("Small grid selected");
        } else if (selectedRadioButton == mediumSizeGridRadioButton) {
            gameModel.setGridSize(15);
            System.out.println("Medium grid selected");
        } else if (selectedRadioButton == largeSizeGridRadioButton) {
            gameModel.setGridSize(20);
            System.out.println("Large grid selected");
        }
    }

    private void handlePlayerModeRadioButtonSelection(RadioButton selectedRadioButton) {
        // Your code to handle the selected RadioButton based on its ID or any other property
        if (selectedRadioButton == singlePlayerRadioButton) {
            gameModel.setSinglePlayerMode(true);
            System.out.println("Singleplayer selected");
        } else if (selectedRadioButton == multiPlayerRadioButton) {
            gameModel.setSinglePlayerMode(false);
            System.out.println("Multiplayer selected");
        }
    }

    @FXML
    private void onCheckBoxAsteroidsSelected(ActionEvent event) {
        CheckBox checkBox = (CheckBox) event.getSource();
        if (checkBox.isSelected()) {
            gameModel.setAsteroidMode(true);
            System.out.println("Asteroids selected");
        } else {
            gameModel.setAsteroidMode(false);
            System.out.println("Asteroids not selected");
        }
    }

    @FXML
    private void onCheckBoxGravitySelected(ActionEvent event) {
        CheckBox checkBox = (CheckBox) event.getSource();
        if (checkBox.isSelected()) {
            gameModel.setGravityMode(true);
            System.out.println("Gravity selected");
        } else {
            gameModel.setGravityMode(false);
            System.out.println("Gravity not selected");
        }
    }

    private void setupGameModel() {
        gameModel.setPlayerTurn(1);
        gameModel.setGrid1(Game.setUpGrid(gameModel.getGridSize(), gameModel.getSinglePlayerMode(), gameModel.getAsteroidMode(), gameModel.getGravityMode()));
        gameModel.setGrid2(Game.setUpGrid(gameModel.getGridSize(), gameModel.getSinglePlayerMode(), gameModel.getAsteroidMode(), gameModel.getGravityMode()));

        gameModel.setP1(new Human("Space Cowboy", gameModel.getGrid1(), gameModel.getGrid2()));

        if (gameModel.getSinglePlayerMode()) {
            gameModel.setP2(new AI("Megatron", gameModel.getGrid2(), gameModel.getGrid1()));
        } else {
            gameModel.setP2(new Human("Rocket Rancher", gameModel.getGrid2(), gameModel.getGrid1()));
        }
    }

    // Navigation

    @FXML
    public void switchToSetupShipsScene(ActionEvent event) throws IOException {
        setupGameModel();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("setup-ships-view.fxml"));

        // PASS CONFIG FOR GRID SIZE, PLAYER MODE, GRAVITY AND ASTEROIDS
        fxmlLoader.setController(new SetupShipController(this.gameModel));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    @FXML
    public void switchToMainScene(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("main-view.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
