@tag
Feature: Playing a turn

  @tag1
  Scenario: Successfully shoot with a cannon
    Given I have started a new game
    And I have placed all my ships on my grid
    And My opponent has placed a ship of type "Cruiser" at coordinate "a" 1 in direction "h" on their grid
    When "I" shot a cannon at coordinate "a" 1
    Then The tile "a" 1 on my "opponent's" grid is hit
    And I get a message "You hit something!" regarding the result of the cannon shot at coordinate "a" 1

  @tag2
  Scenario: Successfully shoot with a laser
    Given I have started a new game
    And I have placed all my ships on my grid
    And My opponent has placed a ship of type "Cruiser" at coordinate "a" 1 in direction "h" on their grid
    When I shoot a laser at row 1 on my opponent's grid
    Then The row 1 on my opponent's grid is hit
    And I get a message "You sunk a ship! 💥🚢" regarding the result of the laser shot at row 1

  @tag3
  Scenario: Successfully shoot with a grenade
    Given I have started a new game
    And I have placed all my ships on my grid
    And My opponent has placed a ship of type "Cruiser" at coordinate "a" 1 in direction "h" on their grid
    When I shoot a grenade at coordinate "a" 2 on my opponent's grid
    Then The tile "a" 2 on my "opponent's" grid is hit
    And 2 random adjacent tiles on my opponent's grid are hit

  @tag4
  Scenario: AI shoots with a cannon
    Given I have started a new game with the AI
    And The AI places its ships
    And I place a "Cruiser" in direction "h" on coordinate "a" 0 on my grid
    When "The AI" shot a cannon at coordinate "a" 0
    Then The tile "a" 0 on my "own" grid is hit
