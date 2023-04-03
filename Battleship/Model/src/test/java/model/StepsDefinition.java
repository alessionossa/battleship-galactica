package model;

import static org.junit.Assert.assertEquals;

import com.galactica.model.Coordinate;
import com.galactica.model.Cruiser;
import com.galactica.model.DeathStar;
import com.galactica.model.Direction;
import com.galactica.model.Grid;
import com.galactica.model.Human;
import com.galactica.model.Scout;
import com.galactica.model.Ship;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class StepsDefinition {

	Grid ownGrid;
	Grid opponentGrid;
	Human player;
	Ship ship;

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
		player.placeShip(ship, coordinate, direction);
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

	@When("I remove the ship")
	public void i_remove_the_ship() {
		player.removeShip(ship);
	}

	@Then("The ship disappears from my grid")
	public void the_ship_disappears_from_my_grid() {
		assertEquals(ship.getCoordinate(), null);
		assertEquals(ship.getDirection(), null);
		assertEquals(ownGrid.anyShipsPlaced(), false);
	}

}