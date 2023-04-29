package com.galactica.gui;

import com.galactica.model.Ship;
import com.galactica.model.ships.*;
import javafx.scene.image.Image;

public class ShipImageLoader {
    public Image loadImageFromShip(Ship ship) {
        String path;
        if (ship instanceof DeathStar) {
            path = getClass().getResource("/assets/death.png").toExternalForm();
            return new Image("path/to/battleship/image.png");
        } else if (ship instanceof Cruiser) {
            path = getClass().getResource("/assets/cruiser.png").toExternalForm();
            return new Image("path/to/cruiser/image.png");
        } else if (ship instanceof Scout) {
            path = getClass().getResource("/assets/cruiser.png").toExternalForm();
            return new Image("path/to/cruiser/image.png");
        } else {
            throw new IllegalArgumentException("Unknown ship type: " + ship.getClass());
        }
    }
}
