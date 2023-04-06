@tag
Feature: Starting a new game 

  @tag1
  Scenario: Successfully start game with a person
    Given I have not started a new game
    When I choose to start a new game with a person
    Then A multiplayer game has been started

  @tag2
  Scenario: Successfully start game with the computer 
    Given I have not started a new game
    When  I choose to start a new game with the computer
    Then A game against the computer has been started
