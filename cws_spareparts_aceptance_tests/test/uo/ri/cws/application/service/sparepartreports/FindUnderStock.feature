Feature: Find spare parts under stock
  As a Manager
  I need to recover the spare parts that are under stock
  To watch the state of the warehouse and then generate orders if needed

  Scenario: Find spare parts under stock
    Given the following relation of spare parts with stock
      | code | stock | minStock |
      | SP1  |     0 |        3 |
      | SP2  |     1 |        3 |
      | SP3  |     2 |        3 |
      | SP4  |     3 |        3 |
      | SP5  |     4 |        3 |
      | SP6  |     4 |        5 |
      | SP7  |     5 |        5 |
      | SP8  |     6 |        5 |
      | SP9  |     6 |        0 |
    When I look for spare parts under stock
    Then I get the following relation of spare parts
      | code |
      | SP1  |
      | SP2  |
      | SP3  |
      | SP6  |
