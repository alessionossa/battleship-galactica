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


    //NAVIGATION
    @FXML
    public void switchToSceneSettings(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("settings-view.fxml"));
        stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void switchToSceneStartGame(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("setup-ships-view.fxml"));
        stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void switchToMainScene(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("main-view.fxml"));
        stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    //MAIN SCENE CONTROLLER

    //TODO Handling button help

    //SETTINGS SCENE CONTROLLER
    @FXML
    public void activatePlayerMode(ActionEvent event) {

    }

    @FXML
    public void selectGridSize(ActionEvent event) {
         //You can't check different sizes at the same time
    }

    @FXML
    public void activateGravity(ActionEvent event) {
    }

    @FXML
    public void activateAsteroids(ActionEvent event) {
    }
}
