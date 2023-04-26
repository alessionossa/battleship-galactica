@tag
Feature: Playing a turn

  @tag1
  Scenario: Successfully shoot with a cannon
    Given I have started a new game on a size 10 grid in "multi" player mode, "without" asteroid mode, "without" gravity mode
    And I have placed all my ships on my grid
    And "My opponent places" a "Cruiser" in direction "h" on coordinate "a" 1
    When "I shoot" a cannon at coordinate "a" 1
    Then The tile "a" 1 on "my opponent's" grid is hit

  @tag2
  Scenario: Successfully shoot with a cannon at an obstacle
    Given I have started a new game on a size 10 grid in "multi" player mode, "with" asteroid mode, "without" gravity mode
    And I have placed all my ships on my grid
    When "I shoot" a cannon at "an asteroid"
    Then The "asteroid" on my opponent's grid is hit

  @tag3
  Scenario: Successfully shoot with a cannon at a planet
    Given I have started a new game on a size 10 grid in "multi" player mode, "without" asteroid mode, "with" gravity mode
    And I have placed all my ships on my grid
    When "I shoot" a cannon at "a planet"
    Then The "planet" on my opponent's grid is hit
    And The entire planet is revealed

  @tag2
  Scenario: Successfully shoot with a laser at row
    Given I have started a new game on a size 10 grid in "multi" player mode, "without" asteroid mode, "without" gravity mode
    And I have placed all my ships on my grid
    And "My opponent places" a "Cruiser" in direction "h" on coordinate "a" 1
    When I shot a laser at row 1
    Then The row 1 on my opponent's grid is hit
    And The ship is sunk
    And I can no longer shoot with a laser

  @tag2
  Scenario: Successfully shoot with a laser at column
    Given I have started a new game on a size 10 grid in "multi" player mode, "without" asteroid mode, "without" gravity mode
    And I have placed all my ships on my grid
    And "My opponent places" a "Cruiser" in direction "v" on coordinate "a" 1
    When I shot a laser at column "a"
    Then The column "a" on my opponent's grid is hit
    And The ship is sunk
    And I can no longer shoot with a laser

  @tag3
  Scenario: Successfully shoot with a grenade
    Given I have started a new game on a size 10 grid in "multi" player mode, "without" asteroid mode, "without" gravity mode
    And I have placed all my ships on my grid
    And "My opponent places" a "Cruiser" in direction "h" on coordinate "a" 1
    When I shot a grenade at coordinate "a" 2
    Then The tile "a" 2 on "my opponent's" grid is hit
    And 2 random adjacent tiles on "my opponent's" grid are hit

  @tag4
  Scenario: AI shoots with a cannon
    Given I have started a new game on a size 10 grid in "single" player mode, "without" asteroid mode, "without" gravity mode
    And The AI places its ships
    And "I place" a "Cruiser" in direction "h" on coordinate "a" 0
    When "The AI shoots" a cannon at coordinate "a" 9
    Then The tile "a" 9 on "my" grid is hit
