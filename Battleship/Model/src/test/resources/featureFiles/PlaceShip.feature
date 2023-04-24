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
    Given I have started a new game in asteroid mode
