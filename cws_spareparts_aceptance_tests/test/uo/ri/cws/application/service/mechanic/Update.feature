Feature: Update a mechanic
  As a Manager
  I want to update a mechanic

  Scenario: Update an existing mechanic
    Given a mechanic
    When I update the mechanic
    Then the mechanic results updated for fields name and surname

  Scenario: Try to update a non existing mechanic
    When I try to update a non existing mechanic
    Then an error happens with an explaining message

  Scenario: Try to update a mechanic updated in the while
    Given a mechanic
    When I try to update a mechanic updated in the while
    Then an error happens with an explaining message

  Scenario Outline: Try to update a mechanic with null name
    When I try to update a mechanic with null name
    Then argument is rejected with an explaining message

  Scenario Outline: Try to update a mechanic with null surname
    When I try to update a mechanic with null surname
    Then argument is rejected with an explaining message

  Scenario Outline: Try to update a mechanic with null nif
    Given a mechanic
    When I try to update a mechanic with null nif
    Then argument is rejected with an explaining message

  Scenario Outline: Try to update with wrong parameters
    Given a mechanic
    When I try to update the mechanic to <name>, <surname> and <nif>
    Then argument is rejected with an explaining message
    Examples: 
      | nif            | name        | surname    |
      | "existing nif" | "Mechanic1" | ""         |
      | "existing nif" | "Mechanic1" | "     "    |
      | "existing nif" | ""          | "surname1" |
      | "existing nif" | "         " | "surname1" |
