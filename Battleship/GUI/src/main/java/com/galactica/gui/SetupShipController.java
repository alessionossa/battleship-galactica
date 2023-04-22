package com.galactica.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.geometry.Pos;

public class SetupShipController {

    @FXML
    private BorderPane borderPane;

    @FXML
    private GridPane grid;

    public void initialize() {
        int gridSize = 10; // Set the desired grid size here
        // String backgroundImage = ""; // Set the path to your background image

        // Load the background image
        Image image = new Image("assets/background.png");
        ImageView imageView = new ImageView(image);
        borderPane.setCenter(imageView);
        imageView.fitWidthProperty().bind(borderPane.widthProperty());
        imageView.fitHeightProperty().bind(borderPane.heightProperty());
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setCache(true);

        // Create the grid
        for (int i = 0; i < gridSize; i++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setPercentWidth(100.0 / gridSize);
            grid.getColumnConstraints().add(column);

            RowConstraints row = new RowConstraints();
            row.setPercentHeight(100.0 / gridSize);
            grid.getRowConstraints().add(row);

            for (int j = 0; j < gridSize; j++) {
                StackPane tile = new StackPane();
                tile.setStyle("-fx-border-color: black;");
                grid.add(tile, i, j);

                if (i == 0 || j == 0) {
                    String coordinate = (i == 0 && j > 0) ? String.valueOf(j) : (j == 0 && i > 0) ? String.valueOf((char) ('A' + i - 1)) : "";
                    Label label = new Label(coordinate);
                    tile.setAlignment(Pos.CENTER);
                    tile.getChildren().add(label);
                }
            }
        }

        grid.toFront();
    }
}
