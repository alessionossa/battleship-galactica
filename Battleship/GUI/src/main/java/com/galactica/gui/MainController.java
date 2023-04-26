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

        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("settings-view.fxml"));
        stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        final boolean resizable = stage.isResizable();
        stage.setScene(scene);
//        stage.show();

        stage.setResizable(!resizable);
        stage.setResizable(resizable);
    }

    @FXML
    public void switchToSceneStartGame(ActionEvent event) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("setup-ships-view.fxml"));

        
        //PASS CONFIG FOR GRID SIZE, PLAYER MODE, GRAVITY AND ASTEROIDS
        fxmlLoader.setController(new SetupShipController(10, false, false, false));
        Parent root = fxmlLoader.load();
        stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
//        stage.show();
    }

    @FXML
    public void switchToMainScene(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("main-view.fxml"));
        stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    //TODO Handling button help
}
