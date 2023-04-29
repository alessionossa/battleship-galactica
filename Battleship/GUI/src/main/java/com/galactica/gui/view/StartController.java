package com.galactica.gui.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class StartController {

    @FXML
    public void switchToSceneSettings(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("settings-view.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        final boolean resizable = stage.isResizable();
        stage.setScene(scene);

        stage.setResizable(!resizable);
        stage.setResizable(resizable);
    }

    @FXML
    public void uploadSavedGame(ActionEvent event) throws IOException {

    }

}
