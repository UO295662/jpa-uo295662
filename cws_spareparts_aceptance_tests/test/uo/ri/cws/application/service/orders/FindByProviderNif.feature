Feature: Find orders by provider nif
  As a Manager
  I need to list orders from a provider
  To see its details

  Scenario Outline: Find orders of a provider
    Given the following relation of orders
      | code | order lines | amount | provider |
      | O1   |           4 |    100 | P1       |
      | O2   |           1 |     20 | P1       |
      | O3   |           2 |     50 | P2       |
    When I look for orders of <provider>
    Then I get a list of <quantity> orders with <codes>

    Examples: 
      | provider | quantity | codes    |
      | "P1"     |        2 | "O1, O2" |
      | "P2"     |        1 | "O3"     |

  Scenario Outline: Find orders for a non existing provider
    When I find orders for a non existing provider with <nif>
    Then an empty list is returned

    Examples: 
      | nif             |
      | "does-not-exist" |
      | ""               |

  Scenario: Try to find orders for a null provider nif
    When I try to find orders for a null provider nif
    Then argument is rejected with an explaining message
