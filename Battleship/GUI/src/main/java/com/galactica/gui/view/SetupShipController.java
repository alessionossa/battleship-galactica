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
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.image.ImageView;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
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

            if (ship.getDirection() == Direction.Horizontal) {
                shipImageView.fitWidthProperty().bind(gridContainer.widthProperty().divide(gridSize + 1));
                shipImageView.setRotate(90);
            } else {
                shipImageView.fitWidthProperty().bind(gridContainer.widthProperty().divide(gridSize + 1));
            }

            this.shipImages.put(ship, shipImageView);
            updatePosition(shipImageView, cell);

            gridContainer.getChildren().add(shipImageView);
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

        shipsToPlace.remove(ship);
        shipsListView.refresh();
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
                System.out.println(childrenIndex);
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
