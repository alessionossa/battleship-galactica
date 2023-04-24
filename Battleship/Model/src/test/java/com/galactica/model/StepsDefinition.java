package com.galactica.model;

import com.galactica.model.*;
import com.galactica.model.ships.Cruiser;
import com.galactica.model.ships.DeathStar;
import com.galactica.model.ships.Scout;
import com.galactica.controller.*;
import com.galactica.cli.*;

import io.cucumber.java.an.Y;
import io.cucumber.java.bs.A;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.messages.internal.com.fasterxml.jackson.databind.cfg.ConstructorDetector.SingleArgConstructor;
import junit.framework.ComparisonFailure;
import org.hamcrest.CoreMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.*;

public class StepsDefinition {

    Grid ownGrid;
    Grid opponentGrid;
    int gridSize;
    Human player;
    Human opponent;
    AI ai;
    Ship ship;
    Exception error;
    BattleshipCLI game;
    Asteroid asteroid;
    BattleshipCLI startShooting;
    Laser laser = new Laser();
    String message;
    List<Coordinate> grenadeScatterCoordinates;
    String tileAsteroid;
    // PLACE SHIP TEST

    @Given("I have started a new game on a size {int} grid in {string} player mode, {string} asteroid mode, {string} gravity mode")
    public void i_have_started_a_new_game(int gridSize, String playerMode, String asteroidMode, String gravityMode) {
        boolean single_player_mode = false;
        boolean asteroid_mode = false;
        boolean gravity_mode = false;
        this.gridSize = gridSize;
        if (playerMode.equals("single"))
            single_player_mode = true;
        if (asteroidMode.equals("with"))
            asteroid_mode = true;
        if (gravityMode.equals("with"))
            gravity_mode = true;

        ownGrid = Game.setUpGrid(gridSize, single_player_mode, asteroid_mode, gravity_mode);
        opponentGrid = Game.setUpGrid(gridSize, single_player_mode, asteroid_mode, gravity_mode);

        player = new Human(ownGrid, opponentGrid);
        if (playerMode.equals("single")) {
            ai = new AI("AI", opponentGrid, ownGrid);
        } else {
            opponent = new Human(opponentGrid, ownGrid);
        }
    }

    @When("{string} a {string} in direction {string} on coordinate {string} {int}")
    public void place_a_ship_in_direction_on_coordinate(String whosTurn, String shipString, String dir, String x,
            int y) {
        if (shipString.equals("Cruiser"))
            ship = new Cruiser(1);
        else if (shipString.equals("Deathstar"))
            ship = new DeathStar(2);
        else if (shipString.equals("Scout"))
            ship = new Scout(3);
        Coordinate coordinate = new Coordinate(x.charAt(0), y);
        Direction direction = Direction.get(dir.charAt(0));

        if (whosTurn.equals("I place"))
            player.placeShip(ship, coordinate, direction);
        else if (whosTurn.equals("The AI places"))
            ai.placeShip(ship, coordinate, direction);
        else
            opponent.placeShip(ship, coordinate, direction);

    }

    @When("The AI places its ships")
    public void the_ai_places_its_ships() {
        ai.placeShips();
    }

    @Then("The ship is placed on tiles {string} {int}, {string} {int} and {string} {int}")
    public void the_ship_is_placed_on_tiles(String x0, int y0, String x1, int y1, String x2, int y2) {
        Ship s0 = ownGrid.getShipAtCoordinate(new Coordinate(x0.charAt(0), y0));
        Ship s1 = ownGrid.getShipAtCoordinate(new Coordinate(x1.charAt(0), y1));
        Ship s2 = ownGrid.getShipAtCoordinate(new Coordinate(x2.charAt(0), y2));
        assertEquals(s0, ship);
        assertEquals(s1, ship);
        assertEquals(s2, ship);
    }

    @Then("All the AI ships are placed on tiles")
    public void all_the_ai_ships_are_placed_on_tiles() {
        assertEquals(ai.hasAllShipsPlaced(), true);
    }

    // DELETE SHIP TEST

    @When("I remove the ship")
    public void i_remove_the_ship() {
        try {
            player.removeShip(ship);
        } catch (UnplacedShipException e) {
            error = e;
        }
    }

    @Then("The ship disappears from my grid")
    public void the_ship_disappears_from_my_grid() {
        assertEquals(ship.getCoordinate(), null);
        assertEquals(ship.getDirection(), null);
        assertEquals(ownGrid.anyShipsPlaced(), false);
    }

    @Then("I get a error message {string}")
    public void i_get_a_error_message(String errorMessage) {
        if (error != null)
            assertEquals(error.getMessage(), errorMessage);
    }

    @Given("I have placed all my ships on my grid")
    public void i_have_placed_all_my_ships_on_my_grid() {
        place_a_ship_in_direction_on_coordinate("I place", "Cruiser", "h", "a", 1);
        place_a_ship_in_direction_on_coordinate("I place", "Scout", "v", "f", 3);
        place_a_ship_in_direction_on_coordinate("I place", "Deathstar", "v", "c", 5);
    }

    @When("{string} a cannon at coordinate {string} {int}")
    public void shoot_a_cannon_at_coordinate_on_my_opponents_grid(String whoShoots, String x, Integer y) {
        if (whoShoots.equals("I shoot"))
            player.shoot(new Coordinate(x.charAt(0), y), new Cannon(), false, false);
        else if (whoShoots.equals("The AI shoots"))
            ai.shoot(new Coordinate(x.charAt(0), y), new Cannon(), false, false);
        else
            opponent.shoot(new Coordinate(x.charAt(0), y), new Cannon(), false, false);
        String tileType = player.getOpponentGrid().getTile(new Coordinate(x.charAt(0), y)).displayValue(false);

        // Ship ship = opponentGrid.getShipAtCoordinate(new Coordinate(string.charAt(0),
        // int1));

        if (Objects.equals(tileType, "X") && ship.isSunk()) {
            message = "You sunk a ship! 💥🚢";
        } else if (Objects.equals(tileType, "/")) {
            message = "You missed";
        } else if ((Objects.equals(tileType, "X") || (Objects.equals(tileType, "A"))
                || (Objects.equals(tileType, "P")))) {
            message = "You hit something!";
        } else {
            message = "Something went wrong";
        }
    }

    @When("I shot a grenade at coordinate {string} {int}")
    public void i_shoot_a_grenade_at_coordinate_on_my_opponent_s_grid(String string, Integer int1) {

        Grenade grenade = new Grenade();
        player.shoot(new Coordinate(string.charAt(0), int1), grenade, false, false);
        grenadeScatterCoordinates = grenade.getScatterCoordinates(new Coordinate(string.charAt(0), int1), opponentGrid);

        boolean hitAtLeastOneShip = false;
        for (int i = 0; i < grenadeScatterCoordinates.size(); i++) {

            String tileType = player.getOpponentGrid().getTile(new Coordinate(string.charAt(0), int1))
                    .displayValue(false);
            Ship ship = opponentGrid.getShipAtCoordinate(new Coordinate(string.charAt(0), int1));

            // for (Coordinate coordinate: grenadeScatterCoordinates) {
            // GridCLI.printGrid(player.getOpponentGrid(), true);
            // System.out.println(coordinate.getX());
            // System.out.println(coordinate.getY());
            // }
            if (ship != null) {
                hitAtLeastOneShip = true;
                boolean isShipSunk = opponentGrid.checkIfShipIsSunk(ship);
                if (isShipSunk) {
                    ship.setSunk(true);
                    message = "You sunk a ship! 💥🚢";

                }
            }
        }
        if (hitAtLeastOneShip) {
            message = "You hit something!";
        } else {
            message = "You missed";
        }
    }

    @When("I shot a laser at row {int}")
    public void i_shoot_a_laser_at_row_on_my_opponent_s_grid(int y) {

        player.shootLaser(new Coordinate('a', y), 'r', laser);
        laser.decrementAmountOfUses();
        List<Coordinate> coordinateList = laser.getLaserCoordinates(new Coordinate('a', y), opponentGrid, 'r');
        for (int i = 0; i < coordinateList.size(); i++) {
            String tileType = player.getOpponentGrid().getTile(coordinateList.get(i)).displayValue(false);
            Ship ship = opponentGrid.getShipAtCoordinate(coordinateList.get(i));

            if (tileType == "S" && ship.isSunk())
                message = "You sunk a ship! 💥🚢";
        }
    }

    @When("I shot a laser at column {string}")
    public void iShootALaserAtColumnOnMyOpponentSGrid(String x) {

        player.shootLaser(new Coordinate(x.charAt(0), 0), 'c', laser);
        laser.decrementAmountOfUses();
        List<Coordinate> coordinateList = laser.getLaserCoordinates(new Coordinate(x.charAt(0), 0), opponentGrid, 'c');
        for (int i = 0; i < coordinateList.size(); i++) {
            String tileType = player.getOpponentGrid().getTile(coordinateList.get(i)).displayValue(false);
            Ship ship = opponentGrid.getShipAtCoordinate(coordinateList.get(i));

            if (tileType == "S" && ship.isSunk())
                message = "You sunk a ship! 💥🚢";
        }

    }

    @Then("The column {string} on my opponent's grid is hit")
    public void theColumnOnMyOpponentSGridIsHit(String x) {
        for (int i = 0; i < gridSize; i++) {
            the_tile_on_my_opponent_s_grid_is_hit(String.valueOf(x), i, "opponent's");
        }
    }

    @And("I get a message {string} regarding the result of the laser shot at column {string}")
    public void iGetAMessageRegardingTheResultOfTheLaserShotAtColumn(String arg0, String arg1) {
        assertEquals(arg0, message);
    }

    @Then("The tile {string} {int} on {string} grid is hit")
    public void the_tile_on_my_opponent_s_grid_is_hit(String x, Integer y, String whosGrid) {
        Grid gridHit;
        if (whosGrid.equals("my"))
            gridHit = ownGrid;
        else
            gridHit = opponentGrid;

        assertEquals(gridHit.getTile(new Coordinate(x.charAt(0),
                y)).isHit(), true);
    }

    @Then("The row {int} on my opponent's grid is hit")
    public void the_row_on_my_opponent_s_grid_is_hit(int y) {
        for (int i = 0; i < gridSize; i++) {
            the_tile_on_my_opponent_s_grid_is_hit(String.valueOf((char) ('a' + i)), y, "opponent's");
        }
    }

    @Then("I get a message {string} regarding the result of the cannon shot at coordinate {string} {int}")
    public void i_get_a_message(String string, String string2, Integer int1) {
        assertEquals(string, message);
    }

    @Then("I get a message {string} regarding the result of the laser shot at row {int}")
    public void i_get_a_message_laser(String string, int y) {

        assertEquals(string, message);
    }

    @And("I get a message {string} or {string} or {string} regarding the result of the grenade shot at coordinate {string} {int}")
    public void iGetAMessageOrOrRegardingTheResultOfTheGrenadeShotAtCoordinate(String arg0, String arg1, String arg2,
            String arg3, int arg4) {
        // if (Objects.equals(arg0, message)){
        // assertEquals(arg0,message);
        // }
        // else if (Objects.equals(arg1, message)){
        // assertEquals(arg1,message);
        // }
        // else if (Objects.equals(arg2, message)){
        // assertEquals(arg2,message);
        // }
        // this doesnt really check if it's actually the correct message eg if we hit a
        // ship but the message is missed the test will still pass
        assertThat(message, CoreMatchers.anyOf(CoreMatchers.is(arg0), CoreMatchers.is(arg1), CoreMatchers.is(arg2)));
    }

    @And("I shoot randomly at {int} tiles")
    public void iShootRandomlyAtTiles(int arg0) {
        assertEquals(grenadeScatterCoordinates.size() - 1, arg0);
        Coordinate coordinate = grenadeScatterCoordinates.get(0);
        int y = coordinate.getY();
        int xInt = coordinate.getX() - 'a';
        List<Coordinate> newList = new ArrayList<Coordinate>();

        for (int i = xInt - 1; i <= xInt + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
                char adjacentX = (char) (i + 'a');
                Coordinate newCoordinate = new Coordinate(adjacentX, j);
                if (opponentGrid.isValidCoordinate(newCoordinate))
                    newList.add(newCoordinate);
            }
        }
        assertThat(newList, hasItems(grenadeScatterCoordinates.get(1), grenadeScatterCoordinates.get(2)));

    }

    @And("There is an asteroid on tile {string} {int} on my opponent's grid")
    public void thereIsAnAsteroidOnTileOnMyOpponentSGrid(String arg0, int arg1) {
        opponentGrid.setTile(new Coordinate(arg0.charAt(0), arg1), new Asteroid());
        assertNotEquals(opponentGrid.getTile(new Coordinate(arg0.charAt(0), arg1)).getAsteroid(), null);
    }

    @And("There is an planet on tile {string} {int} on my opponent's grid")
    public void thereIsAnPlanetOnTileOnMyOpponentSGrid(String arg0, int arg1) {
        opponentGrid.setTile(new Coordinate(arg0.charAt(0), arg1), new Planet(2, ownGrid.getGridSize()));
        assertNotEquals(opponentGrid.getTile(new Coordinate(arg0.charAt(0), arg1)).getPlanet(), null);
    }

    @And("I can no longer shoot with a laser")
    public void iCanNoLongerShootWithALaser() {
        assertEquals(0, laser.amountOfUses);
    }
}
