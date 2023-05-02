package com.galactica.gui.view;

import com.galactica.model.*;
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
import java.util.HashMap;

public class GridContainer extends AnchorPane {

    private int gridSize;

    private Grid grid;

    private boolean isOpponentGrid;

    @FXML
    private BattlefieldGridPane gridPane;

    @FXML
    private ImageView backgroundImageView;

    private final HashMap<Integer, ImageView> planetImages = new HashMap<>();

    private final HashMap<Tile, ImageView> holesImages = new HashMap<>();

    private final HashMap<Integer, ImageView> shipImages = new HashMap<>();

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
        Platform.runLater(this::placePlanets);
    }

    public void setGrid(Grid grid, boolean isOpponentGrid) {
        this.grid = grid;
        this.gridSize = grid.getGridSize(); // TODO: Remove this
        this.isOpponentGrid = isOpponentGrid;
        gridPane.initializeGrid(grid.getGridSize());
    }

    private void placeAsteroids() {
        if (!isOpponentGrid) {
            for (Asteroid asteroid : grid.getAsteroids()) {
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
    }

    private void placePlanets() {
        for (Planet planet : grid.getPlanets()) {
            Image planetImage = new Image(getClass().getResource("/assets/Planet2x2.png").toExternalForm());
            if (planet.getSize() == 3) {
                planetImage = new Image(getClass().getResource("/assets/Planet3x3.png").toExternalForm());
            } else if (planet.getSize() == 4) {
                planetImage = new Image(getClass().getResource("/assets/Planet4x4.png").toExternalForm());
            }

            ImageView planetImageView = new ImageView(planetImage);
            planetImageView.setPreserveRatio(true);

            planetImageView.setPickOnBounds(false);
            planetImageView.setMouseTransparent(true);

            int xCoordinate = grid.convertXToMatrixIndex(planet.getCoordinate().getX()) + 1;
            int yCoordinate = planet.getCoordinate().getY() + 1;
            System.out.println("Placing planet at x" + xCoordinate + "; y" + yCoordinate);
            StackPane tile = (StackPane) getTiles()[yCoordinate][xCoordinate];
            Bounds cellBoundsInContainer = gridPane.localToParent(tile.getBoundsInParent());
            planetImageView.setX(cellBoundsInContainer.getMinX());
            planetImageView.setY(cellBoundsInContainer.getMinY());

            planetImageView.fitWidthProperty()
                    .bind(Bindings.multiply(this.widthProperty().divide(gridSize + 1), planet.getSize() + 0.2));

            if (isOpponentGrid) {
                planetImageView.setVisible(false);
            }

            planetImages.put(planet.getIdentifier(), planetImageView);

            this.getChildren().add(planetImageView);

            planetImageView.toFront();
        }
    }

    public void updateShots() {
        // int tableSize = grid.getGridSize() + 1;
        for (int rowIndex = 0; rowIndex < grid.getGridSize(); rowIndex++) {
            for (int columnIndex = 0; columnIndex < grid.getGridSize(); columnIndex++) {
                StackPane tileView = (StackPane) getTiles()[rowIndex + 1][columnIndex + 1];

                Tile tile = grid.getTiles()[rowIndex][columnIndex];
                if (tile.isHit()) {
                    System.out.println("Hitted" + columnIndex + ", "+ rowIndex);
                    tileView.getStyleClass().remove("untouched-tile");
                    if (tile.getPlanet() != null) {
                        planetImages.get(tile.getPlanet().getIdentifier()).setVisible(true);
                    } else if (tile.getAsteroid() != null) {
                        setHoleImageViewForTile(tile, tileView);
                    } else if (tile.getShip() != null){
                        if (tile.getShip().isSunk()) {
                            ImageView shipImageView = this.shipImages.get(tile.getShip().getIdentifier());
                            if (shipImageView == null) {
                                System.out.println("Missing image for " + tile.getShip() + " at " + columnIndex + ", "+ rowIndex);
                            } else {
                                System.out.println("Image present for " + tile.getShip() + " at " + columnIndex + ", "+ rowIndex);
                                shipImageView.setVisible(true);
                            }
//                            shipImageView.setOpacity(1.0);
//                            shipImageView.toFront();
                        }
                        setHoleImageViewForTile(tile, tileView);
                    }
                } else {
                    if (!tileView.getStyleClass().contains("untouched-tile")) {
                        tileView.getStyleClass().add("untouched-tile");
                    }
                }
            }
        }
    }

    private void setHoleImageViewForTile(Tile tile, StackPane tileView) {
        ImageView holeImageView = holesImages.get(tile);

        if (holeImageView == null) {
            System.out.println("Creating a new hole image");
            Image holeImage = new Image(getClass().getResource("/assets/hole.png").toExternalForm());

            holeImageView = new ImageView(holeImage);
            holeImageView.setPreserveRatio(true);
            this.holesImages.put(tile, holeImageView);

            holeImageView.setPickOnBounds(false);
            holeImageView.setMouseTransparent(true);
            holeImageView.setOpacity(0.6);

            Bounds cellBoundsInContainer = gridPane.localToParent(tileView.getBoundsInParent());
            holeImageView.setX(cellBoundsInContainer.getMinX());
            holeImageView.setY(cellBoundsInContainer.getMinY());

            holeImageView.fitWidthProperty()
                    .bind(this.widthProperty().divide(gridSize + 1));
            this.getChildren().add(holeImageView);
            holeImageView.toFront();
        }
    }

    public ImageView showShipImageView(Ship ship) {
        ImageView shipImageView = shipImages.get(ship);

        if (shipImageView == null) {
            shipImageView = getShipImageView(ship);
            this.shipImages.put(ship.getIdentifier(), shipImageView);

            Coordinate shipOriginCoordinate = ship.getCoordinate();
            int xIndex = grid.convertXToMatrixIndex(shipOriginCoordinate.getX()) + 1;
            int yIndex = shipOriginCoordinate.getY() + 1;
            System.out.println("Ship at coordinate " + xIndex + "," + yIndex);
            StackPane tile = (StackPane) getTiles()[yIndex][xIndex];
            updateShipImagePosition(shipImageView, tile);
            updateImageDirection(ship, shipImageView);
            getChildren().add(shipImageView);
        }

        return shipImageView;
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

            // Bind the pivotX and pivotY properties of the Rotate object relative to the
            // ImageView
            rotate.pivotXProperty().bind(Bindings.add(shipImageView.xProperty(), shipImageView.fitWidthProperty()));
            rotate.pivotYProperty().bind(shipImageView.yProperty());

            // Add the Rotate object to the ImageView's transforms
            shipImageView.getTransforms().add(rotate);

            // Create custom bindings for the translateX and translateY properties to adjust
            // the position of the ImageView after the rotation
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