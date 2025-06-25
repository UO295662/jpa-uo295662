Feature: Add a mechanic
  As a Manager
  I want to register a mechani 
  Because we need a new worker

  Scenario: Add a non existing mechanic
    When I add a new non existing mechanic
    Then the mechanic results added to the system

  Scenario: Try to add a mechanic with a repeated nif
    Given a mechanic
    When I try to add a new mechanic with same nif
    Then an error happens with an explaining message

  Scenario Outline: Try to add a mechanic with null argument
    When I try to add a new mechanic with null argument
    Then argument is rejected with an explaining message

  Scenario Outline: Try to add a mechanic with null nif
    When I try to add a new mechanic with null nif
    Then argument is rejected with an explaining message

  Scenario Outline: Try to add a mechanic with invalid nif
    When I try to add a new mechanic with <nif>, <name>, <surname>
    Then argument is rejected with an explaining message
    Examples: 
      | nif   | name    | surname    |
      | ""    | "name1" | "surname1" |
      | "   " | "name2" | "surname2" |
