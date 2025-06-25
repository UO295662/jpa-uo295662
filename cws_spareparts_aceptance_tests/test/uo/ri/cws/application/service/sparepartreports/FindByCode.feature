Feature: Find by code a spare part
  As a Manager
  I need to recover a spare part
  To see its details

  Scenario Outline: Find an existing spare part report
    Given the following relation of spare parts with sells
      | code | desc     | price | stock | minStock | maxStock | sells |
      | SP1  | sp1-desc |  10.0 |     4 |        5 |       10 |     0 |
      | SP2  | sp2-desc |  11.0 |     3 |        6 |       11 |     1 |
      | SP3  | sp3-desc |  12.0 |     2 |        7 |       12 |     2 |
      | SP4  | sp4-desc |  13.0 |     1 |        8 |       13 |     3 |
    When I look for a spare part report by code <code>
    Then I get the spare part report with <desc>, <price>, <stock>, <minStock>, <maxStock> and <sells>

    Examples: 
      | code  | desc       | price | stock | minStock | maxStock | sells |
      | "SP1" | "sp1-desc" |  10.0 |     4 |        5 |       10 |     0 |
      | "SP2" | "sp2-desc" |  11.0 |     3 |        6 |       11 |     1 |
      | "SP3" | "sp3-desc" |  12.0 |     2 |        7 |       12 |     2 |
      | "SP4" | "sp4-desc" |  13.0 |     1 |        8 |       13 |     3 |

  Scenario: Try to find a spare part report with non existing code
    When I try to find a non existing spare part report
    Then empty is returned

  Scenario: Try to find a spare part report with null code
    When I try to find a spare part report with null code
    Then argument is rejected with an explaining message
