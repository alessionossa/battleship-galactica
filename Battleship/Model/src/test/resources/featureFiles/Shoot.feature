@tag
Feature: Getting notified after a shot

  @tag1
  Scenario: Successfully shot a ship
    Given I have started a new game and I have placed all my ships on my grid
    And My opponents has placed a ship of type "Cruiser" at coordinate "a" 1 on his/her grid, with the direction "h"
    When I shoot at coordinate "a" 1 on his/her grid
    Then I get a message "You hit something!" regarding the result of the shot at coordinate "a" 1 on his/her grid

  @tag2
  Scenario: Successfully shot and sunk a ship
    Given I have started a new game and I have placed all my ships on my grid
    And My opponents has placed a ship of type "Cruiser" at coordinate "a" 1 on his/her grid, with the direction "h"
    When I shoot at coordinate "a" 1 on his/her grid
    Then I get a message "You hit something!" regarding the result of the shot at coordinate "a" 1 on his/her grid
    When I shoot at coordinate "a" 2 on his/her grid
    Then I get a message "You hit something!" regarding the result of the shot at coordinate "b" 1 on his/her grid
    When I shoot at coordinate "a" 3 on his/her grid
    Then I get a message "You sunk a ship! 💥🚢" regarding the result of the shot at coordinate "c" 1 on his/her grid


  @tag3
  Scenario: Successfully shot but missed
    Given I have started a new game and I have placed all my ships on my grid
    And My opponents has placed a ship of type "Scout" at coordinate "d" 5 on his/her grid, with the direction "h"
    When I shoot at coordinate "a" 9 on his/her grid
    Then I get a message "You missed" regarding the result of the shot at coordinate "a" 9 on his/her grid
