package model;

import static org.junit.Assert.assertEquals;

import com.galactica.model.*;
import com.galactica.controller.*;
import com.galactica.cli.*;

import io.cucumber.java.an.Y;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.messages.internal.com.fasterxml.jackson.databind.cfg.ConstructorDetector.SingleArgConstructor;

public class StepsDefinition {

	Grid ownGrid;
	Grid opponentGrid;
	Human player;
	Human player1;
	Ship ship;
	Exception error;
	BattleshipCLI game;


	// PLACE SHIP TEST

	@Given("I have started a new game")
	public void i_have_started_a_new_game() {
		ownGrid = new Grid();
		opponentGrid = new Grid();
		player = new Human(ownGrid, opponentGrid);

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

		try {
			player.placeShip(ship, coordinate, direction);
		} catch (OutOfBoundsException e) {
			error = e;
		}
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
	

	// START GAME TEST: In progress

	/*@Given("I have not started a new game")
	public void i_have_not_started_a_new_game() {
		ownGrid = null;
		opponentGrid = null;
		player = null;
		player1 = null;
		game = null;
	}

	@When("I choose to start a new game with a person")
	public void i_choose_to_start_a_new_game_with_a_person() {
		game = new BattleshipCLI(new CLI());
		boolean singlePlayerMode = false;
		game.startGame(ownGrid, opponentGrid, singlePlayerMode);
	}

	@When("I choose to start a new game with the computer")
	public void i_choose_to_start_a_new_game_with_the_computer() {
		game = new BattleshipCLI(new CLI());
		boolean singlePlayerMode = true;
		game.startGame(ownGrid, opponentGrid, singlePlayerMode);
	}

	@Then("A multiplayer game has been started")
	public void a_multiplayer_game_has_been_started() {
		assertnotEquals(player1, error);
	}

	@Then("A game against the computer has been started")
	public void a_game_against_the_computer_has_been_started() {
		assertEquals(opponentGrid, error);
	}*/

	// SEE GRID TEST
}
