package com.galactica.gui.view;

import com.galactica.model.Direction;
import com.galactica.model.Ship;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.fxml.FXMLLoader;
import javafx.scene.transform.Rotate;

import java.io.IOException;

public class GridContainer extends AnchorPane {

    private int gridSize;

    @FXML
    private BattlefieldGridPane grid;

    @FXML
    private ImageView backgroundImageView;

    public GridContainer() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("grid_container.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
//        setMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
    }

    public void initialize() {

        backgroundImageView.fitWidthProperty().bind(grid.widthProperty());
        backgroundImageView.fitHeightProperty().bind(grid.heightProperty());

        System.out.println("BInding!!!!");
//        NumberBinding gridSize = Bindings.min(this.widthProperty(), this.heightProperty());
//        gridSize.addListener((observable, oldValue, newValue) -> {
//            double newDimension = newValue.doubleValue();
//            grid.setPrefSize(newDimension, newDimension);
//            System.out.println("New dimension to " + newDimension);
//        });

        grid.prefWidthProperty().bind(Bindings.min(this.widthProperty(), this.heightProperty()));
        grid.prefHeightProperty().bind(grid.widthProperty());
    }

    public void setGridSize(int size) {
        this.gridSize = size;
        grid.initializeGrid(size);
    }

    public void updateShipImagePosition(ImageView shipImage, StackPane cell) {
        if (cell != null) {
            Bounds cellBoundsInContainer = grid.localToParent(cell.getBoundsInParent());

            shipImage.toFront();
            shipImage.setX(cellBoundsInContainer.getMinX());
            shipImage.setY(cellBoundsInContainer.getMinY());
            shipImage.setVisible(true);
        } else {
            shipImage.setVisible(false);
        }
    }

    public void updateImageDirection(Ship ship, ImageView shipImageView) {
        if (ship.getDirection() == Direction.Horizontal) {
            shipImageView.fitWidthProperty().bind(this.widthProperty().divide(gridSize + 1));

            // Create a Rotate object
            Rotate rotate = new Rotate();
            rotate.setAngle(90); // Set the rotation angle

            // Bind the pivotX and pivotY properties of the Rotate object relative to the ImageView
            rotate.pivotXProperty().bind(shipImageView.xProperty());
            rotate.pivotYProperty().bind(shipImageView.yProperty());

            // Add the Rotate object to the ImageView's transforms
            shipImageView.getTransforms().add(rotate);

            // Create custom bindings for the translateX and translateY properties to adjust the position of the ImageView after the rotation
            shipImageView.translateXProperty().bind(shipImageView.fitWidthProperty());
        } else {
            shipImageView.fitWidthProperty().bind(this.widthProperty().divide(gridSize + 1));
        }
    }

    public ImageView getShipImageView(Ship ship) {
        ImageView shipImageView;
        Image shipImage = ShipImageLoader.loadImageFromShip(ship);
        shipImageView = new ImageView(shipImage);
        shipImageView.setPreserveRatio(true);

        return shipImageView;
    }

    public Node[][] getTiles() {
        return grid.getTiles();
    }
}