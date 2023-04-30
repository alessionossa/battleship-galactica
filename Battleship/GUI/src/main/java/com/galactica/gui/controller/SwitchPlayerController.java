package com.galactica.gui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;

public class SwitchPlayerController {

    private final Runnable onContinue;

    SwitchPlayerController(Runnable onContinue) {
        this.onContinue = onContinue;
    }

    @FXML
    void continueButtonAction(ActionEvent event) {
        this.onContinue.run();
    }
}
