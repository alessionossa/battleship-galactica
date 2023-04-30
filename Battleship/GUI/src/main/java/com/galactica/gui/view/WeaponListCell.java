package com.galactica.gui.view;

import com.galactica.model.Direction;
import com.galactica.model.Ship;
import com.galactica.model.Weapon;
import javafx.geometry.Pos;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class WeaponListCell extends ListCell<Weapon> {

    @Override
    protected void updateItem(Weapon item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            setText(item.toString());
            setGraphic(null);
        }
    }
}