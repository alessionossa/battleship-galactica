@tag
Feature: Placing ships on a grid

  @tag1
  Scenario: Successfully place a ship
    Given I have started a new game
    When I place a "Cruiser" in direction "h" on coordinate "a" 0 on my grid
    Then The ship is placed on tiles "a" 0, "b" 0 and "c" 0

#We should test different positions for out of bounds errors
  @tag2
  Scenario: Placing ships out of bounds
    Given I have started a new game
    When I place a "Cruiser" in direction "v" on coordinate "a" 9 on my grid
    Then I get a error message "Ship out of bounds"
    