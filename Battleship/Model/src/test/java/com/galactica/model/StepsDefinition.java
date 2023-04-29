package com.galactica.model;

import com.galactica.model.ships.*;
import com.galactica.cli.*;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.List;

import static org.junit.Assert.*;

public class StepsDefinition {

    Grid ownGrid;
    Grid opponentGrid;
    int gridSize;
    Human player;
    Human opponent;
    AI ai;
    Ship ship;
    Ship ship2;
    Exception error;
    Game game = new Game();
    Asteroid asteroid;
    Laser laser = new Laser();
    String message;
    List<Coordinate> grenadeScatterCoordinates;
    String tileAsteroid;
    Coordinate asteroidCoordinate;
    Coordinate planetCoordinate;
    Coordinate coordinateToShoot;

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

    @When("I try to place a {string} on {string}")
    public void i_try_to_place_a_ship_on_an_asteroid(String shipString, String obstacle) {
        if (shipString.equals("Cruiser"))
            ship2 = new Cruiser(1);
        else if (shipString.equals("Deathstar"))
            ship2 = new DeathStar(2);
        else if (shipString.equals("Scout"))
            ship2 = new Scout(3);

        if (obstacle.equals("an asteroid")) {
            asteroidCoordinate = ownGrid.getAsteroids().get(0).getCoordinate();
            player.placeShip(ship2, asteroidCoordinate, Direction.get('h'));
        } else if (obstacle.equals("a planet")) {
            planetCoordinate = ownGrid.getPlanets().get(0).getCoordinate();
            player.placeShip(ship2, planetCoordinate, Direction.get('v'));
        } else if (obstacle.equals("the ship")) {
            player.placeShip(ship2, ship.getCoordinate(), Direction.get('v'));
        } else if (obstacle.equals("the right corner of the grid")) {
            player.placeShip(ship2, new Coordinate('j', 0), Direction.get('h'));
        }

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

    @Then("The ship is not placed on the grid")
    public void the_ship_is_not_placed() {
        assertEquals(ship2.getDirection(), null);
        assertEquals(ship2.isPlaced(), false);
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

    // SHOOT TEST

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
            ai.shootCannon(new Coordinate(x.charAt(0), y), false, false);
        else
            opponent.shoot(new Coordinate(x.charAt(0), y), new Cannon(), false, false);
    }

    @When("{string} a cannon at {string}")
    public void shoot_a_cannon_at_obstacle(String whoShoots, String obstacle) {
        if (whoShoots.equals("I shoot")) {
            if (obstacle.equals("an asteroid"))
                coordinateToShoot = opponentGrid.getAsteroids().get(0).getCoordinate();
            else if (obstacle.equals("a planet"))
                coordinateToShoot = opponentGrid.getPlanets().get(0).getCoordinate();
            GridCLI.printGrid(player.getOpponentGrid(), true);
            player.shoot(coordinateToShoot, player.getCannon(), false, false);
        } else if (whoShoots.equals("The AI shoots")) {
            if (obstacle.equals("an asteroid"))
                coordinateToShoot = ownGrid.getAsteroids().get(0).getCoordinate();
            else if (obstacle.equals("a planet"))
                coordinateToShoot = ownGrid.getPlanets().get(0).getCoordinate();
            ai.shootCannon(coordinateToShoot, false, false);
        }
    }

    @When("I shot a grenade at coordinate {string} {int}")
    public void i_shoot_a_grenade_at_coordinate_on_my_opponent_s_grid(String string, Integer int1) {
        Grenade grenade = new Grenade();
        grenadeScatterCoordinates = player.shootGrenade(new Coordinate(string.charAt(0), int1), grenade);
    }

    @When("{string} a laser at row {int}")
    public void a_laser_at_row(String whoShoots, Integer y) {
        if (whoShoots.equals("I shot")) {
            player.shootLaser(new Coordinate('a', y), 'r', laser);
        } else if (whoShoots.equals("The AI shoots")) {
            ai.shootLaser(new Coordinate('a', y), 'r', laser);
        } else {
            opponent.shoot(new Coordinate('a', y), laser, false, false);
        }

    }

    @When("{string} a laser at column {string}")
    public void iShootALaserAtColumnOnMyOpponentSGrid(String whoShoots, String x) {
        if (whoShoots.equals("I shot")) {
            player.shootLaser(new Coordinate(x.charAt(0), 0), 'c', laser);
        } else if (whoShoots.equals("The AI shoots")) {
            ai.shootLaser(new Coordinate(x.charAt(0), 0), 'c', laser);
        } else {
            opponent.shoot(new Coordinate(x.charAt(0), 0), laser, false, false);
        }
    }

    @When("The AI tries to track the ship until it's sunk")
    public void the_ai_tries_to_track_the_ship() {
        while (!(ship.isSunk()))
            ai.shoot(null, null, false, false);
    }

    @When("The AI tries to track the asteroid until it hits all the nearby tiles")
    public void the_ai_tries_to_track_the_asteroid() {
        while (ai.getFollowTragetMode())
            ai.shoot(null, null, false, false);
    }

    @When("{string} a cannon {string} a planet")
    public void shoot_a_cannon_next_to_a_planet(String whoShoots, String shootWhere) {
        Coordinate coordinate = opponentGrid.getPlanets().get(0).getCoordinate();
        if (whoShoots.equals("The AI shoots"))
            coordinate = ownGrid.getPlanets().get(0).getCoordinate();

        if (shootWhere.equals("above")) {
            coordinateToShoot = new Coordinate(coordinate.getX(), coordinate.getY() - 1);
        } else if (shootWhere.equals("below")) {
            coordinateToShoot = new Coordinate(coordinate.getX(), coordinate.getY() + 2);
        } else if (shootWhere.equals("left of")) {
            coordinateToShoot = new Coordinate((char) (coordinate.getX() - 1), coordinate.getY());
        } else if (shootWhere.equals("right of")) {
            coordinateToShoot = new Coordinate((char) (coordinate.getX() + 2), coordinate.getY());
        } else if (shootWhere.equals("right above")) {
            coordinateToShoot = new Coordinate((char) (coordinate.getX() + 2), coordinate.getY() - 1);
        } else if (shootWhere.equals("right below")) {
            coordinateToShoot = new Coordinate((char) (coordinate.getX() + 2), coordinate.getY() + 2);
        } else if (shootWhere.equals("left above")) {
            coordinateToShoot = new Coordinate((char) (coordinate.getX() - 1), coordinate.getY() - 1);
        } else if (shootWhere.equals("left below")) {
            coordinateToShoot = new Coordinate((char) (coordinate.getX() - 1), coordinate.getY() + 2);
        }
        if (whoShoots.equals("I shoot")) {
            player.shoot(coordinateToShoot, player.getCannon(), true, false);
        } else if (whoShoots.equals("The AI shoots")) {
            ai.shootCannon(coordinateToShoot, true, false);
        }
    }

    @Then("The shot gets rebounded {string} the planet on {string} grid")
    public void the_shot_gets_rebounded(String reboundWhere, String whosGrid) {
        Coordinate coordinate;
        Grid grid;
        if (whosGrid.equals("my")) {
            grid = ownGrid;
            coordinate = ownGrid.getPlanets().get(0).getCoordinate();
        } else {
            grid = opponentGrid;
            coordinate = opponentGrid.getPlanets().get(0).getCoordinate();
        }
        if (reboundWhere.equals("above")) {
            assertEquals(grid.getTile(new Coordinate(coordinate.getX(), coordinate.getY() - 1)).isHit(), true);
        } else if (reboundWhere.equals("below")) {
            assertEquals(grid.getTile(new Coordinate(coordinate.getX(), coordinate.getY() + 2)).isHit(), true);
        } else if (reboundWhere.equals("left of")) {
            assertEquals(
                    grid.getTile(new Coordinate((char) (coordinate.getX() - 1), coordinate.getY())).isHit(),
                    true);
        } else if (reboundWhere.equals("right of")) {
            assertEquals(
                    grid.getTile(new Coordinate((char) (coordinate.getX() + 2), coordinate.getY())).isHit(),
                    true);
        } else if (reboundWhere.equals("right above")) {
            assertEquals(
                    grid.getTile(new Coordinate((char) (coordinate.getX() + 2), coordinate.getY() - 1)).isHit(),
                    true);
        } else if (reboundWhere.equals("right below")) {
            assertEquals(
                    grid.getTile(new Coordinate((char) (coordinate.getX() + 2), coordinate.getY() + 2)).isHit(),
                    true);
        } else if (reboundWhere.equals("left above")) {
            assertEquals(
                    grid.getTile(new Coordinate((char) (coordinate.getX() - 1), coordinate.getY() - 1)).isHit(),
                    true);
        } else if (reboundWhere.equals("left below")) {
            assertEquals(
                    grid.getTile(new Coordinate((char) (coordinate.getX() - 1), coordinate.getY() + 2)).isHit(),
                    true);
        }
    }

    @Then("The column {string} on {string} grid is hit")
    public void the_column_on_grid_is_hit(String x, String whosGrid) {
        if (whosGrid.equals("my opponent's")) {
            for (int i = 0; i < gridSize; i++) {
                the_tile_on_my_opponent_s_grid_is_hit(String.valueOf(x), i, "opponent's");
            }
        } else {
            for (int i = 0; i < gridSize; i++) {
                the_tile_on_my_opponent_s_grid_is_hit(String.valueOf(x), i, "my");
            }
        }
    }


    @Then("The tile {string} {int} on {string} grid is hit")
    public void the_tile_on_my_opponent_s_grid_is_hit(String x, Integer y, String whosGrid) {
        Grid gridHit;
        if (whosGrid.equals("my"))
            gridHit = ownGrid;
        else
            gridHit = opponentGrid;
        assertEquals(gridHit.getTile(new Coordinate(x.charAt(0), y)).isHit(), true);
    }

    @Then("The row {int} on {string} grid is hit")
    public void the_row_on_my_opponent_s_grid_is_hit(int y, String whosGrid) {
        if (whosGrid.equals("my opponent's"))
            for (int i = 0; i < gridSize; i++) {
                the_tile_on_my_opponent_s_grid_is_hit(String.valueOf((char) ('a' + i)), y, "opponent's");
            }
        else if (whosGrid.equals("my"))
            for (int i = 0; i < gridSize; i++) {
                the_tile_on_my_opponent_s_grid_is_hit(String.valueOf((char) ('a' + i)), y, "my");
            }
    }


    @Then("The {string} on my opponent's grid is hit")
    public void the_obstacle_on_grid_is_hit(String obstacle) {
        assertEquals(opponentGrid.getTile(coordinateToShoot).isHit(), true);

    }

    @Then("{int} random adjacent tiles on {string} grid are hit")
    public void random_tiles_are_hit(int howManyTiles, String whosGrid) {
        assertEquals(grenadeScatterCoordinates.size() - 1, howManyTiles);
        for (Coordinate coordinate : grenadeScatterCoordinates) {
            the_tile_on_my_opponent_s_grid_is_hit(String.valueOf(coordinate.getX()), coordinate.getY(), whosGrid);
        }

    }

    @Then("The ship is sunk")
    public void the_ship_is_sunk() {
        assertEquals(ship.isSunk(), true);
    }

    @Then("The entire planet on {string} grid is revealed")
    public void the_entire_planet_is_revealed(String whosGrid) {
        Grid grid;
        if (whosGrid.equals("my"))
            grid = ownGrid;
        else
            grid = opponentGrid;

        for (Coordinate coordinate : grid.getPlanets().get(0).getPlanetCoordinates()) {
            assertEquals(grid.getTile(coordinate).isHit(), true);
        }
    }

    @And("{string} can no longer shoot with a laser")
    public void can_no_longer_shoot_with_a_laser(String whoCanNot) {
            assertEquals(0, laser.getAmountOfUses());
    }

    @When("The AI shoots a random weapon on a random tile")
    public void the_ai_shoots_a_random_weapon_on_a_random_tile() {
        ai.shoot(null, null, false, false);
    }

    @Then("A random tile on my grid is hit")
    public void a_random_tile_on_my_grid_is_hit() {
        Tile[][] matrixOfTiles = ownGrid.getTiles();
        boolean foundHit = false;

        for (int i = 0; i < matrixOfTiles.length; i++) {
            for (int j = 0; j < matrixOfTiles.length; j++) {
                Tile tile = matrixOfTiles[i][j];
                if (tile.isHit()) {
                    foundHit = true;
                    break; // exit the inner loop
                }
            }
            if (foundHit)
                break; // exit the outer loop
        }
        assertEquals(foundHit, true);
    }

    @And("An asteroid is placed at coordinate {string} {int}")
    public void an_asteroid_is_placed_at_coordinate(String x, int y) {
        Coordinate coordinate = new Coordinate(x.charAt(0), y);
        ownGrid.getTile(coordinate).setAsteroid(asteroid);
    }

    @And("The ship is not sunk")
    public void the_ship_is_not_sunk() {
        assertEquals(ship.isSunk(), false);
    }

    @And("The AI is not tracking down any ship")
    public void the_AI_is_not_tracking_down_any_ship() {
        assertEquals(ai.getFollowTragetMode(), false);
    }

    @When("{string} a laser at {string}")
    public void shoot_a_laser_at_obstacle(String whoShoots, String obstacle) {
        if (whoShoots.equals("I shoot")) {
            if (obstacle.equals("an asteroid"))
                coordinateToShoot = opponentGrid.getAsteroids().get(0).getCoordinate();
            else if (obstacle.equals("a planet"))
                coordinateToShoot = opponentGrid.getPlanets().get(0).getCoordinate();
            player.shootLaser(coordinateToShoot, 'c', laser);
        } else if (whoShoots.equals("The AI shoots")) {
            if (obstacle.equals("an asteroid"))
                coordinateToShoot = ownGrid.getAsteroids().get(0).getCoordinate();
            else if (obstacle.equals("a planet"))
                coordinateToShoot = ownGrid.getPlanets().get(0).getCoordinate();
            ai.shootLaser(coordinateToShoot, 'c', laser);
        }
    }

    @And("The laser was stopped by {string}")
    public void the_laser_was_stopped_by_obstacle(String obstacle) {
        Coordinate coordinateToCheck;
        int newY;
        char X;
        if (obstacle.equals("an asteroid")) {
            coordinateToCheck = opponentGrid.getAsteroids().get(0).getCoordinate();
            newY = coordinateToCheck.getY() + 1;
            X = coordinateToCheck.getX();
            assertEquals(ownGrid.getTile(new Coordinate(X, newY)).isHit(), true);
        } else if (obstacle.equals("a planet")) {
            coordinateToCheck = opponentGrid.getPlanets().get(0).getCoordinate();
            newY = coordinateToCheck.getY() + 2;
            X = coordinateToCheck.getX();
            assertEquals(ownGrid.getTile(new Coordinate(X, newY)).isHit(), false);
        }
    }


    // PERSISTENCY LAYER 

    @When("I reload the game") 
    public void i_reload_the_game() { 
       game = game.load(Game.getDefaultPath()); 
    }

    @Then("There is an asteroid at coordinate {string} {int} on {string} grid")
    public void there_is_an_asteroid_at_coordinate_on(String x, int y, String whosGrid) {
        Coordinate coordinate = new Coordinate(x.charAt(0), y);
        if (whosGrid.equals("my"))
            assertEquals(ownGrid.getTile(coordinate).getAsteroid(), asteroid);
        else
            assertEquals(opponentGrid.getTile(coordinate).getAsteroid(), asteroid);
    }
}
