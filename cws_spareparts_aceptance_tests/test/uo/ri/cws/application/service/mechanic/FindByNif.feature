Feature: Find a mechanic by nif
  As a Manager
  I need to recover a mechanic by nif
  To see its details

  Scenario Outline: Find an existing mechanic
    Given a mechanic
    When I look for mechanic by nif
    Then I get mechanic

  Scenario: Try to find a mechanic with non existing nif
    When I try to find a mechanic by a non existent nif
    Then mechanic is not found

  Scenario Outline: Try to find a mechanic with null argument
    When I try to find a mechanic with null nif
    Then argument is rejected with an explaining message
