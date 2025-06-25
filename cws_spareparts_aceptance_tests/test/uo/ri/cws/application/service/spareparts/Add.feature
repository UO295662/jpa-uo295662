Feature: Add a SparePart
  As a Manager
  I want to add a sparepart
  For been able to solve workorders and earn some money selling spareparts

  Scenario: Add a non existing spare part
  	Given a non-registered spare part
    When I add the spare part
    Then the spare part gets added to the system

  Scenario: Try to add a spare with a repeated code
    Given a registered spare part
    When I try to add a new spare part with same code
    Then an error happens with an explaining message

  Scenario Outline: Try to add a spare part with wrong values
    When I try to add a spare part with data
      | code   | description   | price   | stock   | minStock   | maxStock   |
      | <code> | <description> | <price> | <stock> | <minStock> | <maxStock> |
    Then an error happens with an explaining message

    Examples: 
      | code | description | price | stock | minStock | maxStock |
      | sp1  | sparepart   |  -1.0 |    10 |        5 |       25 |
      | sp1  | sparepart   | 100.0 |    -1 |        5 |       25 |
      | sp1  | sparepart   | 100.0 |    10 |       -1 |       25 |
      | sp1  | sparepart   | 100.0 |    10 |        5 |       -1 |

  Scenario Outline: Try to add a spare part with invalid arguments
    When I try to add a spare part with data
      | code   | description   | price   | stock   | minStock   | maxStock   |
      | <code> | <description> | <price> | <stock> | <minStock> | <maxStock> |
    Then argument is rejected with an explaining message

    Examples: 
      | code | description | price | stock | minStock | maxStock |
      |      | sparepart   | 100.0 |    10 |        5 |       25 |
      | sp1  |             | 100.0 |    10 |        5 |       25 |

  Scenario Outline: Try to add a null spare part
    When I try to add a null spare part
    Then argument is rejected with an explaining message
