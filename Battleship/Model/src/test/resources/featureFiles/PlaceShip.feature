@tag
Feature: Placing ships on a grid


  @tag1
  Scenario: Successfully place a ship
    Given The game has not started 
    And I have an empty grid
    And Not all my ships have been placed
    When I place a ship of length 3 "horizontally" on tile "a0" 
    Then The ship appears on tiles "a0", "a1" and "a2"
    And I have one less ship to be placed


















  @tag2
  Scenario Outline: Title of your scenario outline
    Given I want to write a step with <name>
    When I check for the <value> in step
    Then I verify the <status> in step

    Examples: 
      | name  | value | status  |
      | name1 |     5 | success |
      | name2 |     7 | Fail    |
