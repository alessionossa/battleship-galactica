package model;

import static org.junit.Assert.assertEquals;

import com.galactica.model.Coordinate;
import com.galactica.model.Cruiser;
import com.galactica.model.Direction;
import com.galactica.model.Grid;
import com.galactica.model.Human;
import com.galactica.model.Player;
import com.galactica.model.Ship;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class StepsDefinition {

	Grid ownGrid;
	Grid opponentGrid;
	Human player;
	Ship ship;

	@Given("Not all my ships have been placed")
	public void not_all_my_ships_have_been_placed() {
		ownGrid = new Grid();
		opponentGrid = new Grid();
		player = new Human(ownGrid, opponentGrid);

	}

	@When("I place a {string} in direction {string} on coordinate {string} {int}")
	public void i_place_a_ship_in_direction_on_coordinate(String shipInput, String dir, String x, int y) {

		ship = new Cruiser(1);
		Coordinate coordinate = new Coordinate(x.charAt(0), y);
		Direction direction = Direction.get(dir.charAt(0));
		ownGrid.placeShip(ship, coordinate, direction);
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

}