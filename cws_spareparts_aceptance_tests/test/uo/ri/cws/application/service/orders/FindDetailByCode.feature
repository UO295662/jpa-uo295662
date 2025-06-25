Feature: Find order details by code
  As a Manager
  I need to view recover a given order
  To see its details

  Scenario Outline: Find an existing order by code
    Given the following relation of orders
      | code | order lines | amount | provider |
      | O1   |           4 |    100 | P1       |
      | O2   |           1 |     20 | P1       |
      | O3   |           2 |     50 | P2       |
    When I look details of the order with <code>
    Then I get all the details for the order <code>

    Examples: 
      | code |
      | "O1" |
      | "O2" |
      | "O2" |

  Scenario Outline: Find an order with non existing code
    When I find a non existing order with <code>
    Then empty is returned

    Examples: 
      | code             |
      | "does-not-exist" |
      | ""               |

  Scenario: Try to find a null code
    When I try to find a null code
    Then argument is rejected with an explaining message
