@tag
Feature: Placing ships on a grid

  @tag1
  Scenario: User successfully places a ship
    Given I have started a new game
    When I place a "Cruiser" in direction "h" on coordinate "a" 0 on my grid
    Then The ship is placed on tiles "a" 0, "b" 0 and "c" 0

  @tag2
  Scenario: AI places its ships
    Given I have started a new game with the AI
    When The AI places its ships
    Then All the AI ships are placed on tiles
