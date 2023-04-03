@tag
Feature: Placing ships on a grid

  @tag1
  Scenario: Successfully place a ship
    Given Not all my ships have been placed
    When I place a "Cruiser" in direction "h" on coordinate "a" 0
    Then The ship is placed on tiles "a" 0, "b" 0 and "c" 0
