package com.galactica.gui.view;

import com.galactica.model.Asteroid;
import com.galactica.model.Direction;
import com.galactica.model.Grid;
import com.galactica.model.Ship;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
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

    private Grid grid;

    @FXML
    private BattlefieldGridPane gridPane;

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
    }

    public void initialize() {
        backgroundImageView.fitWidthProperty().bind(gridPane.widthProperty());
        backgroundImageView.fitHeightProperty().bind(gridPane.heightProperty());

        gridPane.prefWidthProperty().bind(Bindings.min(this.widthProperty(), this.heightProperty()));
        gridPane.prefHeightProperty().bind(gridPane.widthProperty());

        Platform.runLater(this::placeAsteroids);
    }

    public void setGrid(Grid grid) {
        this.grid = grid;
        this.gridSize = grid.getGridSize(); // TODO: Remove this
        gridPane.initializeGrid(grid.getGridSize());
    }

    private void placeAsteroids() {
        for (Asteroid asteroid: grid.getAsteroids()) {
            Image asteroidImage = new Image(getClass().getResource("/assets/asteroid.png").toExternalForm());
            ImageView asteroidImageView = new ImageView(asteroidImage);
            asteroidImageView.setPreserveRatio(true);

            asteroidImageView.setPickOnBounds(false);
            asteroidImageView.setMouseTransparent(true);

            int xCoordinate = grid.convertXToMatrixIndex(asteroid.getCoordinate().getX()) + 1;
            int yCoordinate = asteroid.getCoordinate().getY() + 1;
            StackPane tile = (StackPane) getTiles()[yCoordinate][xCoordinate];
            Bounds cellBoundsInContainer = gridPane.localToParent(tile.getBoundsInParent());
            asteroidImageView.setX(cellBoundsInContainer.getMinX());
            asteroidImageView.setY(cellBoundsInContainer.getMinY());

            asteroidImageView.fitWidthProperty().bind(this.widthProperty().divide(gridSize + 1));

            this.getChildren().add(asteroidImageView);

            asteroidImageView.toFront();
        }
    }

    public void updateShipImagePosition(ImageView shipImage, StackPane cell) {
        if (cell != null) {
            Bounds cellBoundsInContainer = gridPane.localToParent(cell.getBoundsInParent());

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

            shipImageView.getTransforms().clear();

            // Create a Rotate object
            Rotate rotate = new Rotate();
            rotate.setAngle(-90); // Set the rotation angle

            // Bind the pivotX and pivotY properties of the Rotate object relative to the ImageView
            rotate.pivotXProperty().bind(Bindings.add(shipImageView.xProperty(), shipImageView.fitWidthProperty()));
            rotate.pivotYProperty().bind(shipImageView.yProperty());

            // Add the Rotate object to the ImageView's transforms
            shipImageView.getTransforms().add(rotate);

            // Create custom bindings for the translateX and translateY properties to adjust the position of the ImageView after the rotation
            shipImageView.translateXProperty().bind(Bindings.negate(shipImageView.fitWidthProperty()));
        } else {
            shipImageView.getTransforms().clear();
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
        return gridPane.getTiles();
    }
}