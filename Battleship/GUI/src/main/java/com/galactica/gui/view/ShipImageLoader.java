package com.galactica.gui.view;

import com.galactica.model.Ship;
import com.galactica.model.ships.*;
import javafx.scene.image.Image;

public class ShipImageLoader {
    public static Image loadImageFromShip(Ship ship) {
        String path;
        if (ship instanceof DeathStar) {
            path = ShipImageLoader.class.getResource("/assets/deathstar.png").toExternalForm();
        } else if (ship instanceof Cruiser) {
            path = ShipImageLoader.class.getResource("/assets/cruiser.png").toExternalForm();
        } else if (ship instanceof Scout) {
            path = ShipImageLoader.class.getResource("/assets/scout.png").toExternalForm();
        } else {
            throw new IllegalArgumentException("Unknown ship type: " + ship.getClass());
        }

        return new Image(path);
    }
}
