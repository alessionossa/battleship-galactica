package com.galactica.gui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Window;

import java.util.function.Consumer;

public class SwitchPlayerController {

    private final Consumer<Window> onContinue;


    SwitchPlayerController(Consumer<Window> onContinue) {
        this.onContinue = onContinue;
    }

    @FXML
    void continueButtonAction(ActionEvent event) {
        Node sourceNode = (Node) event.getSource();
        Window currentWindow = sourceNode.getScene().getWindow();
        this.onContinue.accept(currentWindow);
    }
}
