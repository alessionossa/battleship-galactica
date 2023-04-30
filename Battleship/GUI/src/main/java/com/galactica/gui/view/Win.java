package com.galactica.gui.view;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.File;

import javafx.fxml.FXML;

public class Win {
    @FXML
    private ImageView imageview;

    public void initialize() {
        Image i = new Image(new File("/assets/Cat.gif").toURI().toString());
        imageview.setImage(i);
    }

}
