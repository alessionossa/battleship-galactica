package model;

import static org.junit.Assert.assertEquals;

import com.galactica.model.Coordinate;
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
	Player player;
	Ship ship;

	@Given("Not all my ships have been placed")
	public void not_all_my_ships_have_been_placed() {
		ownGrid = new Grid();
		opponentGrid = new Grid();
		player = new Human(ownGrid, opponentGrid);

	}

	@When("I place a {Ship} in direction {char} on coordinate {char} {int}")
	public void i_place_a_ship_in_direction_on_coordinate(Ship shipInput, char directionChar, char x, int y) {
		ship = shipInput;
		Coordinate coordinate = new Coordinate(x, y);
		Direction direction = Direction.get(directionChar);
		player.placeShip(ship, coordinate, direction);
	}

	@Then("The ship is placed on tiles {char} {int}, {char} {int} and {char} {int}")
	public void the_ship_is_placed_on_tiles_and(char x, int y, char x1, int y1, char x2, int y2) {
		Ship s0 = ownGrid.getShipAtCoordinate(new Coordinate(x, y));
		Ship s1 = ownGrid.getShipAtCoordinate(new Coordinate(x1, y1));
		Ship s2 = ownGrid.getShipAtCoordinate(new Coordinate(x1, y1));
		assertEquals(s0, ship);
		assertEquals(s1, ship);
		assertEquals(s2, ship);

	}

}
