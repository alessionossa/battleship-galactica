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
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.image.ImageView;
import javafx.geometry.Pos;
import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.scene.*;
import javafx.stage.Stage;
import javafx.event.*;

import java.io.IOException;
import java.util.HashMap;

public class SetupShipController {

    private Stage stage;
    private Scene scene;

    private int gridSize;
    private boolean singlePlayer;
    private boolean asteroids;
    private boolean gravity;

    ObservableList<Ship> ships;

    @FXML
    private BorderPane borderPane;

    @FXML
    private GridPane grid;

    @FXML
    private StackPane gridContainer;

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

        this.ships = FXCollections.observableArrayList(ships);
    }

    public void initialize() {// Set the desired grid size here
        // TODO: Set up the player mode, asteroids and gravity if needed here

        backgroundImageView.fitWidthProperty().bind(grid.widthProperty());
        backgroundImageView.fitHeightProperty().bind(grid.heightProperty());

        grid.getStyleClass().add("grid");

        createGrid();

        // Bind the column and row constraints to maintain square tiles
        NumberBinding tileSize = Bindings.min(borderPane.widthProperty().divide(gridSize + 2),
                borderPane.heightProperty().divide(gridSize + 2));
        tileSize.addListener((obs, oldSize, newSize) -> {
            for (ColumnConstraints column : grid.getColumnConstraints()) {
                column.setPrefWidth(newSize.doubleValue());
                column.setMaxWidth(newSize.doubleValue());
            }
            for (RowConstraints row : grid.getRowConstraints()) {
                row.setPrefHeight(newSize.doubleValue());
                row.setMaxHeight(newSize.doubleValue());
            }
        });

        setupShipList();

        setupButton();

        startGameButton.setDisable(false);
    }

    private void setupShipList() {
        shipsListView.getItems().addAll(ships);
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

    private void setupButton() {
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

    private void previewShipPlacement(Ship ship, int columnIndex, int rowIndex, StackPane cell) {
        ImageView shipImageView = this.shipImages.get(ship);

        if (shipImageView == null) {
            Image shipImage = ShipImageLoader.loadImageFromShip(ship);
            shipImageView = new ImageView(shipImage);
            shipImageView.setPreserveRatio(true);

            final ImageView newShipImageView = shipImageView;
            newShipImageView.fitWidthProperty().bind(borderPane.widthProperty().divide(gridSize + 2));
            this.shipImages.put(ship, shipImageView);

            updatePosition(shipImageView, cell);

            gridContainer.getChildren().add(shipImageView);
        } else {
            updatePosition(shipImageView, cell);
        }


    }

    private void updatePosition(ImageView shipImage, StackPane cell) {
        Bounds cellBoundsInContainer = grid.localToParent(cell.getBoundsInParent());
//        Point2D cellCoordinates = cellBoundsInContainer.

        System.out.println("Moving to" + cellBoundsInContainer);
        shipImage.toFront();
        shipImage.setX(cellBoundsInContainer.getMinX());
        shipImage.setY(cellBoundsInContainer.getMinY());
    }

    private void createGrid() {
        int tableSize = gridSize + 1;
        // Create the grid
        for (int rowIndex = 0; rowIndex < tableSize; rowIndex++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setHgrow(Priority.NEVER);
            grid.getColumnConstraints().add(column);

            RowConstraints row = new RowConstraints();
            row.setVgrow(Priority.NEVER);
            grid.getRowConstraints().add(row);

            for (int columnIndex = 0; columnIndex < tableSize; columnIndex++) {
                AnchorPane tile = new AnchorPane();
                tile.getStyleClass().add("tile");
                grid.add(tile, rowIndex, columnIndex);

                if (rowIndex == 0 || columnIndex == 0) {
                    StackPane tileTextContainer = new StackPane();
                    tile.getChildren().add(tileTextContainer);
                    String coordinate = (rowIndex == 0 && columnIndex > 0) ? String.valueOf(columnIndex)
                            : (columnIndex == 0 && rowIndex > 0) ? String.valueOf((char) ('A' + rowIndex - 1)) : "";
                    Label label = new Label(coordinate);
                    tileTextContainer.setAlignment(Pos.CENTER);
                    tileTextContainer.getChildren().add(label);
                } else {
                    final int currentRowIndex = rowIndex;
                    final int currentColumnIndex = columnIndex;
                    tile.setOnMouseEntered(event -> {
                        tile.setStyle("-fx-background-color:#FFFF00;");
                        System.out.printf("Mouse enetered cell [%d, %d]%n", currentColumnIndex, currentRowIndex);
                        if (this.selectedShip != null) {
                            previewShipPlacement(this.selectedShip, currentColumnIndex, currentRowIndex, tile);
                        }

                    });

                    tile.setOnMouseExited(event -> {
                        tile.setStyle("-fx-background-color:none;");
                    });
                }
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
        // stage.show();

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
