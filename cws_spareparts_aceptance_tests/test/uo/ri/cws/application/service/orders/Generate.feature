Feature: Generate orders for spare parts
  As a Manager
  I want to be able to automatically generate orders 
  For restoring the stockage of the warehouse an continue earning money selling spareparts

  Scenario: With just one provider and for just one spare part over stock
    Given a relation of spare parts, providers and supplies
      | sparePart | provider | price |
      | SP1       | P1       | 100.0 |
    And a stock of spare parts
      | sparePart | stock | min | max |
      | SP1       |     6 |   5 |  10 |
    When new orders are generated
    Then we get no new orders

  Scenario: With just one provider and for just one spare part under stocked
    Given a relation of spare parts, providers and supplies
      | sparePart | provider | price |
      | SP1       | P1       | 100.0 |
    And a stock of spare parts
      | sparePart | stock | min | max |
      | SP1       |     1 |   5 |  10 |
    When new orders are generated
    Then we get the following orders
      | provider | amount |
      | P1       |    900 |
    And we get the following order lines
      | provider | sparePart | price | quantity |
      | P1       | SP1       | 100.0 |        9 |

  Scenario: No new order is generated when an order is pending for an spare under stocked 
    Given a relation of spare parts, providers and supplies
      | sparePart | provider | price |
      | SP1       | P1       | 100.0 |
    And a stock of spare parts
      | sparePart | stock | min | max |
      | SP1       |     1 |   5 |  10 |
    And an order is already generated
    When new orders are generated
    Then we get no new orders

  Scenario: Two providers for one spare part, being one cheaper than the other
    Given a relation of spare parts, providers and supplies
      | sparePart | provider | price |
      | SP1       | P1       | 100.0 |
      | SP1       | P2       | 111.0 |
    And a stock of spare parts
      | sparePart | stock | min | max |
      | SP1       |     1 |   5 |  10 |
    When new orders are generated
    Then we get the following orders
      | provider | amount |
      | P1       |  900.0 |
    And we get the following order lines
      | provider | sparePart | price | quantity |
      | P1       | SP1       | 100.0 |        9 |

  Scenario: Two providers for one spare part, same price, but different delivery term
    Given a relation of spare parts, providers and supplies
      | sparePart | provider | price | delivery |
      | SP1       | P1       | 100.0 |        5 |
      | SP1       | P2       | 100.0 |        6 |
    And a stock of spare parts
      | sparePart | stock | min | max |
      | SP1       |     1 |   5 |  10 |
    When new orders are generated
    Then we get the following orders
      | provider | amount |
      | P1       |    900 |
    And we get the following order lines
      | provider | sparePart | price | quantity |
      | P1       | SP1       | 100.0 |        9 |

  Scenario: Two supplies same price, same delivery term, but one with other items ordered
    Given a relation of spare parts, providers and supplies
      | sparePart | provider | price  | delivery |
      | SP1       | P1       |  100.0 |        5 |
      | SP1       | P2       |  100.0 |        5 |
      | SP2       | P1       | 1000.0 |        5 |
    And a stock of spare parts
      | sparePart | stock | min | max |
      | SP1       |     1 |   5 |  10 |
      | SP2       |     4 |   5 |  10 |
    When new orders are generated
    Then we get the following orders
      | provider | amount |
      | P1       | 6900.0 |
    And we get the following order lines
      | provider | sparePart | price  | quantity |
      | P1       | SP1       |  100.0 |        9 |
      | P1       | SP2       | 1000.0 |        6 |

  Scenario: Two spares from two different providers generate two orders
    Given a relation of spare parts, providers and supplies
      | sparePart | provider | price |
      | SP1       | P1       | 100.0 |
      | SP2       | P2       |  50.0 |
    And a stock of spare parts
      | sparePart | stock | min | max |
      | SP1       |     1 |   5 |  10 |
      | SP2       |     3 |   5 |  10 |
    When new orders are generated
    Then we get the following orders
      | provider | amount |
      | P1       |  900.0 |
      | P2       |  350.0 |
    And we get the following order lines
      | provider | sparePart | price | quantity |
      | P1       | SP1       | 100.0 |        9 |
      | P2       | SP2       |  50.0 |        7 |

  Scenario: A combination of spare parts, supplies and providers
    Given a relation of spare parts, providers and supplies
      | sparePart | provider | price | delivery |
      | SP1       | P1       | 100.0 |        5 |
      | SP1       | P2       | 100.0 |        6 |
      | SP2       | P1       |  70.0 |       10 |
      | SP2       | P2       |  60.0 |       10 |
      | SP2       | P3       |  67.0 |       10 |
      | SP3       | P2       |  90.0 |       10 |
      | SP3       | P3       |  80.0 |       10 |
      | SP4       | P2       |  60.0 |       10 |
      | SP4       | P3       |  50.0 |       10 |
    And a stock of spare parts
      | sparePart | stock | min | max |
      | SP1       |     1 |   5 |  10 |
      | SP2       |     3 |   5 |  10 |
      | SP3       |     6 |  10 |  50 |
      | SP4       |    16 |  20 |  70 |
    When new orders are generated
    Then we get the following orders
      | provider | amount |
      | P1       |    900 |
      | P2       |    420 |
      | P3       |   6220 |
    And we get the following order lines
      | provider | sparePart | price | quantity |
      | P1       | SP1       | 100.0 |        9 |
      | P2       | SP2       |  60.0 |        7 |
      | P3       | SP3       |  80.0 |       44 |
      | P3       | SP4       |  50.0 |       54 |
