Feature: Delete a supply
  As a Manager
  I want to delete a supply
  to keep the system clean

  Scenario: Delete an existing supply
    Given a supply
    When I remove the supply
    Then the supply gets removed

  Scenario: Try to remove a non existing supply
    When I try to remove a non existent supply
    Then an error happens with an explaining message

  Scenario: Try to remove a supply with null nif
    When I try to remove a supply with null nif
    Then argument is rejected with an explaining message
