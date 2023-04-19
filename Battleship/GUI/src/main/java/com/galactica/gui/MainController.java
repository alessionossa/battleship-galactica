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
    private Parent root;

    @FXML
    public void switchToSceneChooseGridMultiplayer(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("chooseGrid-view.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        //TODO: Send info to model
    }

    @FXML
    public void switchToSceneChooseGridSingleplayer(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("chooseGrid-view.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        //TODO: Send info to model
    }
}
