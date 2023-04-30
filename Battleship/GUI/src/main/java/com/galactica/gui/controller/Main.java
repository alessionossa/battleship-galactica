package com.galactica.gui.controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.stage.Stage;

import java.io.IOException;

import com.galactica.model.*;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("main-view.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("Battleship");
        stage.setScene(scene);

        stage.setMinWidth(900);
        stage.setMinHeight(750);

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}