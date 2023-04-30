package com.galactica.gui.controller;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.File;

import javafx.fxml.FXML;

public class WinController {

    @FXML
    private Label messageLabel;

    private String message;

    WinController(String message) {
        this.message = message;
    }

    public void initialize() {

        this.messageLabel.setText(message);
    }

}
