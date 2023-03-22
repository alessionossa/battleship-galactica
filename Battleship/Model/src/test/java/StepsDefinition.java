import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class StepsDefinition {
	@Given("The game has not started")
	public void the_game_has_not_started() {

	}

	@Given("I have an empty grid")
	public void i_have_an_empty_grid() {
	}

	@Given("Not all my ships have been placed")
	public void not_all_my_ships_have_been_placed() {
	} 

	@When("I place a ship of length {int} {string} on tile {string}")
	public void i_place_a_ship_of_length_on_tile(Integer int1, String string, String string2) {
	}

	@Then("The ship appears on tiles {string}, {string} and {string}")
	public void the_ship_appears_on_tiles_and(String string, String string2, String string3) {
	}

	@Then("I have one less ship to be placed")
	public void i_have_one_less_ship_to_be_placed() {
	}

}
