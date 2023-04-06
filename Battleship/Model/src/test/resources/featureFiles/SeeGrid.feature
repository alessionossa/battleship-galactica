@tag
Feature: See player's grid 

  @tag1
  Scenario: Successfully visualize the player's grid 
    Given I have started a new game
    When It is my turn
    Then I am able to visualize my own grid

  @tag1
  Scenario: Successfully visualize the player's opponent grid 
    Given I have started a new game
    When It is my turn
    Then I am able to visualize my opponent's grid