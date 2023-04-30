package com.galactica.gui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.galactica.model.*;

import java.io.IOException;

public class StartController {

    @FXML
    public void switchToSceneSettings(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("settings-view.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    @FXML
    public void uploadSavedGame(ActionEvent event) throws IOException {
        Game gameModel = new Game();
        gameModel = gameModel.load(Game.getDefaultPath());

        if (gameModel != null) {
            // SET UP GAME PLAY CONTROLLER
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("game-view.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
        }
    }

}
