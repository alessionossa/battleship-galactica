@tag
Feature: Playing a turn

  @tag1
  Scenario: Successfully shoot with a cannon
    Given I have started a new game and I have placed all my ships on my grid
    And My opponent has placed a ship of type "Cruiser" at coordinate "a" 1 in direction "h" on their grid
    When I shoot a cannon at coordinate "a" 1 on my opponent's grid
    Then The tile "a" 1 on my opponent's grid is hit
    And I get a message "You hit something!" regarding the result of the cannon shot at coordinate "a" 1

  @tag2
  Scenario: Successfully shoot with a laser
    Given I have started a new game and I have placed all my ships on my grid
    And My opponent has placed a ship of type "Cruiser" at coordinate "a" 1 in direction "h" on their grid
    When I shoot a laser at row 1 on my opponent's grid
    Then The row 1 on my opponent's grid is hit
    And I get a message "You sunk a ship! 💥🚢" regarding the result of the laser shot at row 1

  @tag3
  Scenario: Successfully shoot with a grenade
    Given I have started a new game and I have placed all my ships on my grid
    And My opponent has placed a ship of type "Cruiser" at coordinate "a" 1 in direction "h" on their grid
    When I shoot a grenade at coordinate "a" 3 on my opponent's grid
    Then The tile "a" 3 on my opponent's grid is hit
    And I get a message "You hit a ship! 💥🚢" regarding the result of the grenade shot at coordinate "a" 1

  @tag4
  Scenario: Successfully shot and sunk a ship
    Given I have started a new game and I have placed all my ships on my grid
    And My opponent has placed a ship of type "Cruiser" at coordinate "a" 1 in direction "v" on their grid
    And Coordinate "a" 1 on my opponent's grid has been hit
    And Coordinate "a" 2 on my opponent's grid has been hit
    When I shoot at coordinate "a" 3 on my opponent's grid
    Then I get a message "You sunk a ship! 💥🚢" regarding the result of the shot at coordinate "a" 3

  @tag5
  Scenario: Successfully shot but missed
    Given I have started a new game and I have placed all my ships on my grid
    And My opponent has placed a ship of type "Scout" at coordinate "d" 5 in direction "h" on their grid
    When I shoot at coordinate "a" 9 on my opponent's grid
    Then The tile "a" 9 on my opponent's grid is hit
    And I get a message "You missed" regarding the result of the shot at coordinate "a" 9
