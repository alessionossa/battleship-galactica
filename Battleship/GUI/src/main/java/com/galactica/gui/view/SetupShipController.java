package com.galactica.gui.view;

import com.galactica.model.Direction;
import com.galactica.model.Ship;
import com.galactica.model.ships.*;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.image.ImageView;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.event.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class SetupShipController {

    private Stage stage;
    private Scene scene;

    private int gridSize;
    private boolean singlePlayer;
    private boolean asteroids;
    private boolean gravity;

    ObservableList<Ship> shipsToPlace;

    Set<Ship> placedShips = new HashSet<>();

    @FXML
    private BorderPane borderPane;

    @FXML
    private BattlefieldGridPane grid;

    @FXML
    private AnchorPane gridContainer;

    @FXML
    private ImageView backgroundImageView;

    @FXML
    private ListView<Ship> shipsListView;

    @FXML
    private Button rotateButton;

    @FXML
    private Button startGameButton;

    private Ship selectedShip;

    private final HashMap<Ship, ImageView> shipImages = new HashMap<>();

    public SetupShipController(int gridSize, boolean singlePlayer, boolean asteroids, boolean gravity) {
        this.gridSize = gridSize;
        this.singlePlayer = singlePlayer;
        this.asteroids = asteroids;
        this.gravity = gravity;

        Ship[] ships = new Ship[] { new Cruiser(1), new DeathStar(2), new Scout(3) };

        this.shipsToPlace = FXCollections.observableArrayList(ships);
    }

    public void initialize() {// Set the desired grid size here
        // TODO: Set up the player mode, asteroids and gravity if needed here

        backgroundImageView.fitWidthProperty().bind(grid.widthProperty());
        backgroundImageView.fitHeightProperty().bind(grid.heightProperty());

//        grid.getStyleClass().add("grid");

        grid.initializeGrid(gridSize);

        // Set GridPane resize properties

        gridContainer.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        gridContainer.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        NumberBinding gridSize = Bindings.min(gridContainer.widthProperty(), gridContainer.heightProperty());
        gridSize.addListener((observable, oldValue, newValue) -> {
            double newDimension = newValue.doubleValue();
            grid.setPrefSize(newDimension, newDimension);
            System.out.println("New dimension to" + newDimension);
        });

        setupShipList();

        setupRotateButton();

        startGameButton.setDisable(false);

        addTileEventHandlers();
    }

    private void setupShipList() {
        shipsListView.getItems().addAll(shipsToPlace);
        shipsListView.setCellFactory(listView -> new ShipListCell());

        shipsListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Ship>() {
            @Override
            public void changed(ObservableValue<? extends Ship> observableValue, Ship ship, Ship t1) {
                selectedShip = t1;
                rotateButton.setDisable(false);
            }
        });

        shipsListView.getSelectionModel().selectFirst();
    }

    private void setupRotateButton() {
        rotateButton.setOnAction(event -> {
            Direction direction = selectedShip.getDirection();
            if (direction == Direction.Horizontal) {
                direction = Direction.Vertical;
            } else {
                direction = Direction.Horizontal;
            }
            selectedShip.setDirection(direction);
            shipsListView.refresh();
        });
    }

    private void previewShipPlacement(Ship ship, StackPane cell) {
        ImageView shipImageView = this.shipImages.get(ship);

        if (shipImageView == null) {
            Image shipImage = ShipImageLoader.loadImageFromShip(ship);
            shipImageView = new ImageView(shipImage);
            shipImageView.setPreserveRatio(true);
            shipImageView.setPickOnBounds(false);
            shipImageView.setMouseTransparent(true);

            // Set the pivot points for rotation
//            shipImageView.setPivotX(0);
//            imageView.setPivotY(0);

            this.shipImages.put(ship, shipImageView);
            updatePosition(shipImageView, cell);

            gridContainer.getChildren().add(shipImageView);

            if (ship.getDirection() == Direction.Horizontal) {
                shipImageView.fitWidthProperty().bind(gridContainer.widthProperty().divide(gridSize + 1));
                // Create a Rotate transformation with a pivot point
                // The pivot point should be the same as the position of the ImageView
                // Rotate rotate = new Rotate(90, shipImageView.getX(), shipImageView.getY(), 0, new Point3D(0, 0, 1));
//                shipImageView.setTranslateX(shipImageView.getBoundsInLocal().getWidth() / 2);
//                shipImageView.setTranslateY(shipImageView.getBoundsInLocal().getHeight() / 2);
//                shipImageView.setRotate(90);
//                shipImageView.translateXProperty().bind(shipImageView.fitHeightProperty());
                Rotate rotate = new Rotate();
                rotate.setAngle(90); // Set the rotation angle

                // Bind the pivotX and pivotY properties of the Rotate object relative to the ImageView
                rotate.pivotXProperty().bind(shipImageView.xProperty());
                rotate.pivotYProperty().bind(shipImageView.yProperty());

                // Add the Rotate object to the ImageView's transforms
                shipImageView.getTransforms().add(rotate);

                // Create custom bindings for the translateX and translateY properties to adjust the position of the ImageView after the rotation
                shipImageView.translateXProperty().bind(shipImageView.fitWidthProperty());
//                shipImageView.translateYProperty().bind(shipImageView.fitWidthProperty());

            } else {
                shipImageView.fitWidthProperty().bind(gridContainer.widthProperty().divide(gridSize + 1));
            }
        } else {
            updatePosition(shipImageView, cell);
        }
    }

    private void placeShip(Ship ship, int columnIndex, int rowIndex, StackPane cell) {
        ImageView shipImageView = this.shipImages.get(ship);

        shipImageView.setMouseTransparent(false);
        shipImageView.setPickOnBounds(true);
        updatePosition(shipImageView, cell);

        placedShips.add(ship);

        boolean result1 = shipsListView.getItems().remove(ship);
        boolean result2 = shipsToPlace.remove(ship);
    }

    private void updatePosition(ImageView shipImage, StackPane cell) {
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

    private void addTileEventHandlers() {
        int tableSize = gridSize + 1;
        for (int rowIndex = 1; rowIndex < tableSize; rowIndex++) {
            for (int columnIndex = 1; columnIndex < tableSize; columnIndex++) {
                int childrenIndex = rowIndex * tableSize + columnIndex;
                StackPane tile = (StackPane) grid.getTiles()[rowIndex][columnIndex];

                final int currentRowIndex = rowIndex;
                final int currentColumnIndex = columnIndex;
                tile.setOnMouseEntered(event -> {
                    // System.out.printf("Mouse entered cell [%d, %d]%n", currentColumnIndex, currentRowIndex);
                    if (this.selectedShip != null) {
                        previewShipPlacement(this.selectedShip, tile);
                    }
                });

                tile.setOnMouseExited(event -> {
                    previewShipPlacement(this.selectedShip, null);
                });

                tile.setOnMouseClicked(event -> {
                    placeShip(this.selectedShip, currentColumnIndex, currentRowIndex, tile);
                });
            }
        }
    }

    @FXML
    public void switchToSceneGame(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("game-view.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        final boolean resizable = stage.isResizable();
        stage.setScene(scene);

        stage.setResizable(!resizable);
        stage.setResizable(resizable);
    }

    @FXML
    public void switchToSettingsScene(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("settings-view.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
