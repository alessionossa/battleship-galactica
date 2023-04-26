@tag
Feature: Deleting ships on a grid

  @tag1
  Scenario: Successfully removing vertical ship from the grid
    Given I have started a new game on a size 10 grid in "multi" player mode, "without" asteroid mode, "without" gravity mode
    And "I place" a "Deathstar" in direction "v" on coordinate "b" 2
    When I remove the ship
    Then The ship disappears from my grid

  @tag2
  Scenario: Remove unplaced ship from the grid
    Given I have started a new game on a size 10 grid in "multi" player mode, "without" asteroid mode, "without" gravity mode
    When I remove the ship
    Then I get a error message "Ship has not been placed in the grid before"
  
  @tag3
  Scenario: Successfully removing horizontal ship from the grid
    Given I have started a new game on a size 10 grid in "multi" player mode, "without" asteroid mode, "without" gravity mode
    And "I place" a "Cruiser" in direction "h" on coordinate "d" 4
    When I remove the ship
    Then The ship disappears from my grid

