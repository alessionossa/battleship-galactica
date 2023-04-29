@tag
Feature: Testing the persistency layer

  @tag1
  Scenario: Loading a saved game
    Given I have started a new game on a size 15 grid in "single" player mode, "with" asteroid mode, "with" gravity mode
    And The AI places its ships
    And An asteroid is placed at coordinate "b" 1
    And "I shoot" a cannon at coordinate "a" 1
    And "I shoot" a cannon at coordinate "d" 7
    When I reload the game
    Then The tile "a" 1 on "my opponent's" grid is hit
    And The tile "d" 7 on "my opponent's" grid is hit
    And There is an asteroid at coordinate "b" 1 on "my opponent's" grid
