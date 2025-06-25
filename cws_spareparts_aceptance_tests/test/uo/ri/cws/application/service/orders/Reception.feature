Feature: Order reception
  As a manager
  I need to receive and register in arrival of orders
  To restore my stockage and then sell more spare parts

  Scenario: Try to validate the reception of a non existing order
    When I try to receive an non existent order
    Then an error happens with an explaining message

  Scenario: Try to validate the reception of an already received order
    Given an order in state RECEIVED
    When I try to receive the order
    Then an error happens with an explaining message

  Scenario: Try to validate the reception with null code
    When I try to receive an order with null code
    Then argument is rejected with an explaining message

  Scenario Outline: Computing the new price of spare parts
    Given a spare with code <spare>, current stock <stock> and price <price>
    And an order with a line for spare code <spare>, quantity <quantity> and supply price <supplyPrice>
    When I receive the order
    Then the new price for the spare part code <spare> is <newPrice>
    And the new stock for the spare part code <spare> is <newStock>
    And the state of the order is RECEIVED
    And the reception date of the order is today

    Examples: 
      | spare | stock | price | quantity | supplyPrice | newPrice | newStock |
      | "SP1" |     2 | 100.0 |       10 |       110.0 |   126.66 |       12 |
      | "SP1" |    10 | 100.0 |       10 |       110.0 |   116.00 |       20 |
      | "SP1" |     0 | 100.0 |       10 |       110.0 |   132.00 |       10 |
      | "SP1" |     2 | 100.0 |       10 |        90.0 |   106.66 |       12 |
      | "SP1" |     2 | 100.0 |        1 |        10.0 |    70.66 |        3 |
