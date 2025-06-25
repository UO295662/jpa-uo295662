Feature: Find not yet invoiced workorders for a client by client nif
  As a Cashier
  I need to find not yet invoiced workorders
  To generate the invoice

  Scenario: Find not invoiced workorders by client nif
    Given a client registered
    And a vehicle
    And a list of several finished workorder ids
    When I search not invoiced workorders by client nif
    Then I get only finished workorders

  Scenario: Find not invoiced workorders by client nif with no workorders
    Given a client registered
    When I search not invoiced workorders by client nif
    Then I get an empty list

  Scenario: Find not invoiced workorders by client nif with some not finished workorders
    Given a client registered
    And a vehicle
    And a list of several finished workorder ids
    And one INVOICED workorder
    And one OPEN workorder
    And one ASSIGNED workorder
    When I search not invoiced workorders by client nif
    Then I get only finished workorders

  Scenario: Try to find workorders for a non existent client
    When I search workorders for a non existent nif
    Then I get an empty list

  Scenario: Try to find workorders with wrong parameters
    When I look for a workorder by empty nif
    Then I get an empty list

  Scenario: Try to find workorders with null argument
    When I try to find workorders with null nif
    Then argument is rejected with an explaining message
