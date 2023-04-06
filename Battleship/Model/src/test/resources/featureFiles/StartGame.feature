@tag
Feature: Starting a new game

  @tag1
  Scenario: Start game with a person
    Given I have not started a new game
    When I choose to start a new game with a person
    Then A multiplayer game has been started


