@tag
Feature: Playing a turn

  @tag1
  Scenario: Successfully shot a ship
    Given I have started a new game and I have placed all my ships on my grid
    And My opponent has placed a ship of type "Cruiser" at coordinate "a" 1 in direction "h" on their grid
    When I shoot at coordinate "a" 1 on my opponent's grid
    Then The tile "a" 1 on my opponent's grid is hit
    And I get a message "You hit something!" regarding the result of the shot at coordinate "a" 1

  @tag2
  Scenario: Successfully shot and sunk a ship
    Given I have started a new game and I have placed all my ships on my grid
    And My opponent has placed a ship of type "Cruiser" at coordinate "a" 1 in direction "v" on their grid
    And Coordinate "a" 1 on my opponent's grid has been hit
    And Coordinate "a" 2 on my opponent's grid has been hit
    When I shoot at coordinate "a" 3 on my opponent's grid
    Then I get a message "You sunk a ship! 💥🚢" regarding the result of the shot at coordinate "a" 3

  @tag3
  Scenario: Successfully shot but missed
    Given I have started a new game and I have placed all my ships on my grid
    And My opponent has placed a ship of type "Scout" at coordinate "d" 5 in direction "h" on their grid
    When I shoot at coordinate "a" 9 on my opponent's grid
    Then The tile "a" 9 on my opponent's grid is hit
    And I get a message "You missed" regarding the result of the shot at coordinate "a" 9
