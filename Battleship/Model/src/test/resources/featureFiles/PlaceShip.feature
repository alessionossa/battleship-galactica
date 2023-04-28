@tag
Feature: Placing ships on a grid

  @tag1
  Scenario: User successfully places a ship
    Given I have started a new game on a size 10 grid in "multi" player mode, "without" asteroid mode, "without" gravity mode
    When "I place" a "Cruiser" in direction "h" on coordinate "a" 9
    Then The ship is placed on tiles "a" 9, "b" 9 and "c" 9

  @tag2
  Scenario: AI places its ships
    Given I have started a new game on a size 10 grid in "single" player mode, "without" asteroid mode, "without" gravity mode
    When The AI places its ships
    Then All the AI ships are placed on tiles

  @tag3
  Scenario: Unable to place a ship on an obstacle
    Given I have started a new game on a size 15 grid in "single" player mode, <asteroids> asteroid mode, <planets> gravity mode
    When I try to place a "Cruiser" on <obstacle>
    Then The ship is not placed on the grid

    Examples: 
      | asteroids | planets   | obstacle      |
      | "with"    | "without" | "an asteroid" |
      | "without" | "with"    | "a planet"    |

  @tag4
  Scenario: Unable to place a ship on another ship
    Given I have started a new game on a size 10 grid in "single" player mode, "without" asteroid mode, "without" gravity mode
    And "I place" a "Deathstar" in direction "v" on coordinate "b" 3
    When I try to place a "Scout" on "the ship"
    Then The ship is not placed on the grid

  @tag5
  Scenario: Unable to place ship out of bounds
    Given I have started a new game on a size 10 grid in "single" player mode, "without" asteroid mode, "without" gravity mode
    When I try to place a "Deathstar" on "the right corner of the grid"
    Then The ship is not placed on the grid
