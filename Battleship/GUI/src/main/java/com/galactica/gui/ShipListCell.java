package com.galactica.gui;

import com.galactica.model.Ship;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

class ShipListCell extends ListCell<Ship> {

    @Override
    protected void updateItem(Ship item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            Image shipImage = new Image(getClass().getResource("/assets/cruiser.png").toExternalForm());
            ImageView imageView = new ImageView(shipImage);
            imageView.setPreserveRatio(true);
            imageView.setFitWidth(100);
            imageView.setFitHeight(100);

            Label label = new Label("Cruiser");
            VBox vbox = new VBox(imageView, label);
            vbox.setAlignment(Pos.CENTER);

            setText(null);
            setGraphic(vbox);
        }
    }
}
