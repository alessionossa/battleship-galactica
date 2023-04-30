package com.galactica.gui.view;

import com.galactica.model.Direction;
import com.galactica.model.Ship;
import javafx.geometry.Pos;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class ShipListCell extends ListCell<Ship> {

    @Override
    protected void updateItem(Ship item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            Image shipImage = ShipImageLoader.loadImageFromShip(item);
            ImageView imageView = new ImageView(shipImage);
            imageView.setPreserveRatio(true);
            imageView.setFitWidth(80);
            imageView.setFitHeight(100);
            if (item.getDirection() == null) {
                item.setDirection(Direction.Vertical);
            }
            switch (item.getDirection()) {
                case Horizontal -> {
                    imageView.setRotate(-90);
                }
                case Vertical -> {
                    imageView.setRotate(0);
                }
            }

            VBox vbox = new VBox(imageView);
            vbox.setAlignment(Pos.CENTER);

            setText(null);
            setGraphic(vbox);
        }
    }
}
