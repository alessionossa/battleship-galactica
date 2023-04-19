package com.galactica.gui;

import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.scene.*;
import javafx.stage.Stage;
import javafx.event.*;

import java.io.IOException;

public class MainController {

    private Stage stage;
    private Scene scene;

    private int gridSize;
    private boolean gravity;
    private boolean asteroids;

    @FXML
    public void switchToSceneSettingsMultiplayer(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("settings-view.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        //TODO: Send info to model
    }

    @FXML
    public void switchToSceneSettingsSingleplayer(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("settings-view.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        //TODO: Send info to model
    }

    @FXML
    public void switchToSceneStartGame(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("startGame-view.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        //TODO: Send info to model
    }

    @FXML
    public void switchToMainScene(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("main-view.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void activateGravity(ActionEvent event) {
    }

    @FXML
    public void activateAsteroids(ActionEvent event) {
    }
}
