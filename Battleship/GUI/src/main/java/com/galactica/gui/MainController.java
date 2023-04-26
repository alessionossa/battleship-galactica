package com.galactica.gui;

import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.scene.*;
import javafx.stage.Stage;
import javafx.event.*;
import javafx.scene.control.RadioButton;
import javafx.scene.control.CheckBox;

import java.io.IOException;

public class MainController {

    private Stage stage;
    private Scene scene;

    private int gridSize;
    private boolean singlePlayer;
    private boolean gravity;
    private boolean asteroids;

    // NAVIGATION
    @FXML
    public void switchToSceneSettings(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("settings-view.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        final boolean resizable = stage.isResizable();
        stage.setScene(scene);
        // stage.show();

        stage.setResizable(!resizable);
        stage.setResizable(resizable);
    }

    @FXML
    private void onRadioButtonSmallSelected(ActionEvent event) {
        RadioButton radioButton = (RadioButton) event.getSource();
        if (radioButton.isSelected()) {
            this.gridSize = 10;
            System.out.println("Small grid selected");
        }
    }

    @FXML
    private void onRadioButtonMediumSelected(ActionEvent event) {
        RadioButton radioButton = (RadioButton) event.getSource();
        if (radioButton.isSelected()) {
            this.gridSize = 15;
            System.out.println("Medium grid selected");
        }
    }

    @FXML
    private void onRadioButtonLargeSelected(ActionEvent event) {
        RadioButton radioButton = (RadioButton) event.getSource();
        if (radioButton.isSelected()) {
            this.gridSize = 20;
            System.out.println("Large grid selected");
        }
    }

    @FXML
    private void onRadioButtonMultiplayerSelected(ActionEvent event) {
        RadioButton radioButton = (RadioButton) event.getSource();
        if (radioButton.isSelected()) {
            this.singlePlayer = false;
            System.out.println("Multiplayer selected");
        }
    }

    @FXML
    private void onRadioButtonSingleplayerSelected(ActionEvent event) {
        RadioButton radioButton = (RadioButton) event.getSource();
        if (radioButton.isSelected()) {
            this.singlePlayer = true;
            System.out.println("Singleplayer selected");
        }
    }

    @FXML
    private void onCheckBoxAsteroidsSelected(ActionEvent event) {
        CheckBox checkBox = (CheckBox) event.getSource();
        if (checkBox.isSelected()) {
            this.asteroids = true;
            System.out.println("Asteroids selected");
        } else {
            this.asteroids = false;
            System.out.println("Asteroids not selected");
        }
    }

    @FXML
    private void onCheckBoxGravitySelected(ActionEvent event) {
        CheckBox checkBox = (CheckBox) event.getSource();
        if (checkBox.isSelected()) {
            this.gravity = true;
            System.out.println("Gravity selected");
        } else {
            this.gravity = false;
            System.out.println("Gravity not selected");
        }
    }

    @FXML
    public void switchToSceneStartGame(ActionEvent event) throws IOException {
        System.out.println("Size: " + this.gridSize + ", PlayerMode: " + this.singlePlayer + ", gravity: "
                + this.gravity + ", asteroids: " + this.asteroids);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("setup-ships-view.fxml"));

        // PASS CONFIG FOR GRID SIZE, PLAYER MODE, GRAVITY AND ASTEROIDS
        fxmlLoader
                .setController(new SetupShipController(this.gridSize, this.singlePlayer, this.asteroids, this.gravity));
        Parent root = fxmlLoader.load();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        // stage.show();
    }

    @FXML
    public void switchToMainScene(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("main-view.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    // TODO Handling button help
}
