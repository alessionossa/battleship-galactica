@tag
Feature: Deleting ships on a grid

  @tag1
  Scenario: Successfully removing ships from the grid
    Given I have started a new game
    And I place a "Deathstar" in direction "v" on coordinate "b" 2 on my grid
    When I remove the ship
    Then The ship disappears from my grid

   @tag2
  Scenario: Remove unplaced ship from the grid
    Given I have started a new game
    When I remove the ship
    Then I get a error message "Ship has not been placed in the grid before"