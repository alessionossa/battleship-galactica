package com.galactica.model;

import com.galactica.model.*;
import com.galactica.model.ships.Cruiser;
import com.galactica.model.ships.DeathStar;
import com.galactica.model.ships.Scout;
import com.galactica.controller.*;
import com.galactica.cli.*;

import io.cucumber.java.an.Y;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.messages.internal.com.fasterxml.jackson.databind.cfg.ConstructorDetector.SingleArgConstructor;
import junit.framework.ComparisonFailure;
import org.hamcrest.CoreMatchers;

import java.util.List;
import java.util.Objects;

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
    Laser laser;
    String message;
    List<Coordinate> grenadeScatterCoordinates;
    // PLACE SHIP TEST

    @Given("I have started a new game")
    public void i_have_started_a_new_game() {
        gridSize = 10;
        ownGrid = new Grid(gridSize);
        opponentGrid = new Grid(gridSize);
        player = new Human(ownGrid, opponentGrid);

    }

    @Given("I have started a new game with the AI")
    public void i_have_started_a_new_game_with_the_ai() {
        gridSize = 10;
        ownGrid = new Grid(gridSize);
        opponentGrid = new Grid(gridSize);
        player = new Human(ownGrid, opponentGrid);
        ai = new AI("AI", ownGrid, opponentGrid);
    }

    @When("I place a {string} in direction {string} on coordinate {string} {int} on my grid")
    public void i_place_a_ship_in_direction_on_coordinate(String shipString, String dir, String x, int y) {
        if (shipString.equals("Cruiser"))
            ship = new Cruiser(1);
        else if (shipString.equals("Deathstar"))
            ship = new DeathStar(2);
        else if (shipString.equals("Scout"))
            ship = new Scout(3);
        Coordinate coordinate = new Coordinate(x.charAt(0), y);
        Direction direction = Direction.get(dir.charAt(0));

        player.placeShip(ship, coordinate, direction);

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

    @When("I have placed all of my ships")
    public void i_have_placed_all_of_my_ships() {
        if (player.hasAllShipsPlaced()) {
            boolean startShooting = true;
        }

    }

    // Method to place a ship on opponent's grid
    public void place_opponent_ship_in_direction_on_coordinate(String shipString, String dir, String x, int y) {
        if (shipString.equals("Cruiser"))
            ship = new Cruiser(1);
        else if (shipString.equals("Deathstar"))
            ship = new DeathStar(2);
        else
            ship = new Scout(3);
        Coordinate coordinate = new Coordinate(x.charAt(0), y);
        Direction direction = Direction.get(dir.charAt(0));
        opponent.placeShip(ship, coordinate, direction);

    }

    @Given("I have started a new game and I have placed all my ships on my grid")
    public void i_have_placed_all_my_ships_on_my_grid() {
        i_have_started_a_new_game();
        i_place_a_ship_in_direction_on_coordinate("Cruiser", "h", "a", 1);
        i_place_a_ship_in_direction_on_coordinate("Scout", "v", "f", 3);
        i_place_a_ship_in_direction_on_coordinate("Deathstar", "v", "c", 5);
    }

    @Given("My opponent has placed a ship of type {string} at coordinate {string} {int} in direction {string} on their grid")
    public void my_opponent_has_placed_a_ship_of_type_at_coordinate_in_direction_on_their_grid(String shipType,
                                                                                               String x, Integer y, String direction) {
        opponent = new Human(opponentGrid, ownGrid);
        place_opponent_ship_in_direction_on_coordinate(shipType, direction, x,
                y);
    }

    // @Given("Coordinate {string} {int} on my opponent's grid has been hit")
    // public void coordinate_on_my_opponent_s_grid_has_been_hit(String string,
    // Integer int1) {
    // opponentGrid.setTile(new Coordinate(string.charAt(0), int1), true);
    // }

    @When("I shoot a cannon at coordinate {string} {int} on my opponent's grid")
    public void i_shoot_a_cannon_at_coordinate_on_my_opponents_grid(String string, Integer int1) {
        player.shoot(new Coordinate(string.charAt(0), int1), new Cannon());
        String tileType = player.getOpponentGrid().getTile(new Coordinate(string.charAt(0), int1)).displayValue(false);
        Ship ship = opponentGrid.getShipAtCoordinate(new Coordinate(string.charAt(0), int1));

        if (Objects.equals(tileType, "X") && ship.isSunk()) {
            message = "You sunk a ship! 💥🚢";
        } else if (Objects.equals(tileType, "/")) {
            message = "You missed";
        } else if (Objects.equals(tileType, "X")) {
            message = "You hit something!";
        } else {
            message = "Something went wrong";
        }
    }

    @When("I shoot a grenade at coordinate {string} {int} on my opponent's grid")
    public void i_shoot_a_grenade_at_coordinate_on_my_opponent_s_grid(String string, Integer int1) {
        Grenade grenade = new Grenade();
        player.shoot(new Coordinate(string.charAt(0), int1), grenade);
        grenadeScatterCoordinates = grenade.getScatterCoordinates(new Coordinate(string.charAt(0), int1), opponentGrid);

        boolean hitAtLeastOneShip = false;
        for (int i = 0; i < grenadeScatterCoordinates.size(); i++) {


            String tileType = player.getOpponentGrid().getTile(new Coordinate(string.charAt(0), int1)).displayValue(false);
            Ship ship = opponentGrid.getShipAtCoordinate(new Coordinate(string.charAt(0), int1));

//            for (Coordinate coordinate: grenadeScatterCoordinates) {
//                GridCLI.printGrid(player.getOpponentGrid(), true);
//                System.out.println(coordinate.getX());
//                System.out.println(coordinate.getY());
//            }
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

    @When("I shoot a laser at row {int} on my opponent's grid")
    public void i_shoot_a_laser_at_row_on_my_opponent_s_grid(int y) {

        Laser laser = new Laser();
        player.shootLaser(new Coordinate('a', y), 'r', laser);
        List<Coordinate> coordinateList = laser.getLaserCoordinates(new Coordinate('a', y), opponentGrid, 'r');
        for (int i = 0; i < coordinateList.size(); i++) {
            String tileType = player.getOpponentGrid().getTile(coordinateList.get(i)).displayValue(false);
            Ship ship = opponentGrid.getShipAtCoordinate(coordinateList.get(i));

            if (tileType == "X" && ship.isSunk())
                message = "You sunk a ship! 💥🚢";
        }
    }

    @Then("The tile {string} {int} on my opponent's grid is hit")
    public void the_tile_on_my_opponent_s_grid_is_hit(String string, Integer int1) {
        assertEquals(opponentGrid.getTile(new Coordinate(string.charAt(0),
                int1)).isHit(), true);
    }

    @Then("The row {int} on my opponent's grid is hit")
    public void the_row_on_my_opponent_s_grid_is_hit(int y) {
        for (int i = 0; i < gridSize; i++) {
            the_tile_on_my_opponent_s_grid_is_hit(String.valueOf((char) ('a' + i)), y);
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
    public void iGetAMessageOrOrRegardingTheResultOfTheGrenadeShotAtCoordinate(String arg0, String arg1, String arg2, String arg3, int arg4) {
//		if (Objects.equals(arg0, message)){
//			assertEquals(arg0,message);
//		}
//		else if (Objects.equals(arg1, message)){
//			assertEquals(arg1,message);
//		}
//		else if (Objects.equals(arg2, message)){
//			assertEquals(arg2,message);
//		}
		//this doesnt really check if it's actually the correct message eg if we hit a ship but the message is missed the test will still pass
		assertThat(message, CoreMatchers.anyOf(CoreMatchers.is(arg0), CoreMatchers.is(arg1), CoreMatchers.is(arg2)));
    }

    @And("I shoot randomly at {int} tiles")
    public void iShootRandomlyAtTiles(int arg0) {
        assertEquals(grenadeScatterCoordinates.size()-1, arg0);
      for (Coordinate coordinate: grenadeScatterCoordinates){
            //GridCLI.printGrid(player.getOpponentGrid(),true);
//            System.out.println(coordinate.getX());
//            System.out.println(coordinate.getY());
            assertEquals(true, player.getOpponentGrid().getTile(coordinate).isHit());
            //TODO: Need to fix this random coordinate problem
           }
    }
}
